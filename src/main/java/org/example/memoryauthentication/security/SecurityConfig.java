package org.example.memoryauthentication.security;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.memoryauthentication.filter.JwtAuthenticationFilter;
import org.example.memoryauthentication.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    CustomUserDetailsService customUserDetailsService;

    JwtAuthenticationFilter authenticationFilter;


    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter authenticationFilter) {
        this.customUserDetailsService =  customUserDetailsService;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws  Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return  customUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return authenticationProvider;
    }


    // In Database Authentication
//    *********************************************************

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
////        UserDetails user = User.builder()
////                .username("user")
////                .password("{bcrypt}$2a$12$NYtvbx4z13dzFHU3NYqF/eb/8yXPpMj2rsp5h7t26oP6Q319fY0QS")
////                .roles("USER")
////                .build();
////        UserDetails admin = User.builder()
////                .username("admin")
////                .password("{bcrypt}$2a$12$NYtvbx4z13dzFHU3NYqF/eb/8yXPpMj2rsp5h7t26oP6Q319fY0QS")
////                .roles("USER", "ADMIN")
////                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
////        users.createUser(user);
////        users.createUser(admin);
//        return users;
//    }


//    In Memory Authentication
//    *********************************************************************
//    @Bean
//    public UserDetailsManager getUserDetailsManager() {
//        UserDetails user = User.builder()
//                .username("Kevin")
//                .password("$2a$12$saRpp3K0xYC3uFfaJ/TsaeXI0YC3lbezIl2L3wUUYkZEacEJCIhNm")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configureSecurity(HttpSecurity web) throws Exception {
        web.authorizeHttpRequests(
                cfg -> cfg.requestMatchers(HttpMethod.GET, "/hello")
                        .hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                        .requestMatchers("/api/auth/login")
                        .permitAll()
        );
        web.httpBasic(Customizer.withDefaults());

        web.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        web.csrf(csrf->csrf.disable());

        return web.build();
    }
}
