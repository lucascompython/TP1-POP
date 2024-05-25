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

typedef struct SearchInput {
  int32_t id;
  const char *text;
} SearchInput;

void init(void);

void deinit(void);

void print_centered_and_wait(const char *text, uint8_t color, uint8_t style);

void print_centered_lines_and_wait(const char *text);

uint8_t arrow_menu(const char *items);

bool input_menu(const struct Input *inputs, uint8_t inputs_length);

uint8_t search_menu(const char *items);

int32_t search_menu_by_id_or_text(const struct SearchInput *items, uint8_t items_length);

#endif /* BINDINGS_H */
