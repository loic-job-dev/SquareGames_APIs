CREATE DATABASE IF NOT EXISTS square_games;
USE square_games;
CREATE TABLE `TicTacToePlayers` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `playerA` varchar(255),
  `playerB` varchar(255),
  `created_at` timestamp
);

CREATE TABLE `TicTacToeGame` (
  `id` varchar(255) PRIMARY KEY,
  `status` varchar(255),
  `boardSize` int,
  `currentPlayer` varchar(255),
  `winner` varchar(255),
  `remainingMoves` int,
  `players_id` integer NOT NULL,
  `grid` text COMMENT 'Optional: grid as CSV or JSON for fast access',
  `created_at` timestamp
);

CREATE TABLE `TicTacToeCell` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `game_id` varchar(255) NOT NULL,
  `x` int,
  `y` int,
  `symbol` varchar(255),
  `player_id` integer,
  `created_at` timestamp
);

ALTER TABLE `TicTacToeGame` ADD FOREIGN KEY (`players_id`) REFERENCES `TicTacToePlayers` (`id`);

ALTER TABLE `TicTacToeCell` ADD FOREIGN KEY (`game_id`) REFERENCES `TicTacToeGame` (`id`);
