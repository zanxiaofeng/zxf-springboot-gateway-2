package zxf.springboot.gateway.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxf.springboot.gateway.support.httpforward.HttpForwarder;

@RestController
public class ForwardController {
    @Autowired
    @Qualifier("authServiceForwarder")
    HttpForwarder authServiceForwarder;

    @RequestMapping("/**")
    public ResponseEntity<byte[]> forwardAll(RequestEntity<byte[]> requestEntity) {
        return authServiceForwarder.forward(requestEntity);
    }
}
