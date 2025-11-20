package fr.campus.squaregamesapi.dto.taquin;

import fr.campus.squaregamesapi.dto.GameDTO;
import java.util.List;

public class TaquinGameDTO extends GameDTO {

    private final int boardSize;
    private final String[][] grid;
    private final List<TaquinTileDTO> tiles;
    private final int emptyX;
    private final int emptyY;

    public TaquinGameDTO(String gameId,
                         String status,
                         int boardSize,
                         String[][] grid,
                         List<TaquinTileDTO> tiles,
                         int emptyX,
                         int emptyY) {
        super(gameId, status);
        this.boardSize = boardSize;
        this.grid = grid;
        this.tiles = tiles;
        this.emptyX = emptyX;
        this.emptyY = emptyY;
    }

    // Getters
    public int getBoardSize() {
        return boardSize;
    }

    public String[][] getGrid() {
        return grid;
    }

    public List<TaquinTileDTO> getTiles() {
        return tiles;
    }

    public int getEmptyX() {
        return emptyX;
    }

    public int getEmptyY() {
        return emptyY;
    }
}
