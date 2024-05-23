use std::ffi::{c_char, CStr};
use std::io::{stdout, Write};

use crossterm::event::{self, KeyCode};
use crossterm::style::Stylize;
use crossterm::terminal::{
    disable_raw_mode as _disable_raw_mode, enable_raw_mode as _enable_raw_mode,
};
use crossterm::{cursor, style, terminal};

static mut TERM_SIZE: TermSize = TermSize { rows: 0, cols: 0 };

static mut STDOUT: Option<std::io::Stdout> = None;

fn get_stdout() -> &'static mut std::io::Stdout {
    unsafe { STDOUT.as_mut().unwrap() }
}
struct TermSize {
    rows: u16,
    cols: u16,
}

#[no_mangle]
pub extern "C" fn init() {
    let stdout = stdout();
    let (cols, rows) = terminal::size().expect("Unable to get terminal size");
    unsafe {
        TERM_SIZE.rows = rows;
        TERM_SIZE.cols = cols;
        STDOUT = Some(stdout);
    };

    _enable_raw_mode().unwrap();

    let stdout = get_stdout();

    crossterm::queue!(stdout, terminal::EnterAlternateScreen, cursor::Hide)
        .expect("Unable to initialize terminal");

    stdout.flush().unwrap();
}

#[no_mangle]
pub extern "C" fn deinit() {
    let stdout = get_stdout();
    crossterm::queue!(stdout, terminal::LeaveAlternateScreen, cursor::Show)
        .expect("Unable to deinitialize terminal");

    _disable_raw_mode().unwrap();

    stdout.flush().unwrap();
}

fn read_key() -> KeyCode {
    let event = event::read().expect("Unable to read event");

    match event {
        event::Event::Key(key_event) => {
            if key_event.kind == event::KeyEventKind::Press {
                key_event.code
            } else {
                read_key()
            }
        }
        _ => read_key(), // Ignore other events and try again
    }
}

#[no_mangle]
pub extern "C" fn print_centered_and_wait(text: *const c_char, color: u8, style: u8) {
    let text = unsafe { CStr::from_ptr(text) };
    let text = text.to_str().expect("Invalid UTF-8 text");

    let stdout = get_stdout();

    crossterm::queue!(stdout, terminal::Clear(terminal::ClearType::All)).unwrap();

    write_centered_text(text, color, style, -1);
    write_centered_text("Pressione qualquer tecla para continuar", 0, 4, 1);
    stdout.flush().unwrap();

    read_key();
}

fn convert_u8_to_color(color: u8) -> style::Color {
    match color {
        0 => style::Color::Reset,
        1 => style::Color::Black,
        2 => style::Color::DarkGrey,
        3 => style::Color::Red,
        4 => style::Color::DarkRed,
        5 => style::Color::Green,
        6 => style::Color::DarkGreen,
        7 => style::Color::Yellow,
        8 => style::Color::DarkYellow,
        9 => style::Color::Blue,
        10 => style::Color::DarkBlue,
        11 => style::Color::Magenta,
        12 => style::Color::DarkMagenta,
        13 => style::Color::Cyan,
        14 => style::Color::DarkCyan,
        15 => style::Color::White,
        16 => style::Color::Grey,
        _ => style::Color::Reset,
    }
}

fn convert_u8_to_style(style: u8) -> style::Attribute {
    match style {
        0 => style::Attribute::Reset,
        1 => style::Attribute::Bold,
        2 => style::Attribute::Dim,
        3 => style::Attribute::Italic,
        4 => style::Attribute::Underlined,
        _ => style::Attribute::Reset,
    }
}

fn add(u: u16, i: i32) -> u16 {
    if i < 0 {
        u - i.abs() as u16
    } else {
        u + i as u16
    }
}

fn write_centered_text(text: &str, color: u8, style: u8, row_offset: i32) {
    let y = add(unsafe { TERM_SIZE.rows / 2 }, row_offset);
    let x = (unsafe { TERM_SIZE.cols } - text.len() as u16) / 2;

    let stdout = get_stdout();

    if color != 0 {
        crossterm::queue!(
            stdout,
            style::SetForegroundColor(convert_u8_to_color(color))
        )
        .expect("Unable to set color");
    }

    if style != 0 {
        crossterm::queue!(stdout, style::SetAttribute(convert_u8_to_style(style)))
            .expect("Unable to set style");
    }

    crossterm::queue!(stdout, cursor::MoveTo(x, y), style::Print(text)).unwrap();

    if color != 0 {
        crossterm::queue!(stdout, style::ResetColor).expect("Unable to reset color");
    }

    if style != 0 {
        crossterm::queue!(stdout, style::SetAttribute(style::Attribute::Reset))
            .expect("Unable to reset style");
    }
}

