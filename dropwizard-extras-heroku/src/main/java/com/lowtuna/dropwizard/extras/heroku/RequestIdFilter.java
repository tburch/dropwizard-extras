package com.lowtuna.dropwizard.extras.heroku;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class RequestIdFilter implements ContainerRequestFilter {
    private static final String HEROKU_REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_LOG_NAME = "requestId";

    private final String headerName;

    public RequestIdFilter() {
        this(HEROKU_REQUEST_ID_HEADER);
    }

    public RequestIdFilter(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String requestId = request.getHeaderValue(headerName);
        if (StringUtils.isNotEmpty(requestId)) {
            MDC.put(REQUEST_ID_LOG_NAME, requestId);
        }
        return request;
    }

}