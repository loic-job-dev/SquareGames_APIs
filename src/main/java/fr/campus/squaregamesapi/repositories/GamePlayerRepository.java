package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.GamePlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerRepository extends CrudRepository<GamePlayerEntity, Long> { }