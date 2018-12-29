# Awari
This project implemented the Alpha-Beta pruning algorithm to play a two-player game called Awari.
 
## RULES
Awari is an Ashanti abstract strategy game in the Mancala family of board games (pit and pebble games) played worldwide. Here we will use slightly modified Abapa rules set out by Awari International for tournaments. They are as follows:
__The objective__ is to win stones by placing your last stone being moved into a pit of your opponent that already contains one or two stones, making the final count two or three, and then capturing and placing these stones into your storehouse.

#### Board and initial setup
The board is comprised of twelve pits. Each side belongs to one player. The numbers in each pit represents the number of stones in that pit.
<div align= "center">
<img src="./images/1.jpg" />
</div>

__The direction of play__ is counterclockwise, moving left to right around the board in a circle. __Each player has six pits__ on one side of the board and a storehouse to their right, which is for storing stones won during the game. __To start the game__, place a constant number (usually 4) stones into each player’s pits, and none in the storehouse. 

### Rule 1: MOVING
On their turn, the player chooses any pit on their side, removes all the stones in that pit, and, starting at the next pit to the right moving counterclockwise, puts one stone into each pit that comes next without skipping any pits, except the pit moved from and the storehouses. For example:

__Before__ → __After__

<img src="./images/2.jpg" width = "430" /> <img src="./images/3.jpg" width = "430" />

### Rule 2: WINNING STONES IN ONE PIT
If a player places their last stone into a pit on the opponent’s side that makes the new count in that pit either 2 or 3 stones, the player wins these stones and places them in their storehouse. For example: 

__Before move__ → __After move, before capture__ → __After capture__

<img src="./images/4.jpg" width = "290" /> <img src="./images/5.jpg" width = "290" /> <img src="./images/6.jpg" width = "290" />

### Rule 3: WINNING STONES IN MULTIPLE PITS
If the last stone does capture the pit’s stones, then the previous pit is checked. If that pit, too, has a count of 2 or 3 stones, then those stones are also placed into the current player’s storehouse, and the pit previous to it is checked next. This capturing continues until a checked pit cannot be captured, or it resides outside your opponent’s set of pits.

__Important Note__: __No capture is possible__ if it would leave an opponent with no stones in any of their pits. __That is, you cannot entirely wipe-out an opponent in one move__ (known as a grand-slam). The move is allowed, but no capture will occur.

### Rule 4: SKIPPING PITS
If a player chooses a pit containing 12 or more stones, the player will skip the pit they started from and continue to the next pit. For example:

__Before move__ → __After move__

<img src="./images/7.jpg" width = "430" /> <img src="./images/8.jpg" width = "430" />

### Rule 5: MANDATORY MOVE
If any player has six empty pits, the next player must move at least 1 stone into the pits of the empty player if they have any pits with enough stones to reach an empty pit. If not, the game ends and the remainder of the stones are forfeit. The player with the largest number of stones in their storehouse is the winner.

__The game also ends__ when, at any time, a player has captured a majority of the stones, which in the standard game is 25 of the 48 total stones.


## The Awari-Playing Agent
This project handles all of the game board data structures, rule aspects of the game, and provides a GUI interface for practicing. In this framework there is an abstract class defined in the file `Player.java`, which defines all the methods necessary for an agent to interface with the game framework. Use this command to run the game:

    % javac *.java 
    % java Awari <Player1Class> <Player2Class> <board.txt> [maxDepth]

The first two arguments are the playing agents’ class names, the third argument is a text file for board setup, and the last argument is the maximum search depth for the AI (it is not required when both players are human). This program has provided a human player class, `HumanPlayer`, which takes input from the GUI interface.

The first line in the setup file defines the current scores for players 1 and 2 respectively, and the next two lines define the number of stones in each of the pits. For example, the setup file for the default initial board state is:

    0 0 
    4 4 4 4 4 4 
    4 4 4 4 4 4 

Note we may use different board states in testing. When the game is run with no parameters, it is a two-human-player practice game with the default initial board setup.

## studentAI.java
The `studentAI.java` assume the AI to be player 1. This file will extend the abstract `Player` class. It implemented:
  1. Minimax search with alpha-beta pruning
  2. Cut-off search at a fixed depth limit
  3. A static board evaluation (SBE) function

Specifically, it implemented five functions:
```Java
  1. public void move (BoardState state); 
  2. public int alphabetaSearch(BoardState state, int maxDepth);
  3. public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta);
  4. public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta);
  5. private int sbe(BoardState state);
```
More details on these five functions are given below.
```Java
1. public void move (BoardState state);
```
This is a wrapper function for alpha-beta search. It should use alpha-beta search to update the data member move (which will be returned by the getMove() method to the Match class that is controlling the game environment). Since the whole search space for Awari is extremely large, it will cut off the search at some fixed depth limit, which is specified by the maxDepth class member. There is a 10-second time limit to calculate each move.
```Java
2. public int alphabetaSearch(BoardState state, int maxDepth);
```
This function will start the alpha-beta search. The detailed descriptions of input and output are given below:
@param state The board state for the current player (the MAX player). The pits for the current player are always in the lower row, and that the lower row is player 1.
@param maxDepth The maximum search depth allowed.
@return Return the best move that leads to the state that gives the maximum SBE value for the current player; returns the move with the smallest index in the case of ties. The value of the move should be in the range [0, 5], with 0 representing the leftmost pit.
```Java
3. public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta);
```
This function will search for the minimax value associated with the best move for the MAX player. The search should be cut off when the current depth equals to the maximum allowed depth. It is important to note that it will also call the SBE function to evaluate the game state when the game is over, i.e., when someone has won the game. The only condition for determining a leaf node (besides having reached maximin depth) is that there are no legal moves for the player to make, effectively ending the game at that state. The detailed descriptions of input and output are:

`@param state` The game state that the MAX player is currently searching from

`@param maxDepth` The maximum search depth allowed

`@param currentDepth` The current depth in the search tree

`@param alpha` The α value

`@param beta` The β value

`@return` The minimax value corresponding to the best move for the MAX player
```Java
4. public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta);
```
This function is similar to maxValue except this function returns the best value for the MIN player.
```Java
5. private int sbe(BoardState state);
```
This function takes a board state as input and returns its SBE value. Use the following method: Return the number of stones in the storehouse of the current player minus the number of stones in the opponent’s storehouse. Always assume the current player is player 1.

## BoardState class
The following public members and methods are important:
```Java
1. int[] score
```
This is an array with two indices. `score[0]` contains the number of stones in player 1’s storehouse, and `score[2]` contains the number of stones in player 2’s storehouse.
```Java
2. boolean isLegalMove(int player, int move)
```
This method _returns_ a Boolean that indicates whether or not a proposed move is legal. 
The `move` parameter is the proposed pit from which to move stones. It must be an integer between 0 and 5 inclusive, with each index representing one of the six pits you can start a move from, from left to right on the bottom row. The `player` parameter indicates the player who is proposing to make that move. It must be either 1 or 2.
```Java
3. BoardState applyMove(int player, int move)
```
This method similar to `isLegalMove` except that it returns a new `boardState` with a move applied. It does NOT check if the proposed move is legal before performing it.
```Java
4. String toString()
```
This method will print out the current board, with sides labeled. This method is to test the code.
