package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.TokenPosition;
import fr.le_campus_numerique.square_games.engine.InconsistentGameDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class AbstractMySQLGameDAO implements GameDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    protected abstract String getSelectSQL();

    protected abstract String getDeleteSQL();

    protected abstract List<TokenPosition<UUID>> loadTokens(Connection conn, String gameId) throws SQLException;

    protected abstract void saveGameInfo(Connection conn, Game game) throws SQLException;

    protected abstract void saveTokens(Connection conn, Game game) throws SQLException;

    protected abstract Game buildGameFromResultSet(ResultSet rs, List<TokenPosition<UUID>> tokens)
            throws SQLException, InconsistentGameDefinitionException;

    @Override
    public void saveGame(Game game) {
        try (Connection conn = databaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            saveGameInfo(conn, game);
            saveTokens(conn, game);

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la sauvegarde du jeu", e);
        }
    }

    @Override
    public Game getGameById(String id) {
        try (Connection conn = databaseConnection.getConnection()) {
            List<TokenPosition<UUID>> tokens = loadTokens(conn, id);

            try (PreparedStatement ps = conn.prepareStatement(getSelectSQL())) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Game not found: " + id);
                    }
                    return buildGameFromResultSet(rs, tokens);
                }
            }
        } catch (SQLException | InconsistentGameDefinitionException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération du jeu", e);
        }
    }

    @Override
    public void deleteGame(String id) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getDeleteSQL())) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du jeu", e);
        }
    }
}
