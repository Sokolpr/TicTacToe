package s21.peanutsh.TicTacToe.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import s21.peanutsh.TicTacToe.datasource.model.TokenEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByUuid(UUID uuid);
}
