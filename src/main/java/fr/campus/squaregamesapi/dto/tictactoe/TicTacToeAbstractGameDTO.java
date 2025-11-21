package fr.campus.squaregamesapi.dto.tictactoe;

import fr.campus.squaregamesapi.dto.AbstractGameDTO;

import java.util.List;

public class TicTacToeAbstractGameDTO extends AbstractGameDTO {

    private int boardSize;
    private String currentPlayer;
    private String winner;
    private int remainingMoves;
    private TicTacToePlayersDTO players;
    private List<TicTacToeCellDTO> board;
    private String[][] grid;

    public TicTacToeAbstractGameDTO(String gameId, int boardSize, String status,
                                    String currentPlayer, String winner, int remainingMoves,
                                    TicTacToePlayersDTO players, List<TicTacToeCellDTO> board, String[][] grid) {
        super(gameId, status);
        this.boardSize = boardSize;
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
