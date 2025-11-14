package fr.campus.squaregamesapi.controller.games.dto;

public class GameCreationParams {

    private String gameType;
    private int playerCountParam;
    private int boardSizeParam;

    public int getBoardSizeParam() {
        return boardSizeParam;
    }

    public String getGameType() {
        return gameType;
    }

    public int getPlayerCountParam() {
        return playerCountParam;
    }
}
