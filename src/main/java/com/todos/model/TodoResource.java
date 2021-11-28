package com.todos.model;

import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
public class TodoResource {

  private UUID id;
  private final String title;
  private final boolean completed;
  private final Long order;

  @Override
  public String toString() {
    return "Todo{" +
        "title='" + title + '\'' +
        ", completed=" + completed +
        ", order=" + order +
        '}';
  }

}
