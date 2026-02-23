package s21.peanutsh.TicTacToe.domain.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends GenericFilter {
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;


    //ServletRequest - запрос, который приходит на фильтр.
    //ServletResponse - ответ, в котором мы можем отправить результат прохождения фильтра
    // FilterChain - цепочка фильтров
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth/login") || requestURI.startsWith("/auth/signup")||requestURI.startsWith("/auth/update/access")) {
            filterChain.doFilter(request, response);// отправляем запрос следующему фильтру
            return;

        } else {
            try {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null) {
                    throw new SecurityException("Missing Authorization header");
                }
                var token = authHeader.substring(7);
                if (jwtProvider.validateAccessToken(token)) {
                    Claims claims = jwtProvider.getAccessClaims(token);
                    var auth = jwtUtil.generate(claims);
                    auth.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    filterChain.doFilter(request,response);
                }else {
                    throw new SecurityException("Missing Authorization header");
                }

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }


    }
}


