use std::ffi::{c_char, CStr, CString};
use std::io::{stdout, Write};

use crossterm::event::{self, KeyCode};
use crossterm::style::Stylize;
use crossterm::terminal::{
    disable_raw_mode as _disable_raw_mode, enable_raw_mode as _enable_raw_mode,
};
use crossterm::{cursor, style, terminal};

static mut TERM_SIZE: TermSize = TermSize { rows: 0, cols: 0 };

macro_rules! print_lines {
    ($text:expr $(, $arg:expr)*) => {
        for line in $text.lines() {
            crossterm::execute!(
                std::io::stdout(),
                $($arg,)*
                style::Print(line),
                cursor::MoveToNextLine(1)
            )
            .expect("Unable to print text");
        }
    };
}
struct TermSize {
    rows: u16,
    cols: u16,
}

#[no_mangle]
pub extern "C" fn enable_raw_mode() {
    _enable_raw_mode().expect("Unable to enable raw mode");
}

#[no_mangle]
pub extern "C" fn disable_raw_mode() {
    _disable_raw_mode().expect("Unable to disable raw mode");
}

#[no_mangle]
pub extern "C" fn enter_alternate_screen() {
    let (cols, rows) = terminal::size().expect("Unable to get terminal size");

    unsafe {
        TERM_SIZE.rows = rows;
        TERM_SIZE.cols = cols;
    }

    crossterm::execute!(stdout(), terminal::EnterAlternateScreen)
        .expect("Unable to enter alternate screen");
}

#[no_mangle]
pub extern "C" fn leave_alternate_screen() {
    crossterm::execute!(stdout(), terminal::LeaveAlternateScreen)
        .expect("Unable to leave alternate screen");
}

#[no_mangle]
pub extern "C" fn clear_screen() {
    crossterm::execute!(stdout(), terminal::Clear(terminal::ClearType::All))
        .expect("Unable to clear screen");
}

#[no_mangle]
pub extern "C" fn move_cursor(x: u16, y: u16) {
    crossterm::execute!(stdout(), cursor::MoveTo(x, y)).expect("Unable to move cursor");
}

#[no_mangle]
pub extern "C" fn hide_cursor() {
    crossterm::execute!(stdout(), cursor::Hide).expect("Unable to hide cursor");
}

#[no_mangle]
pub extern "C" fn show_cursor() {
    crossterm::execute!(stdout(), cursor::Show).expect("Unable to show cursor");
}

fn convert_key_to_u8(key: KeyCode) -> u8 {
    match key {
        KeyCode::Backspace => 8,
        KeyCode::Enter => 13,
        KeyCode::Left => 68,
        KeyCode::Right => 67,
        KeyCode::Up => 65,
        KeyCode::Down => 66,
        KeyCode::Home => 72,
        KeyCode::End => 70,
        KeyCode::PageUp => 53,
        KeyCode::PageDown => 54,
        KeyCode::Tab => 9,
        KeyCode::BackTab => 15,
        KeyCode::Delete => 127,
        KeyCode::Insert => 50,
        KeyCode::F(n) => 59 + n,
        KeyCode::Char(c) => c as u8,
        KeyCode::Null => 0,
        KeyCode::Esc => 27,
        _ => 0,
    }
}

#[no_mangle]
pub extern "C" fn read_key() -> u8 {
    let event = event::read().expect("Unable to read event");

    match event {
        event::Event::Key(key_event) => {
            if key_event.kind == event::KeyEventKind::Press {
                convert_key_to_u8(key_event.code)
            } else {
                read_key()
            }
        }
        _ => read_key(), // Ignore other events and try again
    }
}

