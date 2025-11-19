package fr.campus.squaregamesapi.controller.games.dto.tictactoe;

import java.util.List;

public class TicTacToeGameDTO {

    private String gameId;
    private int boardSize;
    private String status;
    private String currentPlayer;
    private String winner;
    private int remainingMoves;

    private TicTacToePlayersDTO players;
    private List<TicTacToeCellDTO> board;
    private String[][] grid;

    public TicTacToeGameDTO(String gameId, int boardSize, String status,
                            String currentPlayer, String winner, int remainingMoves,
                            TicTacToePlayersDTO players, List<TicTacToeCellDTO> board, String[][] grid) {
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

    public TicTacToePlayersDTO getPlayers() {
        return players;
    }

    public List<TicTacToeCellDTO> getBoard() {
        return board;
    }

    public String[][] getGrid() {
        return grid;
    }
}
