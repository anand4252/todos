package com.todos.mapper;

import com.todos.entity.Todo;
import com.todos.model.TodoResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TodoMapper {
    TodoMapper INSTANCE = Mappers.getMapper( TodoMapper.class );

    Todo TodoResourceToTodo(TodoResource todoResource);

    TodoResource TodoToTodoResource(Todo todo);
}