#[no_mangle]
pub extern "C" fn write_text(text: *const c_char) {
    let text = unsafe { CStr::from_ptr(text) };
    let text = text.to_str().expect("Invalid UTF-8 text");

    print_lines!(text);
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

fn _write_centered_text(text: &str, color: u8, style: u8, row_offset: i32) {
    let y = add(unsafe { TERM_SIZE.rows / 2 }, row_offset);
    let x = (unsafe { TERM_SIZE.cols } - text.len() as u16) / 2;

    if color != 0 {
        crossterm::execute!(
            stdout(),
            style::SetForegroundColor(convert_u8_to_color(color))
        )
        .expect("Unable to set color");
    }

    if style != 0 {
        crossterm::execute!(stdout(), style::SetAttribute(convert_u8_to_style(style)))
            .expect("Unable to set style");
    }

    print_lines!(text, cursor::MoveTo(x, y));

    if color != 0 {
        crossterm::execute!(stdout(), style::ResetColor).expect("Unable to reset color");
    }

    if style != 0 {
        crossterm::execute!(stdout(), style::SetAttribute(style::Attribute::Reset))
            .expect("Unable to reset style");
    }
}

#[no_mangle]
pub extern "C" fn write_centered_text(text: *const c_char, color: u8, style: u8, row_offset: i32) {
    let text = unsafe { CStr::from_ptr(text) };
    let text = text.to_str().expect("Invalid UTF-8 text");

    _write_centered_text(text, color, style, row_offset);
}

#[no_mangle]
pub extern "C" fn arrow_menu(items: *const c_char) -> u8 {
    let items = unsafe { CStr::from_ptr(items) };
    let items = items.to_str().expect("Invalid UTF-8 text");
    let items: Vec<&str> = items.lines().collect();

    let items_length = items.len();
    let mut selected = 0;

    clear_screen();
    loop {
        for (i, item) in items.iter().enumerate() {
            if i == selected {
                crossterm::execute!(
                    stdout(),
                    style::SetForegroundColor(style::Color::Black),
                    style::SetBackgroundColor(style::Color::White),
                )
                .expect("Unable to set color and style");
            }

            let offset = i as i32 - (items_length as i32 / 2);

            _write_centered_text(item, 0, 0, offset);

            if i == selected {
                crossterm::execute!(stdout(), style::ResetColor)
                    .expect("Unable to reset color and style");
            }
        }

        match read_key() {
            65 => {
                // up
                if selected > 0 {
                    selected -= 1;
                } else {
                    selected = items_length - 1;
                }
            }
            66 => {
                // down
                if selected < items_length - 1 {
                    selected += 1;
                } else {
                    selected = 0;
                }
            }
            13 => return selected as u8, // enter
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

// #[no_mangle]
// pub extern "C" fn input_menu(inputs: *const Input, inputs_length: u8) -> bool {
//     let inputs = unsafe { std::slice::from_raw_parts(inputs, inputs_length as usize) };

//     let mut selected = 0;
//     let mut selected_checkbox_option = 0;

//     loop {
//         clear_screen();

//         for (i, input) in inputs.iter().enumerate() {
//             let offset = i as i32 - (inputs_length as i32 / 2);

//             if i == selected {
//                 crossterm::execute!(
//                     stdout(),
//                     style::SetForegroundColor(style::Color::Black),
//                     style::SetBackgroundColor(style::Color::White),
//                 )
//                 .expect("Unable to set color and style");
//             }

//             let label = unsafe { CStr::from_ptr(input.label) };
//             let label = label.to_str().expect("Invalid UTF-8 text");

//             let value = unsafe { CStr::from_ptr(input.value) };
//             let value = value.to_str().expect("Invalid UTF-8 text");

//             if input.is_checkbox {
//                 let options = unsafe { CStr::from_ptr(input.checkbox_options) };
//                 let options = options.to_str().expect("Invalid UTF-8 text");
//                 let options: Vec<&str> = options.lines().collect();

//                 _write_centered_text(label, 0, 0, offset);

//                 for (j, option) in options.iter().enumerate() {
//                     let offset = j as i32 - (options.len() as i32 / 2);

//                     if j == selected_checkbox_option {
//                         crossterm::execute!(
//                             stdout(),
//                             style::SetForegroundColor(style::Color::Black),
//                             style::SetBackgroundColor(style::Color::White),
//                         )
//                         .expect("Unable to set color and style");
//                     }

//                     let checkbox = if j == selected_checkbox_option {
//                         "X"
//                     } else {
//                         " "
//                     };

//                     print_lines!(
//                         format!("{}: [{}] {}", option, checkbox, value),
//                         cursor::MoveTo(0, add(unsafe { TERM_SIZE.rows / 2 }, offset))
//                     );

//                     if j == selected_checkbox_option {
//                         crossterm::execute!(stdout(), style::ResetColor)
//                             .expect("Unable to reset color and style");
//                     }
//                 }
//             } else {
//                 _write_centered_text(format!("{}: {}", label, value).as_str(), 0, 0, offset);
//             }

//             if i == selected {
//                 crossterm::execute!(stdout(), style::ResetColor)
//                     .expect("Unable to reset color and style");
//             }
//         }

//         let offset = inputs_length as i32 / 2 + 1;

//         if selected == inputs_length as usize {
//             crossterm::execute!(
//                 stdout(),
//                 style::SetForegroundColor(style::Color::Black),
//                 style::SetBackgroundColor(style::Color::White),
//             )
//             .expect("Unable to set color and style");
//         }

//         _write_centered_text("OK", 0, 0, offset);

//         if selected == inputs_length as usize {
//             crossterm::execute!(stdout(), style::ResetColor)
//                 .expect("Unable to reset color and style");
//         }

//         if selected == inputs_length as usize + 1 {
//             crossterm::execute!(
//                 stdout(),
//                 style::SetForegroundColor(style::Color::Black),
//                 style::SetBackgroundColor(style::Color::White),
//             )
//             .expect("Unable to set color and style");
//         }

//         _write_centered_text("Cancel", 0, 0, offset + 1);

//         if selected == inputs_length as usize + 1 {
//             crossterm::execute!(stdout(), style::ResetColor)
//                 .expect("Unable to reset color and style");
//         }

//         match read_key() {
//             65 => {
//                 // up
//                 if selected > 0 {
//                     selected -= 1;
//                 } else {
//                     selected = inputs_length as usize + 1;
//                 }
//             }
//             66 => {
//                 // down
//                 if selected < inputs_length as usize + 1 {
//                     selected += 1;
//                 } else {
//                     selected = 0;
//                 }
//             }
//             13 => {
//                 if selected == inputs_length as usize {
//                     return true;
//                 } else if selected == inputs_length as usize + 1 {
//                     return false;
//                 } else if inputs[selected].is_checkbox {
//                     let options = unsafe { CStr::from_ptr(inputs[selected].checkbox_options) };
//                     let options = options.to_str().expect("Invalid UTF-8 text");
//                     let options: Vec<&str> = options.lines().collect();

//                     if selected_checkbox_option < options.len() - 1 {
//                         selected_checkbox_option += 1;
//                     } else {
//                         selected_checkbox_option = 0;
//                     }
//                 }
//             }
//             68 => {
//                 // left
//                 if selected == inputs_length as usize {
//                     selected = inputs_length as usize + 1;
//                 } else if selected == inputs_length as usize + 1 {
//                     selected = inputs_length as usize;
//                 }
//             }
//             67 => {
//                 // right
//                 if selected == inputs_length as usize {
//                     selected = inputs_length as usize + 1;
//                 } else if selected == inputs_length as usize + 1 {
//                     selected = inputs_length as usize;
//                 }
//             }
//             _ => {}
//         }
//     }
// }

fn print_menu_item(label: &str, value: &str, is_checkbox: bool, selected: bool, offset: i32) {
    let y = add(unsafe { TERM_SIZE.rows / 2 }, offset);
    let x = (unsafe { TERM_SIZE.cols } - label.len() as u16 - 1) / 2;

    // I am using write! here because this way I don't need to allocate a new String
    if selected {
        if is_checkbox {
            crossterm::execute!(stdout(), cursor::MoveTo(x - 2, y),).unwrap();
            write!(
                stdout(),
                "> {}{}{} <\n",
                style::Attribute::Underlined,
                label,
                style::Attribute::NoUnderline
            )
            .unwrap();
        } else {
            crossterm::execute!(stdout(), cursor::MoveTo(x - 2, y)).unwrap();
            write!(stdout(), "> {}: {} <\n", label, value).unwrap();
        }
    } else {
        if is_checkbox {
            crossterm::execute!(
                stdout(),
                cursor::MoveTo(x, y),
                style::SetAttribute(style::Attribute::Underlined)
            )
            .unwrap();

            write!(stdout(), "{}\n", label).unwrap();

            crossterm::execute!(stdout(), style::SetAttribute(style::Attribute::Reset)).unwrap();
        } else {
            crossterm::execute!(stdout(), cursor::MoveTo(x, y)).unwrap();
            write!(stdout(), "{}: {}\n", label, value).unwrap();
        }
    }
}

fn print_checkbox_option(option: &str, selected: bool, is_checkbox_selected: bool, offset: i32) {
    let y = add(unsafe { TERM_SIZE.rows / 2 }, offset);
    let x = (unsafe { TERM_SIZE.cols } - option.len() as u16 - 5) / 2;

    if selected {
        if is_checkbox_selected {
            crossterm::execute!(stdout(), cursor::MoveTo(x - 2, y),).unwrap();
            write!(stdout(), "> {}: [X] <\n", option).unwrap();
        } else {
            crossterm::execute!(stdout(), cursor::MoveTo(x - 2, y)).unwrap();
            write!(stdout(), "> {}: [ ] <\n", option).unwrap();
        }
    } else {
        if is_checkbox_selected {
            crossterm::execute!(stdout(), cursor::MoveTo(x, y)).unwrap();
            write!(stdout(), "{}: [X]\n", option).unwrap();
        } else {
            crossterm::execute!(stdout(), cursor::MoveTo(x, y)).unwrap();
            write!(stdout(), "{}: [ ]\n", option).unwrap();
        }
    }
}

#[no_mangle]
pub extern "C" fn input_menu(inputs: *const Input, inputs_length: u8) -> bool {
    let inputs = inputs as *mut Input;
    let inputs: &mut [Input] =
        unsafe { std::slice::from_raw_parts_mut(inputs, inputs_length as usize) };

    let mut selected = 0;
    let mut selected_checkbox: i32 = -1;
    let mut selected_button = true; // true = OK false = Cancelar

    let mut checkbox_length = 0;
    let mut checkbox_options: Vec<&str> = Vec::new();

    if let Some(last_input) = inputs.last() {
        if last_input.is_checkbox {
            checkbox_options = unsafe { CStr::from_ptr(last_input.checkbox_options) }
                .to_str()
                .expect("Invalid UTF-8 text")
                .lines()
                .collect();

            checkbox_length = checkbox_options.len();
        }
    }

    loop {
        clear_screen();
        for (i, input) in inputs.iter().enumerate() {
            let offset = i as i32 - (inputs_length as i32 / 2) - checkbox_length as i32 + 1;

            let label = unsafe { CStr::from_ptr(input.label) }
                .to_str()
                .expect("Invalid UTF-8 text");

            let value = unsafe { CStr::from_ptr(input.value) }.to_str().unwrap();

            print_menu_item(label, value, input.is_checkbox, i == selected, offset);

            if input.is_checkbox {
                for (j, option) in checkbox_options.iter().enumerate() {
                    let offset = (i as i32 + 1) - (checkbox_length as i32 / 2) + j as i32
                        - checkbox_length as i32;
                    print_checkbox_option(
                        option,
                        j + checkbox_length + 2 == selected,
                        j as i32 == selected_checkbox,
                        offset,
                    );
                }
            }
        }

        if selected_button {
            _write_centered_text(
                &format!(
                    "                              {}   [CANCELAR]",
                    "[OK]".black().on_white()
                ),
                0,
                0,
                inputs_length as i32 / 2 + checkbox_length as i32 + 1,
            )
        } else {
            _write_centered_text(
                &format!(
                    "                              [OK]   {}",
                    "[CANCELAR]".black().on_white()
                ),
                0,
                0,
                inputs_length as i32 / 2 + checkbox_length as i32 + 1,
            )
        }

        let char = read_key();
        match char {
            65 => {
                // up
                if selected > 0 {
                    selected -= 1;
                } else {
                    selected = inputs_length as usize + checkbox_length as usize - 1;
                }
            }

            66 => {
                // down
                if selected < inputs_length as usize + checkbox_length as usize - 1 {
                    selected += 1;
                } else {
                    selected = 0;
                }
            }

            68 => {
                // left
                if selected_button {
                    selected_button = false;
                } else {
                    selected_button = true;
                }
            }

            67 => {
                // right
                if !selected_button {
                    selected_button = true;
                } else {
                    selected_button = false;
                }
            }

            32 => {
                // space
                if selected > inputs_length as usize - 1 {
                    selected_checkbox = selected as i32 - inputs_length as i32;

                    let input = &mut inputs[inputs_length as usize - 1]; // assuming the last input is the checkbox

                    let cstring = CString::new(selected_checkbox.to_string()).unwrap();
                    input.value = cstring.into_raw();
                }
            }

            13 => return selected_button,

            _ => {}
        }

        //     if input.is_checkbox {

        // }
    }
}
