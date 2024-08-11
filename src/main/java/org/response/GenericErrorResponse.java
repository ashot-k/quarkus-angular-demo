package org.response;

import org.exceptions.ErrorTypes;


public record GenericErrorResponse (String error, int statusCode, ErrorTypes errorType){
}
