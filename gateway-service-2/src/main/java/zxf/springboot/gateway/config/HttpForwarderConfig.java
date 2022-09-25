package zxf.springboot.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zxf.springboot.gateway.security.E2ETrustTokenGenerator;
import zxf.springboot.gateway.support.httpforward.HttpForwardUrlForward;
import zxf.springboot.gateway.support.httpforward.HttpForwarder;

import static zxf.springboot.gateway.support.httpforward.HttpForwardPostProcessor.noPostProcess;
import static zxf.springboot.gateway.support.httpforward.HttpForwardPreProcessor.*;

@Configuration
public class HttpForwarderConfig {
    private final HttpForwardUrlForward authServiceUrlForward = HttpForwardUrlForward.create(
            "http://localhost:8080", "/([\\w-./?%&=]*)", "/$1");


    @Bean(name = "authServiceForwarder")
    public HttpForwarder authServiceForwarder(@Autowired E2ETrustTokenGenerator e2ETrustTokenGenerator) {
        return new HttpForwarder(urlPreProcess(authServiceUrlForward).andThen(e2ETrustTokenGenerator),
                noPostProcess());
    }
}
