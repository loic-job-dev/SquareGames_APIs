package fr.campus.squaregamesapi.controller.games.dto;

public class PlayersDTO {
    private String playerA;
    private String playerB;

    public PlayersDTO(String playerA, String playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public String getPlayerA() { return playerA; }
    public String getPlayerB() { return playerB; }
}
