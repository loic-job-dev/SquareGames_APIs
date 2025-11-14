package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.interfaces.GameCatalog;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class GameCatalogImpl implements GameCatalog {

    private TicTacToeGameFactory ticTacToeGameFactory = new TicTacToeGameFactory();

    @Override
    public Collection<String> getGameIdentifiers() {
        return List.of(ticTacToeGameFactory.getGameFactoryId());
    }
}
