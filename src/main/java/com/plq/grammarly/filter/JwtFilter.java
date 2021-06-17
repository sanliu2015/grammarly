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
        String jwt = null;
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Map<String,Cookie> cookieMap = Maps.newHashMapWithExpectedSize(cookies.length);
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

//        final String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtUtil.extractUsername(jwt);
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
        // Pass request down the chain, except for OPTIONS
        if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
        }

        //chain.doFilter(request, response);
    }
}