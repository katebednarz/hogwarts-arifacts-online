package edu.tcu.cs.hogwartsartifactsonline.system.exception;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }

    @RestControllerAdvice
    public static class ExceptionHandlerDevice {

        @ExceptionHandler(ObjectNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        Result handleObjectNotFoundException(ObjectNotFoundException ex) {
            return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
        }



        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        Result handleValidationException(MethodArgumentNotValidException ex) {
            List<ObjectError> errors = ex.getBindingResult().getAllErrors();
            Map<String, String> map = new HashMap<>(errors.size());
            errors.forEach((error) -> {
                String key = ((FieldError) error).getField();
                String val = error.getDefaultMessage();
                map.put(key, val);
            });
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are invalid, see data for details.", map);
        }


    }
}
