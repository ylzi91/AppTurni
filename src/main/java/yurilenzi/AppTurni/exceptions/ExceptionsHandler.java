package yurilenzi.AppTurni.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yurilenzi.AppTurni.payloads.ErrorResponseDTO;
import yurilenzi.AppTurni.payloads.GenericResponseDTO;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(SameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleBadRequest(SameException ex){
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleBadRequest(BadRequestException ex){
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleUnauthorizedRequest(UnauthorizedException ex){
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFoundRequest(NotFoundException ex){
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFoundRequest(DateTimeParseException ex){
        return new ErrorResponseDTO("La data non è nel formato corretto (yyyy-mm-dd)");
    }

    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleNotFoundRequest(ForbiddenRequestException ex){
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(EmptyArrayException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseDTO handleNoContentRequest(EmptyArrayException ex){
        return new GenericResponseDTO(ex.getMessage());
    }
}
