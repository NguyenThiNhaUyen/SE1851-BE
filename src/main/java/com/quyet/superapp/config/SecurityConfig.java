package com.quyet.superapp.config;

import com.quyet.superapp.config.jwt.JwtAuthenticationFilter;
import com.quyet.superapp.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // ✅ THÊM DÒNG NÀY
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // đã load user từ DB
        provider.setPasswordEncoder(passwordEncoder()); // dùng BCrypt để so sánh
        return provider;
    }

    // Nếu cần dùng AuthenticationManager ở chỗ khác (ví dụ login), bạn có thể expose nó
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(authenticationProvider());
        return builder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);

        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Đổi đúng port FE của bạn

        //configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ Gắn Provider để xử lý xác thực username/password
                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(MEMBER_ENDPOINTS).hasAnyRole("MEMBER", "ADMIN", "STAFF")
                        .requestMatchers(STAFF_ENDPOINTS).hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .addFilterAfter((request, response, chain) -> {
                    HttpServletRequest req = (HttpServletRequest) request;
                    System.out.println("🔑 Authorization header: " + req.getHeader("Authorization"));
                    System.out.println("➡️ URI: " + req.getRequestURI());
                    System.out.println("➡️ Method: " + req.getMethod());
                    chain.doFilter(request, response);
                }, JwtAuthenticationFilter.class);

        return http.build();
    }


    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/api/verify-otp",
            "/api/forgot",
            "/api/change-password",
            "/api/blog/**",
            "/api/public/**",
            
    };
    private static final String[] MEMBER_ENDPOINTS = {
            "/api/user/**",
            "/api/donation/register/**",
            "/api/profile",
            "/api/donation/**",
            "/api/profile/**",
            "/api/request/**",
            "/api/transfusion/history",
            "/api/blood/**",
            "/api/vnpay/**",
            "/api/slots"
    };
    private static final String[] STAFF_ENDPOINTS = {
            "/api/staff/**",
            "/api/staff/requests/**",
            "/api/blood-requests/**",
            "/api/donation/confirm",
            //"/api/separation/**",// ✅ Thêm dòng này
            "/api/urgent-requests/**",
            "/api/blood-inventory/**",
            "/api/blood/**",
            "/api/separation/logs/**",
            "/api/separation-orders/**",
            "/api/separation-details/**",
            "/api/separation-statistics/**"
    };
    private static final String[] ADMIN_ENDPOINTS = {
            "/api/admin",
            "/api/admin/**",
            "/api/dashboard",
            "/api/users/**",
            "/api/roles/**",
            "/api/notifications/**",
            "/api/blood-inventory/**",
            "/api/separation/**",
            "/api/blood/**",
            "/api/donation/**",
            "/api/transfusion/**",
            "/api/urgent-requests/**",
            "/api/donation/confirm"

    };
}
