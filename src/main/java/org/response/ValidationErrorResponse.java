package org.response;

import org.exceptions.ErrorTypes;

public record ValidationErrorResponse(String[] errors, int status, ErrorTypes errorType) {

}
