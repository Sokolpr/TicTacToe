package s21.peanutsh.TicTacToe.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import s21.peanutsh.TicTacToe.datasource.model.EntityGame;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface RepositoryGame extends CrudRepository<EntityGame, UUID> {
    @Override
    Optional<EntityGame> findById(UUID uuid);

}
