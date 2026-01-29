package s21.peanutsh.TicTacToe.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import s21.peanutsh.TicTacToe.datasource.model.EntityPerson;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepositoryPerson extends CrudRepository<EntityPerson,Long> {

    Optional<EntityPerson> findByLogin(String login);
    boolean existsByLogin(String login);
    Optional<EntityPerson> findByUuid(UUID uuid);
}
