package com.todos.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Resource<T> {

  private final T content;
  private final String url;

  @Override
  public String toString() {
    return "Resource{" +
        "url='" + url + '\'' +
        ", content=" + content +
        '}';
  }
}
