package fr.campus.squaregamesapi.controller.games.dto.tictactoe;

public class TicTacToePlayersDTO {
    private String playerA;
    private String playerB;

    public TicTacToePlayersDTO(String playerA, String playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public String getPlayerA() { return playerA; }
    public String getPlayerB() { return playerB; }
}
