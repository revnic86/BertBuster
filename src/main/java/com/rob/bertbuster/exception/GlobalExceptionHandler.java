package com.rob.bertbuster.exception;

import com.rob.bertbuster.domain.entity.dto.ErrorDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex){
        String errorMessage = ex.getBindingResult().getFieldErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("Validation Failed"); //use validation annotations first if not there use Validation Failed

        ErrorDto errorDto = new ErrorDto(errorMessage);


        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST); //return 400

    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorDto> handleMovieNotFound(MovieNotFoundException ex){

        ErrorDto errorDto = new ErrorDto(ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);

    }



}
