package com.todos.service.impl;

import com.todos.entity.Todo;
import com.todos.mapper.TodoMapper;
import com.todos.model.TodoResource;
import com.todos.repository.TodoRepository;
import com.todos.service.TodoService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class holds all the business logic.
 */
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public TodoResource addTodo(TodoResource todoResource) {
        val todo = TodoMapper.INSTANCE.TodoResourceToTodo(todoResource);
        todo.setUpdatedAt(String.valueOf(Instant.now().toEpochMilli()));

        val savedTodo = todoRepository.save(todo);

        todoResource.setId(savedTodo.getId());
        return todoResource;
    }

    @Override
    public Optional<TodoResource> getTodo(UUID id) {
        return todoRepository.findById(id)
                .map(TodoMapper.INSTANCE::TodoToTodoResource);
    }

    @Override
    public Collection<TodoResource> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoMapper.INSTANCE::TodoToTodoResource)
                .collect(Collectors.toList());
    }

    @Override
    @Generated
    public void deleteAll() {
        todoRepository.deleteAll();
    }

    @Override
    @Generated
    public void deleteTodo(UUID id) {
        todoRepository.deleteById(id);
    }

    @Override
    public Optional<TodoResource> update(UUID id, TodoResource todoResource) {
        return todoRepository.findById(id)
                .map(todo -> merge(todoResource, todo))
                .map(todoRepository::save)
                .map(TodoMapper.INSTANCE::TodoToTodoResource);
    }

    private Todo merge(TodoResource todoResource, Todo todo) {
        val title = todoResource.getTitle();
        if(!StringUtils.isEmpty(title)){
            todo.setTitle(title);
        }
        todo.setCompleted(todoResource.isCompleted());
        val order = todoResource.getOrder();
        if(order != null){
            todo.setOrder(order);
        }
        todo.setUpdatedAt(String.valueOf(Instant.now().toEpochMilli()));

        return todo;
    }
}
