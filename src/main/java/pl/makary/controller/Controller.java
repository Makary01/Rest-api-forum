package pl.makary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.makary.model.ForbiddenResponse;
import pl.makary.model.NotFoundResponse;
import pl.makary.model.OkResponse;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class Controller {

    public ResponseEntity<?> generateResponseFromBindingResult(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    public ResponseEntity<OkResponse> generateOkResponse(String message){
        return ResponseEntity.ok(new OkResponse(message));
    }

    public ResponseEntity<NotFoundResponse> generateNotFoundResponse(String message){
        return new ResponseEntity<>(
                new NotFoundResponse(message),
                HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<ForbiddenResponse> generateForbiddenResponse(String message){
        return new ResponseEntity<>(
                new ForbiddenResponse(message),
                HttpStatus.FORBIDDEN
        );
    }
}
