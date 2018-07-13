package com.scienceroot.repository.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InsufficientFundsException
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Insufficient funds for storing data.")
public class InsufficientFundsException extends RuntimeException {

    
}