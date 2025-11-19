package fr.campus.squaregamesapi.controller.games.dto.tictactoe;

public class TicTacToeCellPositionDTO {
    private int k;
    private int j;

    public TicTacToeCellPositionDTO(int k, int j) {
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
