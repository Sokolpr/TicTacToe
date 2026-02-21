package s21.peanutsh.TicTacToe.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import s21.peanutsh.TicTacToe.datasource.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);

    Optional<UserEntity> findByUuid(UUID uuid);
}
