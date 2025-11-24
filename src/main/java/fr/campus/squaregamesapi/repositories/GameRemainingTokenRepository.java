package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.GameRemainingTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRemainingTokenRepository extends JpaRepository<GameRemainingTokenEntity, Long> {

    List<GameRemainingTokenEntity> findByGameId(String gameId);

}
