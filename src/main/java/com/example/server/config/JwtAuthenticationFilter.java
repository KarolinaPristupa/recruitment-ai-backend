package com.example.server.config;

import com.example.server.model.User;
import com.example.server.service.UserService;
import com.example.server.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("[JWT FILTER] Incoming request path: " + path);
        System.out.println("[JWT FILTER] HTTP method: " + request.getMethod());

        // Пропускаем preflight и HH/mock пути
        if (request.getMethod().equalsIgnoreCase("OPTIONS") ||
                path.startsWith("/mock-hh")) {
            System.out.println("[JWT FILTER] Skipping filter for OPTIONS / HH / mock path");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        System.out.println("[JWT FILTER] Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            String roleFromToken = jwtUtil.extractRole(token);

            System.out.println("[JWT FILTER] Extracted username: " + username);
            System.out.println("[JWT FILTER] Extracted role: " + roleFromToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                boolean valid = jwtUtil.validateToken(token, username);
                System.out.println("[JWT FILTER] Token valid: " + valid);

                if (valid) {
                    var user = userService.findByEmail(username);
                    System.out.println("[JWT FILTER] User loaded: " + user.getEmail());

                    var authority = (GrantedAuthority) () -> roleFromToken;
                    var authToken = new UsernamePasswordAuthenticationToken(
                            user, null, List.of(authority)
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("[JWT FILTER] Authentication set in SecurityContext");
                }
            }
        } else {
            System.out.println("[JWT FILTER] No Bearer token found");
        }

        filterChain.doFilter(request, response);
    }

}
