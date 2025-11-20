package fr.campus.squaregamesapi.dto;

public class PlayMoveDTO {
    private int x; // colonne
    private int y; // ligne (souvent ignorée pour Connect4)

    public int getX() { return x; }
    public int getY() { return y; }
}
