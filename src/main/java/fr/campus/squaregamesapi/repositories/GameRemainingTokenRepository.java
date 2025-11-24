package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.GameRemainingTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRemainingTokenRepository extends CrudRepository<GameRemainingTokenEntity, Long> { }