package com.todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Resource<T> {

  private final T content;
  private final String url;

  public Resource(@JsonProperty("content")T content, @JsonProperty("url")String url) {
    this.content = content;
    this.url = url;
  }

  @Override
  public String toString() {
    return "Resource{" +
            "url='" + url + '\'' +
            ", content=" + content +
            '}';
  }
}