#[no_mangle]
pub extern "C" fn arrow_menu(items: *const c_char) -> u8 {
    let items = unsafe { CStr::from_ptr(items) };
    let items = items.to_str().expect("Invalid UTF-8 text");
    let items: Vec<&str> = items.lines().collect();

    let items_length = items.len();
    let mut selected = 0;

    let stdout = get_stdout();

    crossterm::queue!(stdout, terminal::Clear(terminal::ClearType::All)).unwrap();

    loop {
        for (i, item) in items.iter().enumerate() {
            if i == selected {
                crossterm::queue!(
                    stdout,
                    style::SetForegroundColor(style::Color::Black),
                    style::SetBackgroundColor(style::Color::White),
                )
                .expect("Unable to set color and style");
            }

            let offset = i as i32 - (items_length as i32 / 2);

            write_centered_text(item, 0, 0, offset);

            if i == selected {
                crossterm::queue!(stdout, style::ResetColor)
                    .expect("Unable to reset color and style");
            }
        }

        stdout.flush().unwrap();

        match read_key() {
            KeyCode::Up => {
                if selected > 0 {
                    selected -= 1;
                } else {
                    selected = items_length - 1;
                }
            }
            KeyCode::Down => {
                if selected < items_length - 1 {
                    selected += 1;
                } else {
                    selected = 0;
                }
            }
            KeyCode::Enter => return selected as u8, // enter

            KeyCode::Char(c) => {
                // Match the first item that contains the character
                for (i, item) in items.iter().enumerate() {
                    if item.contains(c) {
                        selected = i;
                        break;
                    }
                }
            }

            _ => {}
        }
    }
}

#[repr(C)]
pub struct Input {
    pub label: *const c_char,
    pub value: *const c_char,
    pub is_checkbox: bool,
    pub checkbox_options: *const c_char, // Multiple options separated by newlines
}

fn print_menu_item(label: &str, value: &str, is_checkbox: bool, selected: bool, offset: i32) {
    let y = add(unsafe { TERM_SIZE.rows / 2 }, offset);
    let x = (unsafe { TERM_SIZE.cols } - label.len() as u16 - value.len() as u16) / 2;

    // I am using write! here because this way I don't need to allocate a new String
    if selected {
        if is_checkbox {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 2, y),).unwrap();
            write!(
                get_stdout(),
                "> {}{}{} <",
                style::Attribute::Underlined,
                label,
                style::Attribute::NoUnderline
            )
            .unwrap();
        } else {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 4, y),).unwrap();
            write!(get_stdout(), "> {}: {} <", label, value).unwrap();
        }
    } else {
        if is_checkbox {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 2, y),).unwrap();

            write!(get_stdout(), "  {}  ", label.underlined()).unwrap();
        } else {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 4, y),).unwrap();
            write!(get_stdout(), "  {}: {}  ", label, value).unwrap();
        }
    }
}

fn print_checkbox_option(option: &str, selected: bool, is_checkbox_selected: bool, offset: i32) {
    let y = add(unsafe { TERM_SIZE.rows / 2 }, offset);
    let x = (unsafe { TERM_SIZE.cols } - option.len() as u16 - 5) / 2;

    if selected {
        if is_checkbox_selected {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 2, y),).unwrap();
            write!(get_stdout(), "> {}: [X] <", option).unwrap();
        } else {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 2, y)).unwrap();
            write!(get_stdout(), "> {}: [ ] <", option).unwrap();
        }
    } else {
        if is_checkbox_selected {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 2, y)).unwrap();
            write!(get_stdout(), "  {}: [X]  ", option).unwrap();
        } else {
            crossterm::queue!(get_stdout(), cursor::MoveTo(x - 2, y)).unwrap();
            write!(get_stdout(), "  {}: [ ]  ", option).unwrap();
        }
    }
}

