package s21.peanutsh.TicTacToe.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import s21.peanutsh.TicTacToe.datasource.mapper.UserEntityMapper;
import s21.peanutsh.TicTacToe.datasource.repository.PersonRepository;
import s21.peanutsh.TicTacToe.domain.model.Role;
import s21.peanutsh.TicTacToe.domain.model.User;
import s21.peanutsh.TicTacToe.domain.service.UserService;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) throws AuthorizationDeniedException {
        if (personRepository.existsByLogin(signUpRequest.getLogin())) {
            throw new AuthorizationDeniedException("Login already exists");
        }
        personRepository.save(UserEntityMapper.userToEntity(User.builder()
                .login(signUpRequest.getLogin())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Set.of(Role.USER))
                .uuid(UUID.randomUUID())
                .build()));

    }

    @Override
    public User loadUserByUsername(String login) throws Exception {
        return UserEntityMapper.entityToUser(
                personRepository.findByLogin(login)
                        .orElseThrow(
                                () -> new Exception("Такого пользователя не существует")));
    }

    @Override
    public User getByUuid(UUID uuidUser) throws UsernameNotFoundException {
        return UserEntityMapper.entityToUser(
                personRepository.findByUuid(uuidUser).orElseThrow(() -> new UsernameNotFoundException("Пользователь не существует")));
    }

    @Override
    public List<UUID> getAllPlayers() {
        return personRepository.getAllUuid();
    }
}
