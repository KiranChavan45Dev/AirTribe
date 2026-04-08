package com.taskmanager.modules.auth.service;

import com.taskmanager.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;

    // Convert raw string to secure SecretKey
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    // Generate token with roles
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract username
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check expiration
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // JWT Filter for Spring Security
    public OncePerRequestFilter getJwtAuthFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException {

                final String authHeader = request.getHeader(jwtConfig.getHeader());
                String username = null;
                String token = null;

                if (authHeader != null && authHeader.startsWith(jwtConfig.getTokenPrefix())) {
                    token = authHeader.substring(jwtConfig.getTokenPrefix().length());
                    try {
                        username = extractUsername(token);
                    } catch (ExpiredJwtException | IllegalArgumentException | MalformedJwtException e) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                        return;
                    }
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (validateToken(token, userDetails)) {

                        // Extract roles safely
                        List<String> roles = Jwts.parserBuilder()
                                .setSigningKey(getSigningKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody()
                                .get("roles", List.class);

                        Collection<GrantedAuthority> authorities = roles.stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

                filterChain.doFilter(request, response);
            }
        };
    }
}