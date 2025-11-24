package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.GameEntity;
import fr.campus.squaregamesapi.entities.GamePlayerEntity;
import fr.campus.squaregamesapi.entities.GameTokenEntity;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional
public class TicTacToeGameRepository {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GameTokenRepository gameTokenRepository;

    private final TicTacToeGameFactory factory = new TicTacToeGameFactory();

    public void saveGame(TicTacToeGame ttt) {
        String gameId = ttt.getId().toString();

        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameId);
        gameEntity.setFactoryId(ttt.getFactoryId());
        gameEntity.setBoardSize(ttt.getBoardSize());
        gameEntity.setPlayerCount(ttt.getPlayerIds().size());


        //Convertir les joueurs, en garantissant l'unicité
        List<GamePlayerEntity> playerEntities = ttt.getPlayerIds().stream()
                .distinct() // élimine les doublons
                .map(pid -> {
                    GamePlayerEntity gp = new GamePlayerEntity();
                    gp.setGame(gameEntity);
                    gp.setPlayerId(pid.toString());
                    return gp;
                }).collect(Collectors.toList());
        gameEntity.setPlayers(playerEntities);

        //Convertir les tokens
        List<GameTokenEntity> tokenEntities = ttt.getBoard().entrySet().stream()
                .map(entry -> {
                    CellPosition pos = entry.getKey();
                    Token token = entry.getValue();

                    GameTokenEntity gt = new GameTokenEntity();
                    gt.setGame(gameEntity);
                    gt.setOwnerId(token.getOwnerId().orElseThrow().toString());
                    gt.setX(pos.x());
                    gt.setY(pos.y());
                    gt.setTokenName(token.getName());
                    return gt;
                }).collect(Collectors.toList());
        gameEntity.setTokens(tokenEntities);

        gameRepository.save(gameEntity);
    }

    @Transactional(readOnly = true)
    public TicTacToeGame loadGame(String gameId) {
        Optional<GameEntity> optGame = gameRepository.findById(gameId);
        if (optGame.isEmpty()) return null;

        GameEntity gameEntity = optGame.get();

        List<UUID> players = gameEntity.getPlayers().stream()
                .map(gp -> UUID.fromString(gp.getPlayerId()))
                .collect(Collectors.toList());

        List<TokenPosition<UUID>> tokenPositions = gameEntity.getTokens().stream()
                .map(gt -> new TokenPosition<>(
                        UUID.fromString(gt.getOwnerId()),
                        gt.getTokenName(),
                        gt.getX(),
                        gt.getY()))
                .collect(Collectors.toList());

        try {
            return factory.createGameWithIds(
                    UUID.fromString(gameEntity.getId()),
                    gameEntity.getBoardSize(),
                    players,
                    tokenPositions,
                    Collections.emptyList()
            );
        } catch (InconsistentGameDefinitionException e) {
            throw new RuntimeException("Impossible de recréer le jeu TicTacToe", e);
        }
    }

    public void deleteGame(String gameId) {
        gameRepository.deleteById(gameId);
    }

    @Transactional(readOnly = true)
    public List<Game> getGames() {
        List<Game> games = new ArrayList<>();

        for (GameEntity entity : gameRepository.findAll()) {
            Game g = loadGame(entity.getId());
            games.add(g);
        }
        return games;
    }

    @Transactional(readOnly = true)
    public TicTacToeGame getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .map(entity -> loadGame(entity.getId()))
                .orElse(null);
    }
}
