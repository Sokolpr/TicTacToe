package s21.peanutsh.TicTacToe.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s21.peanutsh.TicTacToe.datasource.mapper.UserEntityMapper;
import s21.peanutsh.TicTacToe.datasource.repository.PersonRepository;
import s21.peanutsh.TicTacToe.domain.model.Role;
import s21.peanutsh.TicTacToe.domain.model.User;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.Set;
import java.util.UUID;


public interface UserService {


    public void signUp(SignUpRequest signUpRequest) throws AuthorizationDeniedException;

    public User loadUserByUsername(String login) throws Exception ;

    public User getByUuid(UUID uuidUser) throws UsernameNotFoundException;



}
