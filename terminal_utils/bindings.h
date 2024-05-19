#ifndef BINDINGS_H
#define BINDINGS_H

#include <stdint.h>

void enable_raw_mode(void);

void disable_raw_mode(void);

void enter_alternate_screen(void);

void leave_alternate_screen(void);

void clear_screen(void);

void move_cursor(uint16_t x, uint16_t y);

void hide_cursor(void);

void show_cursor(void);

uint8_t read_key(void);

void write_text(const char *text);

void write_centered_text(const char *text, uint8_t color, uint8_t style, int32_t row_offset);

uint8_t arrow_menu(const char *items);

#endif /* BINDINGS_H */
