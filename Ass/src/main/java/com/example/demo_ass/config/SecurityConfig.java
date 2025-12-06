package com.example.demo_ass.config;

import com.example.demo_ass.security.CustomUserDetailsService;
import com.example.demo_ass.security.JwtAuthenticationFilter;
import com.example.demo_ass.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetails;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource; // Inject CORS bean

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Tắt CSRF cho API
        http.csrf(csrf -> csrf.disable());
        // Bật CORS với config từ bean
        http.cors(cors -> cors.configurationSource(corsConfigurationSource));

        // Stateless session cho API
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Cấu hình quyền truy cập
        http.authorizeHttpRequests(auth -> auth
                // Cho phép public login & register
                .requestMatchers("/api/auth/**").permitAll()
                // API admin
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Tất cả API khác cần authentication
                .requestMatchers("/api/**").authenticated()
                // Trang web khác (MVC) cho phép public
                .anyRequest().permitAll()
        );

        // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

//        // Nếu muốn dùng formLogin cho MVC (web), tách URL khác
//        http.formLogin(form -> form
//                .loginPage("/login")
//                .loginProcessingUrl("/login/check")
//                .defaultSuccessUrl("/", true)
//                .failureUrl("/login?error=true")
//                .usernameParameter("username")
//                .passwordParameter("password")
//        );
//
//        // Logout MVC
//        http.logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout=true")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//        );

        // Sử dụng CustomUserDetailsService
        http.userDetailsService(userDetails);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}