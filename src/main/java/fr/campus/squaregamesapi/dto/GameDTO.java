package fr.campus.squaregamesapi.dto;

public abstract class GameDTO {
    protected final String gameId;
    protected final String status;

    public GameDTO(String gameId, String status) {
        this.gameId = gameId;
        this.status = status;
    }
}