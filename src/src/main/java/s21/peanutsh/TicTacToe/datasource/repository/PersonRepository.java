package s21.peanutsh.TicTacToe.datasource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import s21.peanutsh.TicTacToe.datasource.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);

    Optional<UserEntity> findByUuid(UUID uuid);


    @Query("SELECT em.uuidGame FROM ModelEntity em ")
    List<UUID> getAllUuid();

}
