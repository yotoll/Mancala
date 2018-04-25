Abigail Centers
Abigail Morrison
April 25, 2018
COP3252 Homework X
Mancala Game



RULES:

    There are two players, one on each side of the
board. There are four tokens in each of the six slots, 
and two stores on either side. Player two's store is 
located at the top of the screen. Player one's store is 
on the bottom.
	Players take turns picking up tokens and placing
them on the board in a counterclockwise direction. A token 
is dropped in each slot as the player moves the tokens. When 
the player reaches their store, they place a token into it. 
If the last token they're holding is placed in the store, the
player gains an additional turn. 
	If there are more tokens left after the player puts it
into their store, they continue onto the opponent's side of 
the board counterclockwise until they run out of tokens. The
player does not put a token into the opponent's store if they
reach it. They continue onto their side of the board until 
they are out of tokens.
	The game is over when either most of the tokens on
the board are captured or all tokens are captured. The user is
allowed to chose how the game will terminate in the options
layout. If the user choses the "play until all tokens captured" 
option, then the game will terminate when a player has run out
of tokens on his/her side of the board. If the "play until most
tokens are captured" option is selected, then the game terminates
once one player has collected more than half of the total number
of tokens on the board. The player who has collected the most tokens 
at the end is the winner.

Capture Mode:
	If a player places their last token in an empty slot on 
their side of the board and there are tokens in the slot opposite
to it on the board, the player collects the tokens in both 
slots and puts them into their store. This does not grant the player 
another turn. Note that the user cannot capture tokens by landing in 
an empty slot that is not on their side of the board. If one player 
runs out of tokens on their side of the board, the game terminates and 
the other player collects any remaining tokens on his/her side of the board.

Avalanche Mode:
	When a token lands in a slot with one or more tokens, the 
player picks up all of the tokens and continues around the board 
until their last token lands into their store or in an empty slot. If
one player runs out of tokens on their side of the board, the
opponent does not collect any remaining tokens as with Capture mode.

INTERFACE:
-Start button opens the Options layout
-Rules lists the rules of mancala and the two different
modes
-Theme radio buttons are for choosing the game background
-Computer radio buttons are for choosing whether the computer
is player 1, player 2, or off
-Capture and Avalanche radio buttons are the two game modes
-Player 1 and Player 2 name fields are for inputting the
names of players
- An error will appear if one or both of these fields are empty 
or if they are equal
- All radio buttons and textfields are set to a default value on
first opening the Options layout
-After options have been selected, start game initializes
a new game
-The central "board" has buttons that correspond to the
tokens in those slots. These are pressed to make a move.
-Once the token is pressed, commit and undo move buttons are
used to either confirm the move or undo the move before
moving to the next turn
-A button will not move tokens on the board if the user has selected
a slot that is not on his/her side of the board or is empty
-End game button ends the game and returns the user to the
start menu
-Play Again button appears after a game is over. Click to restart the game
using the same game settings
-Buttons/slots should not perform an action while waiting for user to select
the play again button



Game logic for playing with computer (in order of precedence):
- Chose randomly between any slots that will land in the computer player's
  store and therefore give the computer another turn
- If playing in capture mode, chose randomly between any slots that will
  capture tokens from the opponent
- Chose randomly between any remaining slots

*Note that there is a slight delay when playing with the computer. This simulates that 
the computer is making a move.


EXTRA:
-2 rule variants: capture mode and avalanche mode
-All backgrounds/images drawn by Abigail Morrison
-Music (music varies for horror theme)

Music sources:
https://www.youtube.com/watch?v=muQm1pQI2dc
http://www.skulduggerypleasant.co.uk/games-downloads/


WORK SEPARATION:
-Abigail Centers worked on game logic, layout transitions, object placements 
on each layout and music
-Abigail Morrison drew the background images, token images,
and worked on menu options, radio buttons and their action listener functions