package nl.rabobank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice(assignableTypes = PersonController.class)
public class ExceptionHandling {

    @SuppressWarnings("unused")
    @ExceptionHandler({EntityNotFoundException.class})
    ResponseEntity<Void> handleExceptions(EntityNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
