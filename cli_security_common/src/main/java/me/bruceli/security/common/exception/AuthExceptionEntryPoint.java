package me.bruceli.security.common.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.bruceli.common.vo.R;
import me.bruceli.common.vo.RE;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws  ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), R.error(RE.AHTU_ERROR));
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}