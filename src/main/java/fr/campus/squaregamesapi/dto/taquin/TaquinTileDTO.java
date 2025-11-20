package fr.campus.squaregamesapi.dto.taquin;

import fr.le_campus_numerique.square_games.engine.CellPosition;

import java.util.Set;

public class TaquinTileDTO {
    private final int value;
    private final int x;
    private final int y;
    private final Set<CellPosition> allowedMoves;

    // Nouveau constructeur pour le DTO
    public TaquinTileDTO(String name, int x, int y, Set<CellPosition> allowedMoves) {
        this.value = Integer.parseInt(name);
        this.x = x;
        this.y = y;
        this.allowedMoves = allowedMoves;
    }

    public int getValue() { return value; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Set<CellPosition> getAllowedMoves() { return allowedMoves; }
}
