package com.todos.service.impl;

import com.todos.entity.Todo;
import com.todos.mapper.TodoMapper;
import com.todos.model.TodoResource;
import com.todos.repository.TodoRepository;
import com.todos.service.TodoService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TodoServiceImplTest {

    private final EasyRandom generator = new EasyRandom();
    private TodoService todoService;
    private TodoRepository todoRepository;

    @BeforeEach
    void setup() {
        this.todoRepository = mock(TodoRepository.class);
        this.todoService = new TodoServiceImpl(todoRepository);
    }

    @Test
    @DisplayName("Tests addTodo functionality")
    public void GIVEN_a_valid_todo_WHEN_addTodo_invoked_SHOULD_save_todo_and_return_a_valid_TodoResource() {
        //Given
        TodoResource todoResource = generator.nextObject(TodoResource.class);

        //Mock
        Todo mockTodo = TodoMapper.INSTANCE.TodoResourceToTodo(todoResource);
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);

        //When
        final TodoResource actualTodoResource = this.todoService.addTodo(todoResource);

        //Then
        assertAll(
                () -> assertNotNull(actualTodoResource),
                () -> assertThat(actualTodoResource).isEqualTo(todoResource)
        );
    }

    @Test
    @DisplayName("Tests getTodo by Id functionality")
    public void GIVEN_a_valid_id_WHEN_getTodo_invoked_SHOULD_return_a_valid_TodoResource() {
        //Given
        TodoResource todoResource = generator.nextObject(TodoResource.class);

        //Mock
        Todo todo = TodoMapper.INSTANCE.TodoResourceToTodo(todoResource);
        when(todoRepository.findById(any())).thenReturn(Optional.of(todo));

        //When
        final Optional<TodoResource> actualTodoResource = this.todoService.getTodo(todoResource.getId());

        //Then
        assertAll(
                () -> assertTrue(actualTodoResource.isPresent()),
                () -> assertThat(actualTodoResource.get()).isEqualTo(todoResource)
        );
    }

    @Test
    @DisplayName("Tests get all Todos functionality")
    public void WHEN_getTodos_invoked_SHOULD_return_a_valid_TodoResources() {
        //Given
        final List<Todo> todos = generator.objects(Todo.class, 5)
                .collect(Collectors.toList());
        final List<TodoResource> expectedTodoResources = todos.stream()
                .map(TodoMapper.INSTANCE::TodoToTodoResource)
                .collect(Collectors.toList());
        //Mock
        when(todoRepository.findAll()).thenReturn(todos);

        //When
        final Collection<TodoResource> actTodoResources = this.todoService.getAllTodos();

        //Then
        assertAll(
                () -> assertFalse(CollectionUtils.isEmpty(actTodoResources)),
                () -> assertThat(actTodoResources).doesNotContainNull(),
                () -> assertThat(actTodoResources).containsSequence(expectedTodoResources)
        );
    }

    @DisplayName("Tests update Todo functionality")
    @ParameterizedTest
    @MethodSource
    public void GIVEN_updated_todo_WHEN_update_invoked_SHOULD_return_an_updated_TodoResources(TodoResource newTodoResource) {
        //Given
        TodoResource oldTodoResource = generator.nextObject(TodoResource.class);
        newTodoResource.setId(oldTodoResource.getId());

        //Mock
        Todo mockOldTodo = TodoMapper.INSTANCE.TodoResourceToTodo(oldTodoResource);
        when(todoRepository.findById(oldTodoResource.getId())).thenReturn(Optional.of(mockOldTodo));

        Todo updatedTodo = TodoMapper.INSTANCE.TodoResourceToTodo(newTodoResource);
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);


        //When
        Optional<TodoResource> actTodoResource = this.todoService.update(oldTodoResource.getId(), newTodoResource);

        //Then
        assertAll(
                () -> assertTrue(actTodoResource.isPresent()),
                () -> assertThat(actTodoResource.get()).isEqualTo(newTodoResource)
        );
    }


    /**
     * This method Provides values for the Parameterized test case
     *
     * @return TodoResources to cover multiple scenarios
     */
    private static Stream<TodoResource> GIVEN_updated_todo_WHEN_update_invoked_SHOULD_return_an_updated_TodoResources() {
        return Stream.of(new TodoResource("test-title", false, 10),
                new TodoResource("test-title", false, null),
                new TodoResource("", false, null)
        );
    }

}
