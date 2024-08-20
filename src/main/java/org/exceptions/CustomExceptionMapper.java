package org.exceptions;


import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.response.GenericErrorResponse;
import org.response.ValidationErrorResponse;

import java.util.ArrayList;
import java.util.List;

import static org.jboss.resteasy.reactive.RestResponse.Status;
import static org.jboss.resteasy.reactive.RestResponse.status;


public class CustomExceptionMapper {
    @ServerExceptionMapper
    public RestResponse<GenericErrorResponse> mapOptimisticLock(OptimisticLockException e) {
        Status status = Status.INTERNAL_SERVER_ERROR;
        return status(status, new GenericErrorResponse("Entity has been edited during this request", status.getStatusCode(), ErrorTypes.LOCK_EXCEPTION));
    }

    @ServerExceptionMapper
    public RestResponse<GenericErrorResponse> mapEntityNotFound(EntityNotFoundException e) {
        Status status = Status.NOT_FOUND;
        return status(status, new GenericErrorResponse(e.getMessage(), status.getStatusCode(), ErrorTypes.NOT_FOUND));
    }

    @ServerExceptionMapper
    public RestResponse<GenericErrorResponse> mapNumberFormat(NumberFormatException e) {
        Status status = Status.BAD_REQUEST;
        return status(status, new GenericErrorResponse(e.getMessage(), status.getStatusCode(), ErrorTypes.INVALID_PARAMS));
    }

    @ServerExceptionMapper
    public RestResponse<ValidationErrorResponse> mapConstraintViolation(ConstraintViolationException e) {
        Status status = Status.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        String[] errorsStr = errors.toArray(new String[0]);
        return status(status, new ValidationErrorResponse(errorsStr, status.getStatusCode(), ErrorTypes.INVALID_DATA_PROVIDED));
    }


}
