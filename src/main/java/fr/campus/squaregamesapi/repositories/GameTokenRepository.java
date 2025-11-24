package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.GameTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameTokenRepository extends CrudRepository<GameTokenEntity, Long> { }