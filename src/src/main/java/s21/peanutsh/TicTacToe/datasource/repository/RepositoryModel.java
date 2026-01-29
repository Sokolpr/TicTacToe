package s21.peanutsh.TicTacToe.datasource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import s21.peanutsh.TicTacToe.datasource.model.EntityModel;
import s21.peanutsh.TicTacToe.domain.model.StateGame;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepositoryModel extends CrudRepository<EntityModel, Long> {


    List<EntityModel> findAllByStateGameAndFirstPlayerIsNotAndSecondPlayerIsNot(StateGame stateGame, String firstPlayer, String secondPlayer);



    Optional<EntityModel> findByUuidGameAndStateGame(UUID uuid,StateGame stateGame);

    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.uuidGame = :uuidGame " +
            "AND em.gameOver = :gameOver " +
            "AND (em.firstPlayer = :user OR em.secondPlayer = :user)")
    Optional<EntityModel> findByUuidGameAndUuidUser(
            @Param("uuidGame") UUID uuidGame,
            @Param("gameOver") Boolean gameOver,
            @Param("user") String uuidUserString
    );

    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.gameOver = :gameOver " +
            "AND (em.firstPlayer = :user OR em.secondPlayer = :user)")
    List<EntityModel> findCurrentGamesForUser(
            @Param("gameOver") Boolean gameOver,
            @Param("user") String uuidUserString
    );

    Optional<EntityModel> findByUuidGame(UUID uuidGame);

}
