package fr.campus.squaregamesapi.dto.connect4;

public record Connect4TokenDTO(
        int x,
        int y,
        String symbol,   // "R" ou "Y"
        String playerId
) {}