#ifndef BINDINGS_H
#define BINDINGS_H

#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

typedef struct TermSize {
  uint16_t rows;
  uint16_t cols;
} TermSize;

void enable_raw_mode(void);

void disable_raw_mode(void);

void enter_alternate_screen(void);

void leave_alternate_screen(void);

void clear_screen(void);

void move_cursor(uint16_t x, uint16_t y);

void hide_cursor(void);

void show_cursor(void);

void set_title(const uint8_t *title, uintptr_t len);

struct TermSize *get_terminal_size(void);

void free_terminal_size(struct TermSize *ptr);

uint8_t read_key(void);

#endif /* BINDINGS_H */
