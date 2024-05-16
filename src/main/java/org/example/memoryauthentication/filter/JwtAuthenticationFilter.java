package org.example.memoryauthentication.filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.memoryauthentication.service.CustomUserDetailsService;
import org.example.memoryauthentication.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService  jwtService;
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;
        Authentication authentication = null;

//        System.out.println("Hereerererereer " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            jwtToken = authHeader.substring(7);
            username = jwtService.extractUsername(jwtToken);


        }

        authentication = SecurityContextHolder.getContext().getAuthentication();

//        System.out.println("Hereerererereer  11"+ authentication);


        if(username != null && authentication == null) {
//            System.out.println("Hereerererereer  22");

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(jwtService.validateToken(jwtToken, userDetails)) {
//                System.out.println("Hereerererereer 33");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
