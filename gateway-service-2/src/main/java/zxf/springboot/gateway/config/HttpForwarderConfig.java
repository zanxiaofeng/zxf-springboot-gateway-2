package zxf.springboot.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zxf.springboot.gateway.support.httpforward.HttpForwardUrlForward;
import zxf.springboot.gateway.support.httpforward.HttpForwarder;

import static zxf.springboot.gateway.support.httpforward.HttpForwardPostProcessor.defaultPostProcess;
import static zxf.springboot.gateway.support.httpforward.HttpForwardPreProcessor.*;

@Configuration
public class HttpForwarderConfig {
    private final HttpForwardUrlForward authServiceUrlForward = HttpForwardUrlForward.create(
            "http://localhost:8080", "/([\\w-./?%&=]*)", "/$1");

    @Bean(name = "authServiceForwarder")
    public HttpForwarder authServiceForwarder() {
        return new HttpForwarder(defaultPreProcess()
                .andThen(urlPreProcess(authServiceUrlForward)),
                defaultPostProcess());
    }
}
