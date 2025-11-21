package fr.campus.squaregamesapi.dao;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameStatus;
import fr.le_campus_numerique.square_games.engine.InconsistentGameDefinitionException;
import fr.le_campus_numerique.square_games.engine.TokenPosition;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component("mysqlTicTacToeGameDAO")
public class MySQLTicTacToeDAO extends AbstractMySQLGameDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    public MySQLTicTacToeDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    protected String getSelectSQL() {
        return "SELECT * FROM TicTacToeGame WHERE id = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM TicTacToeCell WHERE game_id = ?";
    }

    @Override
    protected List<TokenPosition<UUID>> loadTokens(Connection conn, String gameId) throws SQLException {
        String sql = "SELECT x, y, owner_id, symbol FROM TicTacToeCell WHERE game_id = ?";
        List<TokenPosition<UUID>> tokens = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    UUID owner = UUID.fromString(rs.getString("owner_id"));
                    String symbol = rs.getString("symbol");
                    tokens.add(new TokenPosition<>(owner, symbol, x, y));
                }
            }
        }
        return tokens;
    }

    @Override
    protected void saveGameInfo(Connection conn, Game game) throws SQLException {
        String sql = """
            INSERT INTO TicTacToeGame (id, board_size, player_a, player_b, winner_id)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                board_size = VALUES(board_size),
                player_a = VALUES(player_a),
                player_b = VALUES(player_b),
                winner_id = VALUES(winner_id)
        """;
        TicTacToeGame ttt = (TicTacToeGame) game;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ttt.getId().toString());
            ps.setInt(2, ttt.getBoardSize());
            ps.setString(3, ttt.getPlayerIds().toArray()[0].toString());
            ps.setString(4, ttt.getPlayerIds().toArray()[1].toString());
            ps.setString(5, ttt.getStatus() == GameStatus.TERMINATED
                    ? ttt.getCurrentPlayerId().toString()
                    : null);
            ps.executeUpdate();
        }
    }

    @Override
    protected void saveTokens(Connection conn, Game game) throws SQLException {
        TicTacToeGame ttt = (TicTacToeGame) game;

        // Supprimer anciens tokens
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM TicTacToeCell WHERE game_id = ?")) {
            ps.setString(1, ttt.getId().toString());
            ps.executeUpdate();
        }

        // Réinsérer les tokens
        String insertSQL = "INSERT INTO TicTacToeCell (game_id, x, y, owner_id, symbol) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            ttt.getBoard().forEach((pos, token) -> {
                try {
                    ps.setString(1, ttt.getId().toString());
                    ps.setInt(2, pos.x());
                    ps.setInt(3, pos.y());
                    ps.setString(4, token.getOwnerId().orElseThrow().toString());
                    ps.setString(5, token.getName());
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            ps.executeBatch();
        }
    }

    @Override
    protected Game buildGameFromResultSet(ResultSet rs, List<TokenPosition<UUID>> tokens)
            throws SQLException, InconsistentGameDefinitionException {
        UUID gameId = UUID.fromString(rs.getString("id"));
        int boardSize = rs.getInt("board_size");
        UUID playerA = UUID.fromString(rs.getString("player_a"));
        UUID playerB = UUID.fromString(rs.getString("player_b"));

        TicTacToeGameFactory factory = new TicTacToeGameFactory();
        return factory.createGameWithIds(
                gameId,
                boardSize,
                List.of(playerA, playerB),
                tokens,
                Collections.emptyList() // removed tokens
        );
    }

    @Override
    public List<Game> getGames() {
        List<Game> list = new ArrayList<>();
        String sql = "SELECT id FROM TicTacToeGame";

        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                Game g = getGameById(id);
                if (g != null) list.add(g);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
