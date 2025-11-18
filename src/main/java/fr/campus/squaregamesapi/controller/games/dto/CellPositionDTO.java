package fr.campus.squaregamesapi.controller.games.dto;

public class CellPositionDTO {
    private int k;
    private int j;

    public CellPositionDTO(int k, int j) {
        this.k = k;
        this.j = j;
    }

    public int getK() {
        return k;
    }
    public int getJ() {
        return j;
    }
}
