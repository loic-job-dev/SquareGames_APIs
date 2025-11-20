package fr.campus.squaregamesapi.dto.connect4;

import fr.campus.squaregamesapi.dto.GameDTO;

import java.util.List;

public class Connect4GameDTO extends GameDTO {

    private final String redPlayerId;
    private final String yellowPlayerId;
    private final String currentPlayer;
    private final String winner; // null si personne
    private final int remainingMoves;

    private final int rows = 6;
    private final int columns = 7;

    private final String[][] grid; // 6 lignes × 7 colonnes
    private final List<Connect4TokenDTO> tokens;

    public Connect4GameDTO(
            String gameId,
            String status,
            String redPlayerId,
            String yellowPlayerId,
            String currentPlayer,
            String winner,
            int remainingMoves,
            String[][] grid,
            List<Connect4TokenDTO> tokens
    ) {
        super(gameId, status);
        this.redPlayerId = redPlayerId;
        this.yellowPlayerId = yellowPlayerId;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.remainingMoves = remainingMoves;
        this.grid = grid;
        this.tokens = tokens;
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }
}
