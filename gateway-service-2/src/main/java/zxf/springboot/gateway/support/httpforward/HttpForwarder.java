package zxf.springboot.gateway.support.httpforward;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class HttpForwarder {
    private final HttpForwardPreProcessor httpForwardPreProcessor;
    private final HttpForwardPostProcessor httpForwardPostProcessor;

    public HttpForwarder(HttpForwardPreProcessor httpForwardPreProcessor, HttpForwardPostProcessor httpForwardPostProcessor) {
        this.httpForwardPreProcessor = httpForwardPreProcessor;
        this.httpForwardPostProcessor = httpForwardPostProcessor;
    }

    public ResponseEntity<byte[]> forward(RequestEntity<byte[]> requestEntity) {
        try {
            if (httpForwardPreProcessor != null) {
                requestEntity = httpForwardPreProcessor.apply(requestEntity);
            }
        } catch (HttpForwardException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new HttpForwardException("Exception on pre process", ex);
        }

        ResponseEntity<byte[]> responseEntity;
        try {
            responseEntity = createRestTemplate().exchange(requestEntity, byte[].class);
            log.info("Request: {} {}", requestEntity.getMethod(), requestEntity.getUrl());
            log.info("{}", requestEntity.getHeaders());
            if (!Objects.isNull(requestEntity.getBody())) {
                log.info("{}", new String(requestEntity.getBody()));
            }
            log.info("Response: {}", responseEntity.getStatusCode());
            log.info("{}", responseEntity.getHeaders());
            if (!Objects.isNull(responseEntity.getBody())) {
                log.info("{}", new String(responseEntity.getBody()));
            }
        } catch (Exception ex) {
            throw new HttpForwardException("Exception on forward process", ex);
        }

        try {
            if (httpForwardPostProcessor != null) {
                responseEntity = httpForwardPostProcessor.apply(responseEntity);
            }
        } catch (HttpForwardException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new HttpForwardException("Exception on post process", ex);
        }

        return responseEntity;
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
        return restTemplate;
    }
}
