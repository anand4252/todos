package com.todos.service;

import com.todos.model.TodoResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ValidatorService {

    public void validate(UUID id, TodoResource todoResource) {
        validate(id);
        validate(todoResource);
    }

    public void validate(TodoResource todoResource) {
        if (todoResource == null || StringUtils.isEmpty(todoResource.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Todo Resource");
        }
    }

    public void validate(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UUID should not be null");
        }
    }
}
