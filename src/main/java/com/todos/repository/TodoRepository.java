package com.todos.repository;

import com.todos.entity.Todo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TodoRepository  extends CrudRepository<Todo, UUID> {

    List<Todo> findAll();

}

