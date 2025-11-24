package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.jpa.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, String> { }
