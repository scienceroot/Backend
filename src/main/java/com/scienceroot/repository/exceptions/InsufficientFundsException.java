package com.scienceroot.repository.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InsufficientFundsException
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient funds for storing data.")
public class InsufficientFundsException extends RuntimeException {

    
}