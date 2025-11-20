package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.campus.squaregamesapi.dao.DatabaseConnection;
import fr.le_campus_numerique.square_games.engine.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLGameDAO implements GameDAO {

    @Override
    public List<Game> getGames() {
        List<Game> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO games (id, status) VALUES (?, ?)")) {

            ps.setString(1, game.getId().toString());
            ps.setString(2, game.getStatus().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGame(Game game) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM games WHERE id = ?")) {

            ps.setString(1, game.getId().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
