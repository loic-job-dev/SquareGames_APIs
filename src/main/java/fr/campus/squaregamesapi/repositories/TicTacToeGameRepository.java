package fr.campus.squaregamesapi.repositories;

import fr.campus.squaregamesapi.entities.*;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private GameRemainingTokenRepository gameRemainingTokenRepository;

    private final TicTacToeGameFactory factory = new TicTacToeGameFactory();

    // ---------------------------------------------------------------
    // 1. Sauvegarde d'une nouvelle partie
    // ---------------------------------------------------------------
    @Transactional
    public void saveNewGame(TicTacToeGame ttt) {
        String gameId = ttt.getId().toString();

        // ----- GAME -----
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameId);
        gameEntity.setFactoryId(ttt.getFactoryId());
        gameEntity.setBoardSize(ttt.getBoardSize());
        gameEntity.setPlayerCount(ttt.getPlayerIds().size());
        gameEntity.setCreatedAt(LocalDateTime.now());

        // Etat du jeu
        gameEntity.setCurrentPlayer(ttt.getCurrentPlayer().toString());
        gameEntity.setWinnerId(null);
        gameEntity.setFinished(false);
        gameEntity.setMoveCount(0);

        // ----- PLAYERS -----
        List<GamePlayerEntity> players = ttt.getPlayerIds().stream()
                .map(pid -> {
                    GamePlayerEntity gp = new GamePlayerEntity();
                    gp.setGame(gameEntity);
                    gp.setPlayerId(pid.toString());
                    gp.setCreatedAt(LocalDateTime.now());
                    return gp;
                })
                .collect(Collectors.toList());

        gameEntity.setPlayers(players);

        // Save() GAME + PLAYERS thanks to Cascade
        gameRepository.save(gameEntity);

        // ----- REMAINING TOKENS -----
        List<GameRemainingTokenEntity> remainingTokens = ttt.getRemainingTokens().stream()
                .map(token -> {
                    GameRemainingTokenEntity rt = new GameRemainingTokenEntity();
                    rt.setGame(gameEntity);
                    rt.setOwnerId(token.getOwnerId().orElseThrow().toString());
                    rt.setTokenName(token.getName());
                    rt.setCreatedAt(LocalDateTime.now());
                    return rt;
                })
                .toList();

        gameRemainingTokenRepository.saveAll(remainingTokens);
    }

    // ---------------------------------------------------------------
    // 2. Mise à jour d'une partie (après un coup)
    // ---------------------------------------------------------------
    @Transactional
    public void saveGame(TicTacToeGame ttt) {
        String gameId = ttt.getId().toString();

        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // ----- NEW TOKENS -----
        List<GameTokenEntity> existing = gameEntity.getTokens();
        List<GameTokenEntity> newTokens = new ArrayList<>();

        ttt.getBoard().forEach((pos, token) -> {
            boolean exists = existing.stream()
                    .anyMatch(gt -> gt.getX() == pos.x() && gt.getY() == pos.y());

            if (!exists) {
                GameTokenEntity gt = new GameTokenEntity();
                gt.setGame(gameEntity);
                gt.setOwnerId(token.getOwnerId().orElseThrow().toString());
                gt.setX(pos.x());
                gt.setY(pos.y());
                gt.setTokenName(token.getName());
                gt.setCreatedAt(LocalDateTime.now());
                newTokens.add(gt);
            }
        });

        existing.addAll(newTokens);

        // ----- UPDATE REMAINING TOKENS -----
        gameRemainingTokenRepository.deleteAll(
                gameRemainingTokenRepository.findByGameId(gameId)
        );

        List<GameRemainingTokenEntity> remainingTokens = ttt.getRemainingTokens().stream()
                .map(token -> {
                    GameRemainingTokenEntity rt = new GameRemainingTokenEntity();
                    rt.setGame(gameEntity);
                    rt.setOwnerId(token.getOwnerId().orElseThrow().toString());
                    rt.setTokenName(token.getName());
                    rt.setCreatedAt(LocalDateTime.now());
                    return rt;
                })
                .toList();

        gameRemainingTokenRepository.saveAll(remainingTokens);

        // ----- UPDATE GAME STATE -----
        gameEntity.setCurrentPlayer(ttt.getCurrentPlayer().toString());
        gameEntity.setMoveCount(ttt.getMoveCount());

        if (ttt.getWinner().isPresent()) {
            gameEntity.setWinnerId(ttt.getWinner().get().toString());
            gameEntity.setFinished(true);
        } else {
            gameEntity.setWinnerId(null);
            gameEntity.setFinished(false);
        }

        gameRepository.save(gameEntity);
    }

    // ---------------------------------------------------------------
    // 3. Reconstruction complète d'un jeu depuis la DB
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public TicTacToeGame loadGame(String gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        List<UUID> players = gameEntity.getPlayers().stream()
                .map(gp -> UUID.fromString(gp.getPlayerId()))
                .toList();

        List<TokenPosition<UUID>> playedTokens = gameEntity.getTokens().stream()
                .map(gt -> new TokenPosition<>(
                        UUID.fromString(gt.getOwnerId()),
                        gt.getTokenName(),
                        gt.getX(),
                        gt.getY()
                ))
                .toList();

        Optional<UUID> winner = gameEntity.getWinnerId() == null ?
                Optional.empty() :
                Optional.of(UUID.fromString(gameEntity.getWinnerId()));

        try {
            return factory.resumeGame(
                    UUID.fromString(gameEntity.getId()),
                    gameEntity.getBoardSize(),
                    players,
                    playedTokens,
                    Collections.emptyList(), // removed tokens for TTT
                    UUID.fromString(gameEntity.getCurrentPlayer()),
                    gameEntity.getMoveCount(),
                    winner
            );
        } catch (InconsistentGameDefinitionException e) {
            throw new RuntimeException("Unable to reconstruct TicTacToe game", e);
        }
    }

    // ---------------------------------------------------------------
    // 4. DELETE + LIST
    // ---------------------------------------------------------------
    @Transactional
    public void deleteGame(String gameId) {
        gameRepository.deleteById(gameId);
    }

    @Transactional(readOnly = true)
    public List<TicTacToeGame> getGames() {
        return gameRepository.findAll().stream()
                .map(entity -> loadGame(entity.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public TicTacToeGame getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .map(entity -> loadGame(entity.getId()))
                .orElse(null);
    }
}
