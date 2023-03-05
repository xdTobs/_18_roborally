# user stories

## must have

the game must have the following features:

- us1 - play on at least one course, so that it is possible to play the game
- us2 - 1 to 3 other players, so that it is a multiplayer game
- us3 - the opportunity to draw programming cards from all existing cards that are described in the rules
- us4 - draw new programming cards, so that i do not run out of them
- us5 - play 5 programming cards in a turn to plan the robot's movements
- us6 - robot can move on the board
- us7 - the opportunity to draw programming cards
- us8 - robot's should start at starting field
- us9 - that the shop never run out of cards, so that i can always draw new ones
- us10 - choose which programming cards to play
- us11 - plan a new turn when the robots have stopped moving, if the game is not finished
- us12 - push other players robots if my robot bumps into them, during the moving phase
- us13 - the robots to move one programming card at a time, and then it is the next robot's turn to move
- us16 - take damage and place this damage card in my discard pile, so that the other players have an advantage when i take damage
- us18 - unused cards that get shuffled after the round
- us20 - walls can not be moved through
- us21 - reach checkpoints, to win the game
- us23 - finish and win the game, if i collect the correct amount of checkpoints that is required to win the game on that particular board
- visually distinguishable fields
- place 5 programming cards for one turn, so that every player, play the same amount of programming cards
- draw my programming cards from the shuffled discard pile, when a programming phase begins

## should have

the game should have the following features:

- us14 - receive damage
- us15 - my robot to shoot a laser forward, after each move, so that i'm able to hit and damage other players robots
- us19 - play a damage card in my programming phase, so that i get rid of damage card after the turn
- us22 - be standing on a checkpoint at the end of a register, so that i get a checkpoint to my collection
- land on a blue conveyor belt, so that my robot moves two spaces forward of the conveyor belt
- land on a green conveyor belt, so that my robot moves one space forward of the conveyor belt
- land on a push panel, so that my robot gets pushed to the next space of the direction the panel is facing
- land on a gear, so that my robot turns 90 degrees in the direction of the arrow on the gear
- get hit by lasers placed on the map, which shoot, and conduct damage if hit, every cycle of the moving phase
- take damage if moving out of bounds
- land in a pit to take damage
- re-spawn where i started the game, when my robot goes out of the borders or it lands in a pit
- visually see, what is happening during a round
- change the order of my programming cards during the programming phase, if i make a mistake, or change my mind
- my non-played programming cards, to end in my discard pile when the programming phase is over

## could have

- start a timer in the activation phase of 30 seconds, when i am done programming, so that the other players run out of time, and gets random programming cards on their empty programming fields. this is easier to implement online than it is to do with one computer

## would be nice to have

- play with energy tokens, so that i can upgrade my robot
- purchase upgrading cards, so that i get an advantage
- play temporary upgrade cards before a turn, and get it removed after the turn is over
- choose from different upgrade cards to buy
- keep my non-temporary upgrading cards, so that they work more rounds
- change upgrading cards, if i have more that the robot can carry
- get one of four damage cards, when i take damage
- if i fall into a pit, fall of the board or activate a worm card, i must reboot my robot and take the following actions
    - take two spam damage cards and place them in my discard pile,
    - cancel my programming
    - discard my programming cards (including damage cards) from register and hand
    - and wait until next turn to program the robot.
    - i can then place my robot on the reboot token that i started the game on and play temporary upgrading cards during activation phase
