package com.leite.tester.security;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final HandlerExceptionResolver resolver;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        if(token != null){
            try {
                boolean tokenValido = jwtTokenProvider.validadeToken(token);
                if(tokenValido){
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    if(authentication != null){
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                filterChain.doFilter(servletRequest, servletResponse);
            }catch (Exception e){
                HttpServletRequest httpServletRequest = null;
                HttpServletResponse httpServletResponse = null;
                httpServletRequest = (HttpServletRequest) servletRequest;
                if(servletResponse instanceof HttpServletResponse){
                    httpServletResponse = (HttpServletResponse) servletResponse;
                }
                resolver.resolveException(httpServletRequest,httpServletResponse, null, e);
            }
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
