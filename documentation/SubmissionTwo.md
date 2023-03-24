---
title: "RoboRally Part 2"
date: March 23, 2023
geometry: margin=3cm
header-includes: |
  \usepackage{fancyhdr}
  \pagestyle{fancy}
  \fancyfoot[CO,CE]{Group 18}
  \fancyfoot[LE,RO]{\thepage}
---

\newpage

# Functional Requirements

# MoSCoW Requirement List

## Must have

The game must have the following features:

- 1 - Play on at least one course.
- 2 - 2 to 6 players can play the game.
- 3 - Draw cards from all existing cards that are described in the rules
- 4 - Play 5 programming cards in a turn to plan the robot's movements.
- 5 - Choose which programming cards to play.
- 6 - Draw new programming cards each turn.
- 7 - Robots should start at starting field.
- 8 - Robots can move on the board.
- 9 - Shop refreshes cards.
- 10 - Plan a new turn when the robots have stopped moving, if the game is not finished.
- 11 - Push other players robots the robot bumps into them, during the moving phase.
- 12 - The robots moves one programming card at a time, and then it is the next robot's turn to move.
- 13 - Take damage and place this damage card in the discard pile
- 14 - Unused cards get shuffled after the round.
- 15 - Walls can not be moved through.
- 16 - Reach checkpoints with the robot.
- 17 - Finish and win the game, if I collect the correct amount of checkpoints that is required to win the game on that particular board.
- 18 - Visually distinguishable fields
- 19 - Draw my programming cards from the shuffled discard pile, when a programming phase begins

## Should have

The game should have the following features:

- 20 - Robots receive damage
- 21 - Robots shoot a laser forward, after each move, so that other players robots get hit and take damage.
- 22 - Play a damage card in the programming phase.
- 23 - Robots standing on a checkpoint at the end of a register, so that the player gets a checkpoint to their collection.
- 24 - Landing on a blue conveyor belt moves the robot two spaces forward of the conveyor belt.
- 25 - Landing on a green conveyor belt moves the robot one space forward of the conveyor belt.
- 26 - Landing on a push panel moves the robot to the next space of the direction the panel is facing.
- 27 - Landing on a gear, turns the robot 90 degrees in the direction of the arrow on the gear.
- 28 - Robots can get hit by lasers placed on the map, and take damage if hit, every cycle of the moving phase.
- 29 - Robots take damage if moving out of bounds.
- 30 - Robots that land in a pit takes damage.
- 31 - Robot re-spawns where it started the game, when the robot goes out of bounds, or it lands in a pit.
- 32 - Players can visually see what is happening during a round.
- 33 - Players can change the order of their programming cards during the programming phase, if they make a mistake, or changes their mind.
- 34 - The non-played programming cards, ends in the discard pile when the programming phase is over.

## Could have

- 35 - A timer of 30 seconds starts in the activation phase, when a player is done programming, so that the other players run out of time.
- 36 - PLayers gets random programming cards on their empty programming fields if they run out of time.

## Would be nice to have

- 37 - Use energy tokens to upgrade robot.
- 38 - Purchase upgrade cards to upgrade robot.
- 39 - Play temporary upgrade cards before a turn, and get it removed after the turn.
- 40 - Choose from different upgrade cards to buy.
- 41 - Players keeps the non-temporary upgrading cards after the turn.
- 42 - Change upgrade cards, if a player has more than the robot can carry.
- 43 - Players get one of four damage cards, when their robot takes damage.
- 44 - If the robot falls into a pit, fall of the board, or activate a worm card, the robot must reboot and take the following actions:
  - Take two spam damage cards and place them in the players discard pile.
  - Cancel the players programming.
  - Discard the programming cards (including damage cards) from register and hand.
  - Wait until next turn to program the robot.
  - Place the robot on the reboot token that the robot started the game on and play temporary upgrading cards during activation phase.

\newpage

# Taxonomy

Substantives will be used as inspiration for our classes and fields on classes, and verbs for methods

| Substantives         | Verbs       |
| -------------------- | ----------- |
| Gameboard            | Upgrade     |
| Start board          | Programm    |
| Robot figure         | Activate    |
| priority antenna     | Purchase    |
| 30-second sand timer | Shuffle     |
| Programming cards    | Reboot      |
| Player mat           | Move        |
| Reboot tokens        | Draw        |
| Checkpoint tokens    | Turn        |
| Programming cards    | Flip        |
| Damage cards         | Fire        |
| Upgrade shop         | Push        |
| Checkpoint           | Rotate      |
| Energy bank          | Win         |
| Energy reserve       | Take damage |
|                      | Lose        |

