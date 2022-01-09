package fr.b4.apps.configuration;


import fr.b4.apps.common.exceptions.*;
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
            if (exception.getCause() instanceof BadRequestException) {
                setResponse(httpResponse, exception.getCause().getMessage(), 400);
            }

            if (exception.getCause() instanceof IllegalArgumentException) {
                setResponse(httpResponse, exception.getCause().getMessage(), 400);
            }

            if (exception.getCause() instanceof ResourceNotFoundException) {
                setResponse(httpResponse, exception.getCause().getMessage(), 404);
            }

            if (exception.getCause() instanceof ThirdPartyException) {
                setResponse(httpResponse, exception.getCause().getMessage(), 502);
            }

            if (exception.getCause() instanceof ResourceUpdateFailedException) {
                setResponse(httpResponse, exception.getCause().getMessage(), 501);
            }

            if (exception.getCause() instanceof ForbiddenException) {
                setResponse(httpResponse, exception.getCause().getMessage(), 401);
            }
        }
    }

    private void setResponse(HttpServletResponse httpResponse, String message, int code) throws IOException {
        httpResponse.setStatus(code);
        httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpResponse.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}
