package zxf.springboot.gateway.support.httpforward;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpForwardUrlForward {
    private final URI baseUrl;
    private final Pattern pathMatch;
    private final String pathReplace;

    private HttpForwardUrlForward(URI baseUrl, Pattern pathMatch, String pathReplace) {
        this.baseUrl = baseUrl;
        this.pathMatch = pathMatch;
        this.pathReplace = pathReplace;
    }

    public static HttpForwardUrlForward create(String baseUrl, String pathMatch, String pathReplace) {
        return new HttpForwardUrlForward(URI.create(baseUrl), Pattern.compile(pathMatch), pathReplace);
    }

    public URI getForwardUrl(URI originalUrl) {
        try {
            Matcher pathMatcher = pathMatch.matcher(originalUrl.getPath());
            if (!pathMatcher.matches()) {
                throw new HttpForwardException("Exception on getForwardUrl: The request path does not match: " + originalUrl.getPath());
            }
            String newPath = pathMatcher.replaceAll(pathReplace);
            return new URI(baseUrl.getScheme(), baseUrl.getUserInfo(), baseUrl.getHost(), baseUrl.getPort(), baseUrl.getPath() + newPath, originalUrl.getQuery(), originalUrl.getFragment());
        } catch (URISyntaxException ex) {
            throw new HttpForwardException("Exception on getForwardUrl: Failed to build new url", ex);
        }
    }
}
