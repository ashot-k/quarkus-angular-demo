package org.exceptions;


import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.response.GenericErrorResponse;

import static org.jboss.resteasy.reactive.RestResponse.Status;
import static org.jboss.resteasy.reactive.RestResponse.status;


public class CustomExceptionMapper {
    @ServerExceptionMapper
    public RestResponse<GenericErrorResponse> mapOptimisticLock(OptimisticLockException e) {
       // StaleObjectStateException staleException = (StaleObjectStateException) e.getCause();
        return status(Status.INTERNAL_SERVER_ERROR, new GenericErrorResponse("Entity has been edited during this request", Status.INTERNAL_SERVER_ERROR.getStatusCode(), ErrorTypes.LOCK_EXCEPTION));
    }
    @ServerExceptionMapper
    public RestResponse<GenericErrorResponse> mapEntityNotFound(EntityNotFoundException e) {
        return status(Status.NOT_FOUND, new GenericErrorResponse(e.getMessage(), Status.NOT_FOUND.getStatusCode(), ErrorTypes.NOT_FOUND));
    }

}
