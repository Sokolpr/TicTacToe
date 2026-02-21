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
public interface ModelRepository extends CrudRepository<EntityModel, Long> {

    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.stateGame = :stateGame " +
            "AND (em.firstPlayer is NULL  OR em.firstPlayer <> :uuidUser) "+
            "AND (em.secondPlayer is NULL  OR em.secondPlayer <> :uuidUser)")
    List<EntityModel> findGames(
            @Param("stateGame") StateGame stateGame,
            @Param("uuidUser") UUID uuidUser
    );


    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.gameOver = true " +
            "AND (:uuidUser = em.firstPlayer " +
            "OR :uuidUser = em.secondPlayer)")
    List<EntityModel> findEndGameForUser(
            @Param("uuidUser") UUID uuidUser
    );


    Optional<EntityModel> findByUuidGameAndStateGame(UUID uuid, StateGame stateGame);

    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.uuidGame = :uuidGame " +
            "AND (em.firstPlayer = :user OR em.secondPlayer = :user)")
    Optional<EntityModel> findByUuidGameAndUuidUser(
            @Param("uuidGame") UUID uuidGame,
            @Param("user") UUID uuidUser
    );

    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.gameOver = :gameOver " +
            "AND (em.firstPlayer = :user OR em.secondPlayer = :user)")
    List<EntityModel> findCurrentGamesForUser(
            @Param("gameOver") Boolean gameOver,
            @Param("user") UUID uuidUser
    );

    Optional<EntityModel> findByUuidGame(UUID uuidGame);

    @Query("SELECT em FROM EntityModel em " +
            "WHERE em.gameOver = :gameOver " +
            "AND (em.firstPlayer = :user OR em.secondPlayer = :user)")
    List<EntityModel> byTambrama(

    );

}
