package fr.campus.squaregamesapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_remaining_token")
public class GameRemainingTokenEntity {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Setter
    @Getter
    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Setter
    @Getter
    @Column(name = "token_name", length = 10)
    private String tokenName;

    @Column(name = "sequence", nullable = false)
    private int sequence;

    @Setter
    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
