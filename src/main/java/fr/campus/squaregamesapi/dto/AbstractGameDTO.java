package fr.campus.squaregamesapi.dto;

public abstract class AbstractGameDTO {
    protected final String gameId;
    protected final String status;

    public AbstractGameDTO(String gameId, String status) {
        this.gameId = gameId;
        this.status = status;
    }
}