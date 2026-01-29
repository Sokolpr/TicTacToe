package s21.peanutsh.TicTacToe.domain.service;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import s21.peanutsh.TicTacToe.datasource.repository.RepositoryPerson;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

public class AuthFilter extends GenericFilter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());
    private final RepositoryPerson repositoryPerson;
    private final PasswordEncoder passwordEncoder;


    public AuthFilter(RepositoryPerson repositoryPerson, PasswordEncoder passwordEncoder) {
        this.repositoryPerson = repositoryPerson;
        this.passwordEncoder = passwordEncoder;
    }


    //ServletRequest - запрос, который приходит на фильтр.
    //ServletResponse - ответ, в котором мы можем отправить результат прохождения фильтра
    // FilterChain - цепочка фильтров
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth/login") || requestURI.startsWith("/auth/signup")) {
            filterChain.doFilter(request, response);// отправляем запрос следующему фильтру
            return;

        } else {
            try {

                String authHeader = request.getHeader("Authorization");
                if (authHeader == null) {
                    throw new SecurityException("Missing Authorization header");
                }
                //достаем из заголовка логи и пароль
                String[] auth = DecodeBase64.decodeBase64(authHeader);
                var entity = repositoryPerson.findByLogin(auth[0]); // находим по логину человека
                if (entity.isPresent()) {
                    if (passwordEncoder.matches(auth[1], entity.get().getPassword())) { //проверяем совпадают ли пароли
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken
                                        (entity.get().getUuid(), null, Collections.emptyList()); // сохраняем в токен
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                        return;

                    } else {
                        throw new SecurityException("Invalid password");
                    }
                } else {
                    throw new SecurityException("User not found");
                }

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }


    }
}


