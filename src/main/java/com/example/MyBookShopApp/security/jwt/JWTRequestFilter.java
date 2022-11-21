package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.security.BookstoreUserDetails;
import com.example.MyBookShopApp.service.BookstoreUserDetailsService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

  private final BookstoreUserDetailsService bookstoreUserDetailsService;
  private final JWTUtil jwtUtil;

  public JWTRequestFilter(
      BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil) {
    this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    String token = null;
    String username = null;
    Cookie[] cookies = httpServletRequest.getCookies();

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          token = cookie.getValue();
          username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          BookstoreUserDetails userDetails =
              (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(username);
          if (jwtUtil.validateToken(token, userDetails)) {
            ArrayList<String> roles = userDetails.getRoles();
            UsernamePasswordAuthenticationToken authenticationToken;
            if (roles.contains("ADMIN")) {
              authenticationToken =
                  new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthoritiesAdmin());
            } else {
              authenticationToken =
                  new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthorities());
            }
            authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
