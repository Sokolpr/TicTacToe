package s21.peanutsh.TicTacToe.domain.service;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import s21.peanutsh.TicTacToe.domain.model.User;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.UUID;


public interface UserService {


     void signUp(SignUpRequest signUpRequest) throws AuthorizationDeniedException;

     User loadUserByUsername(String login) throws Exception ;

     User getByUuid(UUID uuidUser) throws UsernameNotFoundException;



}
