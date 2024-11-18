package yurilenzi.AppTurni.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yurilenzi.AppTurni.payloads.ErrorResponseDTO;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(SameEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleBadRequest(SameEmailException ex){
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
}