#[no_mangle]
pub extern "C" fn input_menu(inputs: *const Input, inputs_length: u8) -> bool {
    let inputs: &mut [Input] =
        unsafe { std::slice::from_raw_parts_mut(inputs as *mut Input, inputs_length as usize) };

    let stdout = get_stdout();

    let mut selected = 0;
    let mut selected_checkbox: i32 = -1;
    let mut selected_button = true; // true = OK false = Cancelar

    let mut button_text = String::with_capacity(76);
    let y = add(unsafe { TERM_SIZE.rows / 2 }, 0);

    let checkbox_options = if let Some(last_input) = inputs.last() {
        if last_input.is_checkbox {
            let checkbox_value = unsafe { CStr::from_ptr(last_input.value) }
                .to_str()
                .expect("Invalid UTF-8 text");

            if !checkbox_value.is_empty() {
                selected_checkbox = checkbox_value.parse().unwrap();
            }

            unsafe { CStr::from_ptr(last_input.checkbox_options) }
                .to_str()
                .expect("Invalid UTF-8 text")
                .lines()
                .collect()
        } else {
            Vec::new()
        }
    } else {
        Vec::new()
    };

    let checkbox_length = checkbox_options.len();

    let buttons_offset = if checkbox_length > 0 {
        (inputs_length + checkbox_length as u8) / 2 + 1
    } else {
        ((inputs_length + 1) / 2) + 2 // + 2 to leave a space between the inputs and the buttons
    };

    crossterm::queue!(stdout, terminal::Clear(terminal::ClearType::All)).unwrap();

    loop {
        for (i, input) in inputs.iter().enumerate() {
            let offset = i as i32 - (inputs_length as i32 / 2) - checkbox_length as i32 + 1;

            let label = unsafe { CStr::from_ptr(input.label) }
                .to_str()
                .expect("Invalid UTF-8 text");

            let value = unsafe { CStr::from_ptr(input.value) }.to_str().unwrap();

            print_menu_item(label, value, input.is_checkbox, i == selected, offset);

            if input.is_checkbox {
                for (j, option) in checkbox_options.iter().enumerate() {
                    let offset = if inputs_length >= 2 {
                        (i as i32 + 1) - (checkbox_length as i32 / 2) + j as i32
                            - checkbox_length as i32
                    } else {
                        (i as i32) - (checkbox_length as i32 / 2) + j as i32
                    };
                    print_checkbox_option(
                        option,
                        selected as i32 - inputs_length as i32 == j as i32,
                        j as i32 == selected_checkbox,
                        offset,
                    );
                }
            }
        }

        // Do this to avoid allocating a new String each iteration
        if selected_button {
            button_text.clear();
            button_text.push_str("                              ");
            button_text.push_str(&"[OK]".black().on_white().to_string());
            button_text.push_str("   [CANCELAR]");
        } else {
            button_text.clear();
            button_text.push_str("                              [OK]   ");
            button_text.push_str(&"[CANCELAR]".black().on_white().to_string());
        };
        write_centered_text(&button_text, 0, 0, buttons_offset as i32);

        stdout.flush().unwrap();

        let char = read_key();
        match char {
            KeyCode::Up => {
                if selected > 0 {
                    selected -= 1;
                } else {
                    selected = inputs_length as usize + checkbox_length as usize - 1;
                }
            }

            KeyCode::Down => {
                if selected < inputs_length as usize + checkbox_length as usize - 1 {
                    selected += 1;
                } else {
                    selected = 0;
                }
            }

            KeyCode::Left | KeyCode::Right => {
                selected_button = !selected_button;
            }

            KeyCode::Char(' ') => {
                if selected < inputs_length as usize && inputs[selected].is_checkbox {
                    continue;
                }

                if selected > inputs_length as usize - 1 {
                    // checkbox

                    selected_checkbox = selected as i32 - inputs_length as i32;

                    let input = &mut inputs[inputs_length as usize - 1]; // assuming the last input is the checkbox

                    unsafe {
                        // avoid allocating a new String by writing to the already allocated memory that Java allocated
                        let value = input.value as *mut u8;
                        *value = selected_checkbox as u8 + b'0';
                        *value.add(1) = 0;
                    }
                } else {
                    // normal input

                    let input = &mut inputs[selected];
                    unsafe {
                        let value = input.value as *mut u8;

                        let mut len = 0;
                        while *value.add(len) != 0 {
                            len += 1;
                        }

                        if len + 2 <= 40 {
                            *value.add(len) = b' ';
                            *value.add(len + 1) = 0;
                        }
                    }
                }
            }

            KeyCode::Enter => return selected_button, // enter

            _ => {
                if selected < inputs_length as usize && !inputs[selected].is_checkbox {
                    let input = &mut inputs[selected];

                    unsafe {
                        let value = input.value as *mut u8;

                        let mut len = 0;
                        while *value.add(len) != 0 {
                            len += 1;
                        }

                        if char == KeyCode::Backspace {
                            if len > 0 {
                                len -= 1;
                                *value.add(len) = 0;

                                // Clear the line when backspacing to avoid the '>' '<' artifacts
                                crossterm::queue!(
                                    stdout,
                                    cursor::MoveTo(
                                        0,
                                        y - (inputs_length as u16 / 2) + selected as u16
                                            - checkbox_length as u16
                                            + 1
                                    ),
                                    terminal::Clear(terminal::ClearType::CurrentLine)
                                )
                                .unwrap();
                            }
                        } else {
                            if len + 2 <= 40 {
                                *value.add(len) = match char {
                                    KeyCode::Char(c) => c as u8,
                                    _ => 0,
                                };
                                *value.add(len + 1) = 0;
                            }
                        }
                    }
                }
            }
        }
    }
}

