# Functional Requirements
## Concepts, Features and Use Cases
### Concepts
The game Roborally includes many concepts, that are hard to group accurately.
We will do out best to write out as many as possible, that cover the basic game:
<br>
1. __Robots__: The game revolves around the concept of robots moving around a field, to reach their destination as fast as possible
2. __Factory__: The game is played in a factory, which includes many sub concepts, such as __Obstacles, Conveyor Belts__ and __Lasers__.
3. __Programming cards__: Each turn a player programs is made using Programming Cards. These cards predetermine the turn's outcome, and are an important concept to get right, to get the right feel of the game.
4. __Game Phases__: The game is split in different game phases, which include the __Programming Phase__ and the __Movement Phase__. This is a unique part of Roborally, and is equally important to get right.
### Features
The features of a game like Roborally is a bit of an abstract concept, we have chosen some things that we consider to be the most important features of Roborally. There is some overlap between Concepts and Features, since the Concepts are implemented as Features.
1. __Game Phases__: The players are able to select their __Programming Cards__ in the __Programming Phase__ of the game, before these cards are then executed in the __Movement Phase__.
2. __Programming Cards__: The players will be able to program what their robot will do using cards that predetermine moves and timing.
3. __Lasers__: The Factory will include lasers on the map, which do damage to robots standing in their way. This damage will worsen the __Programming Cards__ that are available by the player on the receiving end.
4. __Robot Lasers__: At the end of the __Movement Phase__, players will also shoot these lasers, in the direction they are facing.
### Use Cases
Use cases are never easy to define for a game, since the major use case is almost always just: "play the game". There are however some additional use cases to be had with Roborally.
1. Play the Game: This is always going to be the main use case for any game.
2. Save the Game: We intend to implement saving the game state in the middle of a game. This is not in itself useful, but will be utilized in another use case.
3. Load Game: This is the obvious follow-up to the previous use case.
4. _Maybe add phases as use cases if it makes sense_
### UC Chart
_Insert Use case diagram_
## Domain Model
### Class Diagram (s)
Since the scale of this program is quite large, we do not believe that a single class diagram will fit all of our classes, and still be readable. Therefore, we have decided to split these into more general class diagrams for oversight, and more specific class diagrams, for help with implementation.
<br>
To begin with we have decided on a total overview class diagram, and a specific class diagram for our Factory/Board

### Activity Diagrams
_IDK what to do with this yet_
### State Diagrams
_Maybe not necessary_
# Non-Functional Requirements

### Performance
The game should be fast and responsive, with minimal lag or delays between player actions. (how fast? 50 ms?)
### Usability
The game interface should be intuitive and easy to use, with clear instructions and feedback for players.
### Reliability
The game should be stable and reliable, with zero errors or crashes.
### Compatibility
The game should be compatible with a range of operating systems, and should run smoothly on different hardware configurations.





