package com.plq.grammarly.filter;

import com.google.common.collect.Maps;
import com.plq.grammarly.service.impl.UserServiceImpl;
import com.plq.grammarly.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (isProtectedUrl(request)) {
            String jwt = null;
            String username = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Map<String, Cookie> cookieMap = Maps.newHashMapWithExpectedSize(cookies.length);
                for (Cookie cookie : cookies) {
                    cookieMap.put(cookie.getName(), cookie);
                }
                if (cookieMap.containsKey("gp-token")) {
                    jwt = cookieMap.get("gp-token").getValue();
                }
                if (cookieMap.containsKey("gp-username")) {
                    username = cookieMap.get("gp-username").getValue();
                }
            }
//        }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }
        }
        // Pass request down the chain, except for OPTIONS
        if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
        }

    }

    /**
     * 是否需要jwt认证的url
     *
     * @param request
     * @return
     */
    private boolean isProtectedUrl(HttpServletRequest request) {
        String urlPath = request.getServletPath();
        String[] ignorePaths = new String[]{"/login", "/api/v1/login", "/webjars/**", "/exchangeCode/exchange", "/index", "/activate", "/confirm","/"};
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : ignorePaths) {
            if (antPathMatcher.match(pattern, urlPath)) {
                return false;
            }
        }
        return true;
    }
}