#[no_mangle]
pub extern "C" fn search_menu(items: *const c_char) -> u8 {
    let items = unsafe { CStr::from_ptr(items) };
    let items = items.to_str().expect("Invalid UTF-8 text");
    let items: Vec<(&str, u8)> = items
        .lines()
        .enumerate()
        .map(|(i, item)| (item, i as u8))
        .collect();

    let items_length = items.len();

    let mut filtered_items = Vec::with_capacity(items_length);

    let mut selected = 0;
    let mut search = String::new();

    let stdout = get_stdout();

    let y = add(
        unsafe { TERM_SIZE.rows / 2 },
        (items_length / 2) as i32 - items_length as i32 - 2, // -2 to leave space for the search bar
    );

    loop {
        crossterm::queue!(stdout, terminal::Clear(terminal::ClearType::All)).unwrap();
        let x = (unsafe { TERM_SIZE.cols } - 9 - search.len() as u16) / 2; // 9 is the length of "Pesquisa:"

        // I am not using write_centered_text here because this way I can avoid allocating a new String by not using format!
        crossterm::queue!(
            stdout,
            cursor::MoveTo(x, y),
            style::SetAttribute(style::Attribute::Underlined),
            style::Print("Pesquisa:"),
            style::SetAttribute(style::Attribute::Reset),
            cursor::MoveTo(x + 10, y),
            style::Print(&search)
        )
        .unwrap();

        filtered_items.clear();
        for &(item, i) in items.iter() {
            if item.contains(&search) {
                filtered_items.push((item, i));
            }
        }

        for (i, (item, _)) in filtered_items.iter().enumerate() {
            if i == selected {
                crossterm::queue!(
                    stdout,
                    style::SetForegroundColor(style::Color::Black),
                    style::SetBackgroundColor(style::Color::White),
                )
                .expect("Unable to set color and style");
            }

            let offset = i as i32 - (items_length as i32 / 2);

            write_centered_text(item, 0, 0, offset);

            if i == selected {
                crossterm::queue!(stdout, style::ResetColor)
                    .expect("Unable to reset color and style");
            }
        }

        stdout.flush().unwrap();

        match read_key() {
            KeyCode::Up => {
                if selected > 0 {
                    selected -= 1;
                } else {
                    selected = filtered_items.len() - 1;
                }
            }
            KeyCode::Down => {
                if selected < filtered_items.len() - 1 {
                    selected += 1;
                } else {
                    selected = 0;
                }
            }

            KeyCode::Backspace => {
                search.pop();
                selected = 0;
            }

            KeyCode::Enter => return filtered_items[selected].1, // return original index
            KeyCode::Char(c) => {
                search.push(c);
                selected = 0;
            }
            _ => {}
        }
    }
}
