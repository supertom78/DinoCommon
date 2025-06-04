package fr.liksi.starters.headers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Objects;

public class HeadersClientInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(HeadersClientInterceptor.class);
    private final HeadersHolder headersHolder;

    public HeadersClientInterceptor(HeadersHolder headersHolder) {
        this.headersHolder = headersHolder;
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final var parcHeader = headersHolder.getParc();
        final var correlationId = headersHolder.getCorrelationId();
        final var authorization = headersHolder.getAuthorization();
        LOG.info("Forwarding headers : {} {} and authorization is {} null ", parcHeader, correlationId, Objects.isNull(authorization));

        request.getHeaders().addIfAbsent("parc", parcHeader.name());
        request.getHeaders().addIfAbsent("correlationId", correlationId.toString());
        request.getHeaders().addIfAbsent("Authorization", authorization);

        return execution.execute(request, body);
    }
}
