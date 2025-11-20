package fr.campus.squaregamesapi.dto.tictactoe;

public class TicTacToeCellPositionDTO {
    private int x;
    private int y;

    public TicTacToeCellPositionDTO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
