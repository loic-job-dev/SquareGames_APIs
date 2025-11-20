package fr.campus.squaregamesapi.dto.connect4;

import fr.campus.squaregamesapi.dto.GameDTO;

import java.util.List;

public class Connect4GameDTO extends GameDTO {

    private final String redPlayer;
    private final String yellowPlayer;
    private final String currentPlayer;
    private final String winner;
    private final int remainingTokens;
    private final String[][] grid;
    private final List<Connect4TokenDTO> tokens;

    public Connect4GameDTO(
            String gameId,
            String status,
            String redPlayer,
            String yellowPlayer,
            String currentPlayer,
            String winner,
            int remainingTokens,
            String[][] grid,
            List<Connect4TokenDTO> tokens
    ) {
        super(gameId, status);  // boardSize pas utilisé ici mais hérité
        this.redPlayer = redPlayer;
        this.yellowPlayer = yellowPlayer;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.remainingTokens = remainingTokens;
        this.grid = grid;
        this.tokens = tokens;
    }

    public String getRedPlayer() {
        return redPlayer;
    }

    public String getYellowPlayer() {
        return yellowPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public int getRemainingTokens() {
        return remainingTokens;
    }

    public String[][] getGrid() {
        return grid;
    }

    public List<Connect4TokenDTO> getTokens() {
        return tokens;
    }

    public int getRows() {
        return 6;
    }

    public int getColumns() {
        return 7;
    }
}
