# Roborally Part 4: How to play, features and thoughts

## How to play

To play Roborally... (Open in Intellij?). When the game launches, select "File" in the upper left corner, and press "New Game". You will then be prompted to select a gameboard from your files. Click "OK" and select "Dizzy Highway". After this, select number of players when prompted. The game will then start, and you can play the game.

## Features implemented

Our Roborally game includes several key features:
- Loading a gameboard from a JSON file. There's a full gameboard called Dizzy Highway, containing all of the spacetypes that have been implemented up to this date. Furthermore, there's an empty gameboard and a testboard.
- Saving a game as a JSON file
- Partly loading a saved from a JSON file. To see our thoughts on the implementation, see "Thoughts on load feature", later in this document.
- Several spacetypes, such as checkpoints, both types of conveyorbelts and walls.
- Playing and drawing new cards to play on your turn.
- Finding a winner when all checkpoints have been reached by a player.


## Thoughts on load feature

We started implementing the json serializing and deserializing in on our own, and we have it is almost working, but there are some hard to solve bugs. To get rid of these we will reimplement the serde and deser and rely much more on the fileaccess package given to us from the professor. We had problems getting up and running with our classes, because they differ a bit in architecture from the one the professor supplies, but we have almost solved that, so in a approximately a week we will have the serializing perfected.

For now much of the state works when serializing and deserializing, but when we want to deserialize the board from json the the subclasses of `Space` gets lost and all subtypes gets turned into generic Spaces, which makes the board not very useful. Unfortunatly we will not have time to fix this for the delivery on 5th of May, but it is almost completed and will be done as soon as possible.
