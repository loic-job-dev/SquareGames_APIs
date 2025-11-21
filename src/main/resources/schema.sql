DROP DATABASE IF EXISTS square_games;
CREATE DATABASE square_games;
USE square_games;

-- Table des jeux
CREATE TABLE TicTacToeGame (
                               id VARCHAR(36) PRIMARY KEY,             -- correspond à UUID du jeu
                               board_size INT NOT NULL,
                               player_a VARCHAR(36) NOT NULL,         -- UUID joueur A
                               player_b VARCHAR(36) NOT NULL,         -- UUID joueur B
                               winner_id VARCHAR(36),                  -- UUID gagnant (null si ongoing)
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des cellules occupées
CREATE TABLE TicTacToeCell (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               game_id VARCHAR(36) NOT NULL,
                               x INT NOT NULL,
                               y INT NOT NULL,
                               owner_id VARCHAR(36) NOT NULL,         -- UUID du propriétaire du jeton
                               symbol CHAR(1) NOT NULL,               -- 'X' ou 'O' pour faciliter la lecture
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (game_id) REFERENCES TicTacToeGame(id)
);

-- Table pour les jetons non placés (remaining tokens)
CREATE TABLE TicTacToeRemainingToken (
                                         id INT PRIMARY KEY AUTO_INCREMENT,
                                         game_id VARCHAR(36) NOT NULL,
                                         owner_id VARCHAR(36) NOT NULL,         -- UUID du propriétaire
                                         symbol CHAR(1) NOT NULL,
                                         position_x INT,                        -- NULL si non placé
                                         position_y INT,                        -- NULL si non placé
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (game_id) REFERENCES TicTacToeGame(id)
);
