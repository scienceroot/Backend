package com.scienceroot.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author svenseemann
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Action not allowed")
public class ActionForbiddenException extends RuntimeException {

}
