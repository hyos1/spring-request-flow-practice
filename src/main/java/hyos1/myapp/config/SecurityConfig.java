package hyos1.myapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 활성화
@EnableMethodSecurity(
        securedEnabled = true)  // @Secured 활성화
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class)// JwtAuthenticationFilter를 스프링 시큐리티 인증 프로세스 전에 진행

                // JWT 사용시 불필요한 기능들 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)

                // 토큰이 필요한 기능에 토큰 없이 요청시 응답 메세지 설정
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(request -> request.getRequestURI().startsWith("/auth")).permitAll()
                                .requestMatchers("/auth/refresh").authenticated() // 토큰 재발급은 JWT 필요 (추가 예정)
                                .requestMatchers("/user_coupons", "/user_coupons/**").authenticated()
//                        .requestMatchers("/orders","/orders/**").hasAuthority(UserRole.Authority.ADMIN)
//                        .requestMatchers("/test").hasAuthority(UserRole.Authority.ADMIN)
//                        .requestMatchers("/open").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
