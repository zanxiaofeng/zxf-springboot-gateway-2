package zxf.springboot.gateway.support.httpforward;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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
