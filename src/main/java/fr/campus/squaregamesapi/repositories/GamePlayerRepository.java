package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.jpa.GamePlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, Long> { }