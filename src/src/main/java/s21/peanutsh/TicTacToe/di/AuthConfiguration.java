package s21.peanutsh.TicTacToe.di;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import s21.peanutsh.TicTacToe.datasource.repository.PersonRepository;
import s21.peanutsh.TicTacToe.datasource.repository.TokenRepository;
import s21.peanutsh.TicTacToe.domain.service.*;
import s21.peanutsh.TicTacToe.domain.service.impl.AuthServiceImpl;
import s21.peanutsh.TicTacToe.domain.service.impl.JwtProviderImpl;
import s21.peanutsh.TicTacToe.domain.service.impl.JwtUtilImpl;
import s21.peanutsh.TicTacToe.domain.service.impl.UserServiceImpl;
import s21.peanutsh.TicTacToe.web.controller.AuthController;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthFilter authFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/auth/update/access").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        ;

        return http.build();
    }

    @Bean
    public AuthController authController(AuthService authService) {
        return new AuthController(authService);
    }

    @Bean
    public AuthService authService(JwtProvider jwtProvider,
                                   UserService userService,
                                   TokenRepository tokenRepository,
                                   PasswordEncoder passwordEncoder) {
        return new AuthServiceImpl(jwtProvider, userService, tokenRepository, passwordEncoder);
    }

    @Bean
    public JwtProvider jwtProvider(@Value("${secretAccess}") String accessSecret,
                                   @Value("${lifeTimeAccess}") Integer lifeTimeAccess,
                                   @Value("${secretRefresh}") String refreshSecret,
                                   @Value("${lifeTimeRefresh}") Integer lifeTimeRefresh) {
        return new JwtProviderImpl(accessSecret,
                lifeTimeAccess,
                refreshSecret,
                lifeTimeRefresh);
    }

    @Bean
    public UserService userService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        return new UserServiceImpl(passwordEncoder, personRepository);
    }

    @Bean
    public AuthFilter authFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
        return new AuthFilter(jwtProvider, jwtUtil);
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtilImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
