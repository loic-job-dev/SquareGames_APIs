package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.jpa.GameTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameTokenRepository extends JpaRepository<GameTokenEntity, Long> { }