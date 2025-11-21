package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component("mysqlGameDAO")
public class MySQLGameDAO implements GameDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<Game> getGames() {
        List<Game> list = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, status FROM games")) {


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public TicTacToeGame getGameById(String id) {
        try (Connection conn = databaseConnection.getConnection()) {

            // 1️⃣ Charger les infos principales du jeu
            String gameSQL = "SELECT * FROM TicTacToeGame WHERE id = ?";
            UUID gameId;
            int boardSize;
            UUID playerA;
            UUID playerB;

            try (PreparedStatement ps = conn.prepareStatement(gameSQL)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return null; // jeu non trouvé
                    gameId = UUID.fromString(rs.getString("id"));
                    boardSize = rs.getInt("board_size");
                    playerA = UUID.fromString(rs.getString("player_a"));
                    playerB = UUID.fromString(rs.getString("player_b"));
                }
            }

            // 2️⃣ Charger les jetons placés
            String cellSQL = "SELECT x, y, owner_id, symbol FROM TicTacToeCell WHERE game_id = ?";
            List<TokenPosition<UUID>> boardTokens = new ArrayList<>();

            try (PreparedStatement psCell = conn.prepareStatement(cellSQL)) {
                psCell.setString(1, id);
                try (ResultSet rs = psCell.executeQuery()) {
                    while (rs.next()) {
                        int x = rs.getInt("x");
                        int y = rs.getInt("y");
                        UUID owner = UUID.fromString(rs.getString("owner_id"));
                        String tokenName = rs.getString("symbol");
                        boardTokens.add(new TokenPosition<>(owner, tokenName, x, y));
                    }
                }
            }

            // 3️⃣ Charger les jetons non placés
            String remainingSQL = "SELECT owner_id, symbol FROM TicTacToeRemainingToken WHERE game_id = ?";
            List<TokenPosition<UUID>> removedTokens = new ArrayList<>();

            try (PreparedStatement psRem = conn.prepareStatement(remainingSQL)) {
                psRem.setString(1, id);
                try (ResultSet rs = psRem.executeQuery()) {
                    while (rs.next()) {
                        UUID owner = UUID.fromString(rs.getString("owner_id"));
                        String tokenName = rs.getString("symbol");
                        removedTokens.add(new TokenPosition<>(owner, tokenName, -1, -1)); // non placé
                    }
                }
            }

            // 4️⃣ Reconstituer le jeu via la factory
            TicTacToeGameFactory factory = new TicTacToeGameFactory();
            TicTacToeGame game = factory.createGameWithIds(
                    gameId,
                    boardSize,
                    List.of(playerA, playerB),
                    boardTokens,             // jetons déjà placés
                    Collections.emptyList()  // <-- aucun removedToken
            );

            return game;

        } catch (SQLException | InconsistentGameDefinitionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveGame(Game game) {
        Map<CellPosition, Token> board = game.getBoard();
        Collection<Token> remainingTokens = game.getRemainingTokens();

        String insertGameSQL = """
        INSERT INTO TicTacToeGame (id, board_size, player_a, player_b, winner_id)
        VALUES (?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            board_size = VALUES(board_size),
            player_a = VALUES(player_a),
            player_b = VALUES(player_b),
            winner_id = VALUES(winner_id)
    """;

        String deleteCellsSQL = "DELETE FROM TicTacToeCell WHERE game_id = ?";
        String insertCellSQL = "INSERT INTO TicTacToeCell (game_id, x, y, owner_id, symbol) VALUES (?, ?, ?, ?, ?)";

        String deleteRemainingSQL = "DELETE FROM TicTacToeRemainingToken WHERE game_id = ?";
        String insertRemainingSQL = "INSERT INTO TicTacToeRemainingToken (game_id, owner_id, symbol) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection()) {
            conn.setAutoCommit(false); // début de transaction

            // 1️⃣ Insérer ou mettre à jour le jeu
            try (PreparedStatement psGame = conn.prepareStatement(insertGameSQL)) {
                psGame.setString(1, game.getId().toString());
                psGame.setInt(2, game.getBoardSize());
                psGame.setString(3, game.getPlayerIds().toArray()[0].toString());
                psGame.setString(4, game.getPlayerIds().toArray()[1].toString());
                psGame.setString(5, game.getStatus() == GameStatus.TERMINATED
                        ? game.getCurrentPlayerId().toString()
                        : null);
                psGame.executeUpdate();
            }

            // 2️⃣ Supprimer les anciennes cellules puis réinsérer
            try (PreparedStatement psDeleteCells = conn.prepareStatement(deleteCellsSQL)) {
                psDeleteCells.setString(1, game.getId().toString());
                psDeleteCells.executeUpdate();
            }

            try (PreparedStatement psCell = conn.prepareStatement(insertCellSQL)) {
                for (Map.Entry<CellPosition, Token> entry : board.entrySet()) {
                    CellPosition pos = entry.getKey();
                    Token token = entry.getValue();
                    psCell.setString(1, game.getId().toString());
                    psCell.setInt(2, pos.x());
                    psCell.setInt(3, pos.y());
                    psCell.setString(4, token.getOwnerId().orElseThrow().toString());
                    psCell.setString(5, token.getName());
                    psCell.addBatch();
                }
                psCell.executeBatch();
            }

            // 3️⃣ Supprimer les anciens jetons restants puis réinsérer
            try (PreparedStatement psDeleteRemaining = conn.prepareStatement(deleteRemainingSQL)) {
                psDeleteRemaining.setString(1, game.getId().toString());
                psDeleteRemaining.executeUpdate();
            }

            try (PreparedStatement psRem = conn.prepareStatement(insertRemainingSQL)) {
                for (Token token : remainingTokens) {
                    psRem.setString(1, game.getId().toString());
                    psRem.setString(2, token.getOwnerId().orElseThrow().toString());
                    psRem.setString(3, token.getName());
                    psRem.addBatch();
                }
                psRem.executeBatch();
            }

            conn.commit(); // validation de la transaction

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (databaseConnection.getConnection() != null) {
                    databaseConnection.getConnection().rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void deleteGame(Game game) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM games WHERE id = ?")) {

            ps.setString(1, game.getId().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
