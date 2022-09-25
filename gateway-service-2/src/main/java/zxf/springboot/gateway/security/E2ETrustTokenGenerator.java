package zxf.springboot.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import zxf.springboot.gateway.service.E2ETrustTokenService;
import zxf.springboot.gateway.support.httpforward.HttpForwardPreProcessor;

@Service
public class E2ETrustTokenGenerator implements HttpForwardPreProcessor {
    @Autowired
    private E2ETrustTokenService e2ETrustTokenService;

    @Override
    public RequestEntity<byte[]> apply(RequestEntity<byte[]> originalRequest) {
        HttpHeaders newHttpHeaders = HttpHeaders.writableHttpHeaders(originalRequest.getHeaders());
        newHttpHeaders.add("X-E2E-Trust-Token", e2ETrustTokenService.getE2ETrustToken(SecurityUtils.getCurrentAccessToken()));
        return new RequestEntity<>(originalRequest.getBody(), newHttpHeaders, originalRequest.getMethod(), originalRequest.getUrl());
    }
}
