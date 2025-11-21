package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.campus.squaregamesapi.dao.DatabaseConnection;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameStatus;
import fr.le_campus_numerique.square_games.engine.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public Game getGameById(String id) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, status FROM games WHERE id = ?")) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveGame(Game game) {

        Map<CellPosition, Token> board = game.getBoard();
        Collection<Token> remainingTokens = game.getRemainingTokens();

        try (Connection conn = databaseConnection.getConnection()) {
            conn.setAutoCommit(false); // début de la transaction

            // INSERT INTO TicTacToeGame
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO TicTacToeGame (id, board_size, player_a, player_b, winner_id) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, game.getId().toString());
                ps.setInt(2, game.getBoardSize());
                ps.setString(3, game.getPlayerIds().toArray()[0].toString());
                ps.setString(4, game.getPlayerIds().toArray()[1].toString());
                ps.setString(5, game.getStatus() == GameStatus.TERMINATED ? game.getCurrentPlayerId().toString() : null);
                ps.executeUpdate();
            }

            // INSERT INTO TicTacToeCell = coups déjà joués
            try (PreparedStatement psCell = conn.prepareStatement(
                    "INSERT INTO TicTacToeCell (game_id, x, y, owner_id, symbol) VALUES (?, ?, ?, ?, ?)")) {
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

            // INSERT INTO TicTacToeRemainingToken = tokens restants
            try (PreparedStatement psRem = conn.prepareStatement(
                    "INSERT INTO TicTacToeRemainingToken (game_id, owner_id, symbol) VALUES (?, ?, ?)")) {
                for (Token token : remainingTokens) {
                    psRem.setString(1, game.getId().toString());
                    psRem.setString(2, token.getOwnerId().orElseThrow().toString());
                    psRem.setString(3, token.getName());
                    psRem.addBatch();
                }
                psRem.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
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
