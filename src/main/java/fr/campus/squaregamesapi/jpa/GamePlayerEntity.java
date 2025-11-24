package fr.campus.squaregamesapi.jpa;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_player")
public class GamePlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Column(name = "player_id", nullable = false)
    private String playerId;

    @Column(length = 1)
    private String symbol; // facultatif pour TTT / Connect4

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // getters et setters

    public Long getId() {
        return id;
    }

    public GameEntity getGame() {
        return game;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
