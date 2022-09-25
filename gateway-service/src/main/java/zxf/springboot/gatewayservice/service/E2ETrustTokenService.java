package zxf.springboot.gatewayservice.service;

import org.springframework.stereotype.Service;

@Service
public class E2ETrustTokenService {
    public String getE2ETrustToken(String accessToken) {
        return "E2E-" + accessToken;
    }
}
