package fr.b4.apps.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpExceptionHandler implements Filter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } catch (NestedServletException exception) {

            String reason = exception.getCause().getClass().getSimpleName();

            switch (reason) {
                case "BadRequestException":
                case "IllegalArgumentException":
                    setResponse(httpResponse, exception.getCause().getMessage(), 400);
                    break;
                case "ResourceNotFoundException":
                    setResponse(httpResponse, exception.getCause().getMessage(), 404);
                    break;
                case "ThirdPartyException":
                    setResponse(httpResponse, exception.getCause().getMessage(), 502);
                    break;
                case "ResourceUpdateFailedException":
                case "ResourceNotSavedException":
                    setResponse(httpResponse, exception.getCause().getMessage(), 501);
                    break;
                case "ForbiddenException":
                    setResponse(httpResponse, exception.getCause().getMessage(), 401);
                    break;
                default:
                    exception.getCause().printStackTrace();
                    setResponse(httpResponse, exception.getCause().getMessage(), 500);
            }

        }
    }

    private void setResponse(HttpServletResponse httpResponse, String message, int code) throws IOException {
        httpResponse.setStatus(code);
        httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpResponse.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}
