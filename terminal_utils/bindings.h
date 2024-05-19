#ifndef BINDINGS_H
#define BINDINGS_H

#include <stdint.h>
#include <stdbool.h>

typedef struct Input {
  const char *label;
  const char *value;
  bool is_checkbox;
  const char *checkbox_options;
} Input;

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

bool input_menu(const struct Input *inputs, uint8_t inputs_length);

#endif /* BINDINGS_H */
