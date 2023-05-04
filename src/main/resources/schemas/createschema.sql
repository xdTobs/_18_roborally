# Need to switch of FK check for MySQL since there are crosswise FK references
DROP DATABASE IF EXISTS roborally;
CREATE DATABASE IF NOT EXISTS roborally;
USE roborally;
SET FOREIGN_KEY_CHECKS = 0;;

CREATE TABLE IF NOT EXISTS Game
(
    gameID        int     NOT NULL UNIQUE AUTO_INCREMENT,

    name          varchar(255),

    phase         tinyint,
    step          tinyint,
    currentPlayer tinyint NULL,

    PRIMARY KEY (gameID)
);;

CREATE TABLE IF NOT EXISTS Player
(
    gameID            int     NOT NULL,
    playerID          tinyint NOT NULL,

    name              varchar(255),
    colour            varchar(31),
    checkpointCounter tinyint,

    positionX         int,
    positionY         int,
#     heading           ENUM ("north", "south", "east", "west"),
    PRIMARY KEY (gameID, playerID),
    FOREIGN KEY (gameID) REFERENCES Game (gameID)
);;

SET FOREIGN_KEY_CHECKS = 1;;

#  TODO still some stuff missing here
INSERT INTO Game (name, phase, step, currentPlayer)
VALUES ('TestGame', 0, 0, 0);


INSERT INTO Player (gameID, playerID, name, colour, checkpointCounter, positionX, positionY)
VALUES (LAST_INSERT_ID(), 0, 'TestPlayer', 'red', 0, 0, 0);

SELECT * FROM Game;
SELECT * FROM Player
