package org.response;

import org.exceptions.ErrorTypes;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.HashMap;

public record ValidationErrorResponse(HashMap<String, String> errors, RestResponse.Status status, ErrorTypes errorType) {

}
