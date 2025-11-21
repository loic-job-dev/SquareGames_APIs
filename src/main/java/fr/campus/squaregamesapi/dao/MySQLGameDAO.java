package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.campus.squaregamesapi.dao.DatabaseConnection;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

//            while (rs.next()) {
//                Game g = new Game(rs.getString("id"));  // adapte selon ton constructeur
//                list.add(g);
//            }

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

//            if (rs.next()) {
//                return new Game(rs.getString("id"));
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addGame(Game game) {

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO TicTacToeGame (id, board_size, player_a, player_b) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, game.getId().toString());
            ps.setInt(2, game.getBoardSize());
            ps.setString(3, game.getPlayerIds().toArray()[0].toString());
            ps.setString(4, game.getPlayerIds().toArray()[1].toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGame(Game game) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE games SET status = ? WHERE id = ?")) {

            ps.setString(1, game.getStatus().toString());
            ps.setString(2, game.getId().toString());
            ps.executeUpdate();

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