\newpage

## Concepts, Features and Use Cases

### Concepts

The game RoboRally includes many concepts, that are hard to group accurately.
We will do out best to write out as many as possible, that cover the basic game:

1. **Robots**: The game revolves around the concept of robots moving around a field, to reach their destination as fast as possible
2. **Factory**: The game is played in a factory, which includes many sub concepts, such as **Obstacles, Conveyor Belts** and **Lasers**.
3. **Programming cards**: Each turn a player programs is made using Programming Cards. These cards predetermine the turn's outcome, and are an important concept to get right, to get the right feel of the game.
4. **Game Phases**: The game is split in different game phases, which include the **Programming Phase** and the **Movement Phase**. This is a unique part of Roborally, and is equally important to get right.

### Features

The features of a game like Roborally is a bit of an abstract concept, we have chosen some things that we consider to be the most important features of Roborally. There is some overlap between Concepts and Features, since the Concepts are implemented as Features.

1. **Game Phases**: The players are able to select their **Programming Cards** in the **Programming Phase** of the game, before these cards are then executed in the **Movement Phase**.
2. **Programming Cards**: The players will be able to program what their robot will do using cards that predetermine moves and timing.
3. **Lasers**: The Factory will include lasers on the map, which do damage to robots standing in their way. This damage will worsen the **Programming Cards** that are available by the player on the receiving end.
4. **Robot Lasers**: At the end of the **Movement Phase**, players will also shoot these lasers, in the direction they are facing.

## Use Cases

Use cases are never easy to define for a game, since the major use case is almost always just: "play the game". There are however some additional use cases to be had with Roborally.

1. Play the Game: This is always going to be the main use case for any game.
2. Save the Game: We intend to implement saving the game state in the middle of a game. This is not in itself useful, but will be utilized in another use case.
3. Load Game: This is the obvious follow-up to the previous use case.

\newpage

## Class Diagrams

Since the scale of this program is quite large, we do not believe that a single class diagram will fit all of our classes, and still be readable. Therefore, we have decided to split these into one general class diagram, and a few more specific class diagrams, for help with implementation.

### Overview

This is the structure in which our MVC classes interacts with one another. We can only see the way that the different classes related to the Board(GameBoard) interacts here, but most of the other game elements follow a similar pattern.

![MVC interactions with Board](./media/class-diagrams/overview/overview.png)

\newpage

### View

The view gets updated through the observer/subject, also known as observer/observable, pattern.

![view package in our MVC structure](./media/class-diagrams/view/view.png)

\newpage

### Model and Controller

When state is update in model the view is notified because all parts of the model inherits the Subject class.

![model and controller package in our MVC structure](./media/class-diagrams/modelAndController/modelAndController.png)

\newpage

### Board

Board is a sub-package in the model package.

![board package](./media/class-diagrams/altBoard/altBoard.png){height=700px}

## Activity Diagram

Activity diagram containing our must-have requirements

![activity diagram](./media/Must%20have%20req%20activitydiagram/Must%20have%20req%20activitydiagram.png)

\newpage

# Non-Functional Requirements

## Performance {.unnumbered .unlisted}

The game should be fast and responsive, with minimal lag or delays between player actions.

## Usability {.unnumbered .unlisted}

The game interface should be intuitive and easy to use, with clear instructions and feedback for players.

## Reliability {.unnumbered .unlisted}

The game should be stable and reliable, with zero errors or crashes.

## Compatibility {.unnumbered .unlisted}

The game should be compatible with a range of operating systems, and should run smoothly on different hardware configurations.

## Scalability {.unnumbered .unlisted}

The game should be able to handle large numbers of players and game sessions, without sacrificing performance or usability.

## Maintainability {.unnumbered .unlisted}

The code should be well-structured and modular, with clear documentation and easy-to-understand logic, to make future updates and maintenance easier.

## Accessibility {.unnumbered .unlisted}

The game should be designed to be accessible to a wide range of users.

## Localization {.unnumbered .unlisted}

The game should be designed to support multiple languages and cultural contexts, to make it accessible to a global audience.

