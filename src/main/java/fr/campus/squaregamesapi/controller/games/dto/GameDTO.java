package fr.campus.squaregamesapi.controller.games.dto;

public abstract class GameDTO {
    protected final String gameId;
    protected final String status;
    protected final int boardSize;

    public GameDTO(String gameId, String status, int boardSize) {
        this.gameId = gameId;
        this.status = status;
        this.boardSize = boardSize;
    }
}