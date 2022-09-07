package zxf.springboot.gatewayservice.service;

import org.springframework.stereotype.Service;

@Service
public class E2ETokenService {
    public String getE2EToken(String accessToken) {
        return "E2E-" + accessToken;
    }
}
