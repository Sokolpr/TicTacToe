package s21.peanutsh.TicTacToe.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s21.peanutsh.TicTacToe.datasource.model.EntityPerson;
import s21.peanutsh.TicTacToe.datasource.repository.RepositoryPerson;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;
import s21.peanutsh.TicTacToe.web.model.WebPerson;

import java.util.Base64;
import java.util.UUID;

@Service
public class ServiceAuth {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RepositoryPerson repositoryPerson;

    public ServiceAuth(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException  {
        if (repositoryPerson.existsByLogin(signUpRequest.getLogin())) {
            throw new AuthorizationDeniedException("Login already exists");
        }
        //переделать через маппер
        repositoryPerson.save(new EntityPerson(signUpRequest.getLogin(), passwordEncoder.encode(signUpRequest.getPassword())));

    }


    //header имеет вид "Basic {закодированный пароль}"
    public UUID authorize(String header) throws Exception {
        String[] auth;
        try {
            auth = DecodeBase64.decodeBase64(header);

        } catch (Exception e) {
            throw new Exception("Invalid Authorization header");
        };
        var entity = repositoryPerson.findByLogin(auth[0]);
        if (entity.isPresent()) {
            if (passwordEncoder.matches(auth[1], entity.get().getPassword())) {
                return entity.get().getUuid();
            }
            throw new Exception("неверный пароль");
        }
        else {
            throw new Exception("Такого пользователя не существует");
        }
    }

    public WebPerson getUser(UUID uuidUser) throws UsernameNotFoundException {
        var user = repositoryPerson.findByUuid(uuidUser);
        if(user.isEmpty()){
            throw  new UsernameNotFoundException("Пользователь не существует");
        }
        return convertPersonToWeb(user.get());
    }



    private static WebPerson convertPersonToWeb(EntityPerson entityPerson){
        return WebPerson.builder()
                .login(entityPerson.getLogin())
                .uuid(entityPerson.getUuid())
                .build();
    }
}
