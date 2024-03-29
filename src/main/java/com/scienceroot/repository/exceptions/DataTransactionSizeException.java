package com.scienceroot.repository.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DataTransactionSizeException
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Data too large.")
public class DataTransactionSizeException extends RuntimeException {

    
}