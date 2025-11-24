package fr.campus.squaregamesapi.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game")
public class GameEntity {

    @Id
    private String id; // UUID en String

    @Column(name = "factory_id", nullable = false)
    private String factoryId;

    @Column(name = "board_size", nullable = false)
    private int boardSize;

    @Column(name = "player_count", nullable = false)
    private int playerCount;

    @Column(name="current_player")
    private String currentPlayerId;

    @Column(name = "winner_id")
    private String winnerId;

    @Column(name = "current_player")
    private String currentPlayer;

    @Column(name = "winner_id")
    private String winnerId;

    @Column(name = "is_finished")
    private boolean finished;

    @Column(name = "move_count")
    private int moveCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relation avec game_player
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GamePlayerEntity> players;

    // Relation avec game_token
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameTokenEntity> tokens;

    // Relation avec game_remaining_token
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    private List<GameRemainingTokenEntity> remainingTokens;

    // getters et setters

    public String getId() {
        return id;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<GamePlayerEntity> getPlayers() {
        return players;
    }

    public List<GameTokenEntity> getTokens() {
        return tokens;
    }

    public List<GameRemainingTokenEntity> getRemainingTokens() {
        return remainingTokens;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPlayers(List<GamePlayerEntity> players) {
        this.players = players;
    }

    public void setTokens(List<GameTokenEntity> tokens) {
        this.tokens = tokens;
    }

    public void setRemainingTokens(List<GameRemainingTokenEntity> remainingTokens) {
        this.remainingTokens = remainingTokens;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

}
