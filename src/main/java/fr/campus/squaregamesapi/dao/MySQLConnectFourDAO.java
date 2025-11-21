package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGame;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("mysqlConnectFourGameDAO")
public class MySQLConnectFourDAO extends AbstractMySQLGameDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    private final ConnectFourGameFactory factory = new ConnectFourGameFactory();

    @Override
    public void saveGame(Game game) {
        if (!(game instanceof ConnectFourGame c4)) {
            throw new IllegalArgumentException("Expected ConnectFourGame");
        }

        String gameId = c4.getId().toString();
        String factoryId = c4.getFactoryId();

        try {
            Connection conn = databaseConnection.getConnection();
            conn.setAutoCommit(false);

            // --- 1️⃣ Insert or update the game ---
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO game (id, factory_id, board_size, player_count) " +
                            "VALUES (?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE factory_id = VALUES(factory_id), board_size = VALUES(board_size), player_count = VALUES(player_count)")) {
                stmt.setString(1, gameId);
                stmt.setString(2, factoryId);
                stmt.setInt(3, c4.getBoardSize());
                stmt.setInt(4, 2); // Connect4 = 2 joueurs
                stmt.executeUpdate();
            }

            // --- 2️⃣ Supprimer les anciens tokens ---
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM game_token WHERE game_id = ?")) {
                stmt.setString(1, gameId);
                stmt.executeUpdate();
            }

            // --- 3️⃣ Insérer les tokens ---
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO game_token (game_id, owner_id, x, y, token_name) VALUES (?, ?, ?, ?, ?)")) {
                for (Map.Entry<CellPosition, Token> entry : c4.getBoard().entrySet()) {
                    Token token = entry.getValue();
                    CellPosition pos = entry.getKey();
                    UUID ownerId = token.getOwnerId().orElseThrow();

                    stmt.setString(1, gameId);
                    stmt.setString(2, ownerId.toString());
                    stmt.setInt(3, pos.x());
                    stmt.setInt(4, pos.y());
                    stmt.setString(5, token.getName());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save ConnectFour game", e);
        }
    }

    @Override
    protected Game loadGame(String gameId, String factoryId) {
        if (!"connect4".equals(factoryId)) {
            throw new IllegalArgumentException("Factory ID mismatch: " + factoryId);
        }

        try {
            // --- 1️⃣ Charger le jeu ---
            int boardSize;
            try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(
                    "SELECT board_size FROM game WHERE id = ?")) {
                stmt.setString(1, gameId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    boardSize = rs.getInt("board_size");
                }
            }

            // --- 2️⃣ Charger les joueurs ---
            List<UUID> players = new ArrayList<>();
            try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(
                    "SELECT player_id FROM game_player WHERE game_id = ? ORDER BY id ASC")) {
                stmt.setString(1, gameId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        players.add(UUID.fromString(rs.getString("player_id")));
                    }
                }
            }

            if (players.size() != 2) {
                throw new IllegalStateException("Expected 2 players, found: " + players.size());
            }

            // --- 3️⃣ Charger les tokens ---
            List<TokenPosition<UUID>> tokenPositions = new ArrayList<>();
            try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(
                    "SELECT owner_id, x, y, token_name FROM game_token WHERE game_id = ?")) {
                stmt.setString(1, gameId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        UUID owner = UUID.fromString(rs.getString("owner_id"));
                        int x = rs.getInt("x");
                        int y = rs.getInt("y");
                        String tokenName = rs.getString("token_name");

                        tokenPositions.add(new TokenPosition<>(owner, tokenName, x, y));
                    }
                }
            }

            // --- 4️⃣ Créer le jeu via la factory ---
            return factory.createGameWithIds(
                    UUID.fromString(gameId),
                    boardSize,
                    players,
                    tokenPositions,
                    Collections.emptyList() // aucun token retiré pour Connect4
            );

        } catch (SQLException | InconsistentGameDefinitionException e) {
            throw new RuntimeException(e);
        }
    }
}
