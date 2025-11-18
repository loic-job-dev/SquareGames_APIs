package fr.campus.squaregamesapi.controller.games.dto;

public class CellDTO {
    private int x;
    private int y;
    private String symbol;
    private String playerId;

    public CellDTO(int x, int y, String symbol, String playerId) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.playerId = playerId;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getSymbol() { return symbol; }
    public String getPlayerId() { return playerId; }
}
