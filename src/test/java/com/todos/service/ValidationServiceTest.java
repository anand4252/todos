package com.todos.service;

import com.todos.model.TodoResource;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationServiceTest {
    private final EasyRandom generator = new EasyRandom();
    private final ValidatorService validatorService = new ValidatorService();

    @NullSource
    @ParameterizedTest
    void testValidateIdFailure(UUID input) {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                validatorService.validate(input)
        );

        "UUID should not be null".equals(exception.getMessage());
        Assertions.assertThat(exception.getStatus().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testValidateIdSuccess() {
        assertDoesNotThrow(() -> validatorService.validate(UUID.randomUUID()));
    }

    @NullSource
    @ParameterizedTest
    @MethodSource
    void testValidateTodoResourceFailure(TodoResource input) {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            validatorService.validate(input);
        });

        "Invalid Todo Resource".equals(exception.getMessage());
        Assertions.assertThat(exception.getStatus().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testValidateTodoResourceSuccess() {
        final TodoResource todoResource = generator.nextObject(TodoResource.class);
        assertDoesNotThrow(() -> validatorService.validate(todoResource));
    }

    @Test
    void testValidate() {
        final TodoResource todoResource = generator.nextObject(TodoResource.class);
        assertDoesNotThrow(() -> validatorService.validate(UUID.randomUUID(), todoResource));
    }



    private static Stream<Arguments> testValidateTodoResourceFailure() {
        return Stream.of(
                Arguments.of(new TodoResource(null, false, 2)),
                Arguments.of(new TodoResource( "", false, 2)),
                Arguments.of(new TodoResource("", false, null))
        );
    }
}
