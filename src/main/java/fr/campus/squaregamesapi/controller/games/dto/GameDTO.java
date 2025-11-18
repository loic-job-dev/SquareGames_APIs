package fr.campus.squaregamesapi.controller.games.dto;

import java.util.List;

public class GameDTO {

    private String gameId;
    private int boardSize;
    private String status;
    private String currentPlayer;
    private String winner;
    private int remainingMoves;

    private PlayersDTO players;
    private List<CellDTO> board;
    private String[][] grid;

    public GameDTO(String gameId, int boardSize, String status,
                   String currentPlayer, String winner, int remainingMoves,
                   PlayersDTO players, List<CellDTO> board, String[][] grid) {
        this.gameId = gameId;
        this.boardSize = boardSize;
        this.status = status;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.remainingMoves = remainingMoves;
        this.players = players;
        this.board = board;
        this.grid = grid;
    }

    public String getGameId() {
        return gameId;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public PlayersDTO getPlayers() {
        return players;
    }

    public List<CellDTO> getBoard() {
        return board;
    }

    public String[][] getGrid() {
        return grid;
    }
}
