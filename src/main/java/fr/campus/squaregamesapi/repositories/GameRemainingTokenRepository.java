package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.jpa.GameRemainingTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRemainingTokenRepository extends JpaRepository<GameRemainingTokenEntity, Long> { }