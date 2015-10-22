package com.callsintegration.dto.api.ErrorHandlers;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by berz on 22.10.2015.
 */
public abstract class APIRequestErrorHandler implements ResponseErrorHandler {
    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }
}
