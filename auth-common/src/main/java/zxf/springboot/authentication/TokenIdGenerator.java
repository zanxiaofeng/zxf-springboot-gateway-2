package zxf.springboot.authentication;

import org.apache.catalina.SessionIdGenerator;
import org.apache.catalina.util.StandardSessionIdGenerator;

public class TokenIdGenerator {
    public static String generateTokenId() {
        SessionIdGenerator sessionIdGenerator = new StandardSessionIdGenerator();
        return sessionIdGenerator.generateSessionId();
    }
}
