package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, String> { }
