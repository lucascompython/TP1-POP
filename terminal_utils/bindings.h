#ifndef BINDINGS_H
#define BINDINGS_H

#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

void enable_raw_mode(void);

void disable_raw_mode(void);

void enter_alternate_screen(void);

void leave_alternate_screen(void);

void clear_screen(void);

void move_cursor(uint16_t x, uint16_t y);

void hide_cursor(void);

void show_cursor(void);

uint8_t read_key(void);

void write_text(const uint8_t *text, uintptr_t len);

void write_centered_text(const uint8_t *text, uintptr_t len, uint8_t color, uint8_t style);

#endif /* BINDINGS_H */
