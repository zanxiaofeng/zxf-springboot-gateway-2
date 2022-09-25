package zxf.springboot.gateway.support.httpforward;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.UnaryOperator;

public interface HttpForwardPostProcessor extends UnaryOperator<ResponseEntity<byte[]>> {
    default HttpForwardPostProcessor andThen(HttpForwardPostProcessor after) {
        Objects.requireNonNull(after);
        return (t) -> after.apply(this.apply(t));
    }

    static HttpForwardPostProcessor noPostProcess() {
        return responseEntity -> responseEntity;
    }

    static HttpForwardPostProcessor defaultPostProcess() {
        return clearHeadersPostProcess(HttpHeaders.TRANSFER_ENCODING, HttpHeaders.CONTENT_LENGTH,
                HttpHeaders.SET_COOKIE, HttpHeaders.SET_COOKIE2);
    }

    static HttpForwardPostProcessor clearHeadersPostProcess(String... httpHeaderNames) {
        return originalResponse -> {
            HttpHeaders newHttpHeaders = HttpHeaders.writableHttpHeaders(originalResponse.getHeaders());
            for (int i = 0; i < httpHeaderNames.length; i++) {
                newHttpHeaders.remove(httpHeaderNames[i]);
            }
            return new ResponseEntity<>(originalResponse.getBody(), newHttpHeaders, originalResponse.getStatusCode());
        };
    }
}
