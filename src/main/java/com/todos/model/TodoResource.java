package com.todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class TodoResource {

    private UUID id;
    private final String title;
    private final boolean completed;
    private final Integer order;

    public TodoResource(@JsonProperty("title") String title,
                        @JsonProperty("completed") boolean completed,
                        @JsonProperty("order") Integer order) {
        this.title = title;
        this.completed = completed;
        this.order = order;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "title='" + title + '\'' +
                ", completed=" + completed +
                ", order=" + order +
                '}';
    }

}
