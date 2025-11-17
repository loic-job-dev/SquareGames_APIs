package fr.campus.squaregamesapi.config;

import fr.le_campus_numerique.square_games.engine.GameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameFactoryConfig {

    @Bean
    public GameFactory ticTacToeFactory() {
        return new TicTacToeGameFactory();
    }

    @Bean
    public GameFactory taquinFactory() {
        return new TaquinGameFactory();
    }

    @Bean
    public GameFactory connect4Factory() {
        return new ConnectFourGameFactory();
    }
}
