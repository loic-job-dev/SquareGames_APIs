DROP DATABASE IF EXISTS square_games;
CREATE DATABASE square_games;
USE square_games;

-- Table des jeux
CREATE TABLE game (
                      id VARCHAR(36) PRIMARY KEY,          -- UUID du jeu
                      factory_id VARCHAR(50) NOT NULL,     -- "tictactoe", "connect4", "15 puzzle"
                      board_size INT NOT NULL,
                      player_count INT NOT NULL,
                      winner_id VARCHAR(36),               -- UUID du gagnant si applicable
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des joueurs par jeu
CREATE TABLE game_player (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             game_id VARCHAR(36) NOT NULL,
                             player_id VARCHAR(36) NOT NULL,
                             symbol CHAR(1),                      -- facultatif pour TTT / Connect4
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (game_id) REFERENCES game(id)
);

-- Table des jetons ou tuiles placés
CREATE TABLE game_token (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            game_id VARCHAR(36) NOT NULL,
                            owner_id VARCHAR(36) NOT NULL,
                            x INT NOT NULL,
                            y INT NOT NULL,
                            token_name VARCHAR(10),              -- "X", "O", "R", "Y", "1", "2", ...
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (game_id) REFERENCES game(id)
);

-- Table des jetons restants / non placés (pour TTT et Connect4)
CREATE TABLE game_remaining_token (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      game_id VARCHAR(36) NOT NULL,
                                      owner_id VARCHAR(36) NOT NULL,
                                      token_name VARCHAR(10),
                                      position_x INT,                      -- null si non placé
                                      position_y INT,                      -- null si non placé
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (game_id) REFERENCES game(id)
);
