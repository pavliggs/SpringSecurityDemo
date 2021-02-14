package com.khovaylo.springsecuritydemo.security.jwt;

import com.khovaylo.springsecuritydemo.exception.JwtAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Pavel Khovaylo
 */
@Component
public class JwtFilter extends GenericFilter {
    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                if (authentication != null)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(ex.getHttpStatus().value());
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        chain.doFilter(request, response);
    }
}