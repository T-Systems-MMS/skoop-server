package io.knowledgeassets.myskills.server.exception.handler;

import io.knowledgeassets.myskills.server.exception.domain.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

interface IExceptionHandler {

    default ResponseEntity<Object> buildResponseEntity(Exception ex, ResponseError responseError) {
        if (ex.getCause() != null) {
            responseError.setDebugMessage(ex.getCause().getLocalizedMessage());
        }
        return new ResponseEntity<>(responseError, responseError.getStatus());
    }
} 
