package com.scienceroot.config;

import com.scienceroot.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandlingController {

    private Logger LOG = Logger.getLogger(ExceptionHandlingController.class.getName());

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public @ResponseBody
    ExceptionJsonInfo handleUserNotFoundException(HttpServletRequest request, UserNotFoundException ex) {
        ExceptionJsonInfo response = new ExceptionJsonInfo();
        response.message = ex.getMessage();
        response.url = request.getRequestURL().toString();
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public @ResponseBody
    ExceptionJsonInfo handleIllegalStateException(HttpServletRequest request, IllegalStateException ex) {
        ExceptionJsonInfo response = new ExceptionJsonInfo();
        response.message = ex.getMessage();
        response.url = request.getRequestURL().toString();
        return response;
    }
}
