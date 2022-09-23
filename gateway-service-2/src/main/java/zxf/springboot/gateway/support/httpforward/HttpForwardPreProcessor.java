package zxf.springboot.gateway.support.httpforward;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;

import java.util.Objects;
import java.util.function.UnaryOperator;

public interface HttpForwardPreProcessor extends UnaryOperator<RequestEntity<byte[]>> {
    default HttpForwardPreProcessor andThen(HttpForwardPreProcessor after) {
        Objects.requireNonNull(after);
        return (t) -> after.apply(this.apply(t));
    }

    static HttpForwardPreProcessor defaultPreProcess() {
        return clearHeadersPreProcess(HttpHeaders.COOKIE);
    }

    static HttpForwardPreProcessor urlPreProcess(HttpForwardUrlForward httpForwardUrlForward) {
        return originalRequest -> {
            return new RequestEntity<>(originalRequest.getBody(), originalRequest.getHeaders(),
                    originalRequest.getMethod(), httpForwardUrlForward.getForwardUrl(originalRequest.getUrl()));
        };
    }

    static HttpForwardPreProcessor extraHeaderPreProcess(String httpHeaderName, String httpHeaderValue) {
        return originalRequest -> {
            HttpHeaders newHttpHeaders = HttpHeaders.writableHttpHeaders(originalRequest.getHeaders());
            newHttpHeaders.remove(httpHeaderName);
            newHttpHeaders.add(httpHeaderName, httpHeaderValue);
            return new RequestEntity<>(originalRequest.getBody(), newHttpHeaders, originalRequest.getMethod(), originalRequest.getUrl());
        };
    }

    static HttpForwardPreProcessor clearHeadersPreProcess(String... httpHeaderNames) {
        return originalRequest -> {
            HttpHeaders newHttpHeaders = HttpHeaders.writableHttpHeaders(originalRequest.getHeaders());
            for (int i = 0; i < httpHeaderNames.length; i++) {
                newHttpHeaders.remove(httpHeaderNames[i]);
            }
            return new RequestEntity<>(originalRequest.getBody(), newHttpHeaders, originalRequest.getMethod(), originalRequest.getUrl());
        };
    }
}
