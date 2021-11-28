package com.todos.controller;

import com.todos.model.Resource;
import com.todos.model.TodoResource;
import com.todos.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(
        methods = {POST, GET, OPTIONS, DELETE, PATCH},
        maxAge = 3600,
        allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
        origins = "*"
)
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<Resource<TodoResource>> add(@RequestBody TodoResource todoResource) {
        final TodoResource todoResourceAdded = todoService.addTodo(todoResource);

        log.debug("Todo added. " + todoResourceAdded);
        final Resource<TodoResource> resource = new Resource<>(todoResourceAdded, getHref(todoResourceAdded.getId()));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Resource<TodoResource>>> listAll() {

        final List<Resource<TodoResource>> todos = todoService.getAllTodos().stream()
                .map(todo -> new Resource<>(todo, getHref(todo.getId())))
                .collect(Collectors.toList());

        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping(value = "/{todo-id}")
    public HttpEntity<Resource<TodoResource>> get(@PathVariable("todo-id") UUID id) {
        return todoService.getTodo(id)
                .map(todo -> new Resource<>(todo, getHref(todo.getId())))
                .map(resource -> new ResponseEntity<>(resource, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteAll() {
        todoService.deleteAll();
        log.debug("All Todos deleted.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{todo-id}")
    @SuppressWarnings("rawtypes")
    public ResponseEntity delete(@PathVariable("todo-id") UUID id) {
        todoService.deleteTodo(id);
        log.debug("Todo with id {} deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{todo-id}")
    public HttpEntity<Resource<TodoResource>> update(@PathVariable("todo-id") UUID id, @RequestBody TodoResource updatedTodoResource) {
        return todoService.update(id, updatedTodoResource)
                .map(todo -> new ResponseEntity<>(new Resource<>(todo, getHref(todo.getId())), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    private String getHref(UUID id) {
        return linkTo(methodOn(this.getClass()).get(id))
                .withSelfRel()
                .getHref();
    }
}
