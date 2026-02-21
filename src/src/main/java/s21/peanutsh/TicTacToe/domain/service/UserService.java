package s21.peanutsh.TicTacToe.domain.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s21.peanutsh.TicTacToe.datasource.mapper.UserMapper;
import s21.peanutsh.TicTacToe.datasource.repository.PersonRepository;
import s21.peanutsh.TicTacToe.domain.model.Role;
import s21.peanutsh.TicTacToe.domain.model.User;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;


    public void signUp(SignUpRequest signUpRequest) throws AuthorizationDeniedException {
        if (personRepository.existsByLogin(signUpRequest.getLogin())) {
            throw new AuthorizationDeniedException("Login already exists");
        }
        personRepository.save(UserMapper.userToEntity(User.builder()
                .login(signUpRequest.getLogin())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Set.of(Role.USER))
                .build()));

    }

    public User loadUserByUsername(String login) throws Exception {
        return UserMapper.entityToUser(
                personRepository.findByLogin(login)
                        .orElseThrow(
                                () -> new Exception("Такого пользователя не существует")));
    }

    public User getByUuid(UUID uuidUser) throws UsernameNotFoundException {
        return UserMapper.entityToUser(
                personRepository.findByUuid(uuidUser).orElseThrow(() -> new UsernameNotFoundException("Пользователь не существует")));
    }



}
