package com.todos.service;

import com.todos.model.TodoResource;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TodoService {

    TodoResource addTodo(TodoResource todoResource);

    Optional<TodoResource> getTodo(UUID id);

    Collection<TodoResource> getAllTodos();

    void deleteAll();

    void deleteTodo(UUID id);

    Optional<TodoResource> update(UUID id, TodoResource updatedTodoResource);
}
