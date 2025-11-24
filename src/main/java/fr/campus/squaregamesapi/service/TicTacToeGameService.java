package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.jpa.GameEntity;
import fr.campus.squaregamesapi.jpa.GamePlayerEntity;
import fr.campus.squaregamesapi.jpa.GameTokenEntity;
import fr.campus.squaregamesapi.repositories.GameRepository;
import fr.campus.squaregamesapi.repositories.GamePlayerRepository;
import fr.campus.squaregamesapi.repositories.GameTokenRepository;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.InconsistentGameDefinitionException;
import fr.le_campus_numerique.square_games.engine.Token;
import fr.le_campus_numerique.square_games.engine.TokenPosition;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicTacToeGameService {

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

        // --- Sauvegarder le gagnant uniquement si tu ajoutes un getter dans le moteur ---
        // gameEntity.setWinnerId(ttt.getWinnerId() != null ? ttt.getWinnerId().toString() : null);

        // --- Convertir les joueurs, en garantissant l'unicité ---
        List<GamePlayerEntity> playerEntities = ttt.getPlayerIds().stream()
                .distinct() // élimine les doublons
                .map(pid -> {
                    GamePlayerEntity gp = new GamePlayerEntity();
                    gp.setGame(gameEntity);
                    gp.setPlayerId(pid.toString());
                    return gp;
                }).collect(Collectors.toList());
        gameEntity.setPlayers(playerEntities);

        // --- Convertir les tokens ---
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
}
