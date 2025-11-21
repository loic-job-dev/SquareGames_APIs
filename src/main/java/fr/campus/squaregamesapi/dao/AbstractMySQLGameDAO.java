package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMySQLGameDAO implements GameDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public void deleteGame(String gameId) {
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(
                "DELETE FROM game WHERE id = ?")) {
            stmt.setString(1, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(
                "SELECT id, factory_id FROM game");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String gameId = rs.getString("id");
                String factoryId = rs.getString("factory_id");
                Game game = loadGame(gameId, factoryId);
                games.add(game);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return games;
    }

    @Override
    public Game getGameById(String gameId) {
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(
                "SELECT id, factory_id FROM game WHERE id = ?")) {
            stmt.setString(1, gameId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return loadGame(rs.getString("id"), rs.getString("factory_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public abstract void saveGame(Game game);

    /**
     * Chaque DAO spécifique doit implémenter cette méthode
     * pour reconstruire le jeu depuis la DB selon son type.
     */
    protected abstract Game loadGame(String gameId, String factoryId);
}