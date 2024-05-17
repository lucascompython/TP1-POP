use crossterm::event::KeyCode;
use crossterm::terminal::{
    disable_raw_mode as _disable_raw_mode, enable_raw_mode as _enable_raw_mode,
};

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
    crossterm::execute!(std::io::stdout(), crossterm::terminal::EnterAlternateScreen)
        .expect("Unable to enter alternate screen");
}

#[no_mangle]
pub extern "C" fn leave_alternate_screen() {
    crossterm::execute!(std::io::stdout(), crossterm::terminal::LeaveAlternateScreen)
        .expect("Unable to leave alternate screen");
}

#[no_mangle]
pub extern "C" fn clear_screen() {
    crossterm::execute!(
        std::io::stdout(),
        crossterm::terminal::Clear(crossterm::terminal::ClearType::All)
    )
    .expect("Unable to clear screen");
}

#[no_mangle]
pub extern "C" fn move_cursor(x: u16, y: u16) {
    crossterm::execute!(std::io::stdout(), crossterm::cursor::MoveTo(x, y))
        .expect("Unable to move cursor");
}

#[no_mangle]
pub extern "C" fn hide_cursor() {
    crossterm::execute!(std::io::stdout(), crossterm::cursor::Hide).expect("Unable to hide cursor");
}

#[no_mangle]
pub extern "C" fn show_cursor() {
    crossterm::execute!(std::io::stdout(), crossterm::cursor::Show).expect("Unable to show cursor");
}

#[no_mangle]
pub extern "C" fn set_title(title: *const u8, len: usize) {
    let title = unsafe { std::slice::from_raw_parts(title, len) };
    let title = std::str::from_utf8(title).expect("Invalid UTF-8 title");
    crossterm::execute!(std::io::stdout(), crossterm::terminal::SetTitle(title))
        .expect("Unable to set title");
}

#[repr(C)]
pub struct TermSize {
    pub rows: u16,
    pub cols: u16,
}

#[no_mangle]
pub extern "C" fn get_terminal_size() -> *mut TermSize {
    let (cols, rows) = crossterm::terminal::size().expect("Unable to get terminal size");
    let term_size = TermSize { rows, cols };

    Box::into_raw(Box::new(term_size))
}

#[no_mangle]
pub extern "C" fn free_terminal_size(ptr: *mut TermSize) {
    if !ptr.is_null() {
        unsafe {
            drop(Box::from_raw(ptr));
        }
    }
}

fn convert_key_u8(key: KeyCode) -> u8 {
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
    let event = crossterm::event::read().expect("Unable to read event");

    match event {
        crossterm::event::Event::Key(key_event) => convert_key_u8(key_event.code),
        _ => 0,
    }
}
