package zxf.springboot.gateway.support.httpforward;

public class HttpForwardException extends RuntimeException {
    public HttpForwardException(String message){
        super(message);
    }

    public HttpForwardException(String message, Throwable cause){
        super(message, cause);
    }
}
