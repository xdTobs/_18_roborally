#!/bin/bash

function prettyJson() {
  echo $1 | python -m json.tool
}

# Create game
gameId=$(curl -s --location 'localhost:8080/game' \
  --header 'Content-Type: application/json' \
  --data '{
  "playerCapacity": 2,
  "boardName": "a-test-board.json",
  "playerName": "player-1"
}
')
responseJoin=$(curl -s --location "localhost:8080/game/$gameId" \
  --header 'roborally-player-name: player-2')

responsePoll=$(curl -s --location "localhost:8080/game/$gameId" \
  --header 'roborally-player-name: player-1')

echo ''
echo "gameId: $gameId"
echo ''

echo 'join'
echo "$responseJoin" | jq '.board.players[] | {name, x ,y, heading, firstCard: .playableCards[0].card.command}'
echo ''

echo 'poll'
echo "$responsePoll" | jq '.board.players[] | {name, x ,y, heading, firstCard: .playableCards[0].card.command}'
echo ''


moveP1=$(curl -s --location "localhost:8080/game/$gameId/moves" \
  --header 'Content-Type: application/json' \
  --header 'roborally-player-name: player-1' \
  --data '[ 'empty', 'empty', 'empty', 'empty', 'empty' ]' 
)

moveP2=$(curl -s --location "localhost:8080/game/$gameId/moves" \
  --header 'Content-Type: application/json' \
  --header 'roborally-player-name: player-2' \
  --data '[ 'empty', 'empty', 'empty', 'empty', 'empty' ]' 
)
echo $moveP1
echo $moveP2

responseAfterMove=$(curl -s --location "localhost:8080/game/$gameId" \
  --header 'roborally-player-name: player-1')

echo "$responseAfterMove" | jq '.board.players[] | {name, x ,y, heading, firstCard: .playableCards[0].card.command}'

