# N-Puzzle

## The problem

The [N-puzzle](https://en.wikipedia.org/wiki/15_puzzle) problem is a puzzle invented and popularized by Noyes Palmer Chapman in the 1870s. It is played on a N-by-N grid with N square blocks labeled 1 through N and a blank square. Your goal is to rearrange the blocks so that they are in order, using as few moves as possible. You are permitted to slide blocks horizontally or vertically into the blank square. The following shows a sequence of legal moves from an initial board (left) to the goal board (right). 

![image][1]

## Best-first search

Now, we describe a solution to the problem that illustrates a general artificial intelligence methodology known as the [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm). We define a search node of the game to be a board, the number of moves made to reach the board, and the previous search node. First, insert the initial search node (the initial board, 0 moves, and a null previous search node) into a priority queue. Then, delete from the priority queue the search node with the minimum priority, and insert onto the priority queue all neighboring search nodes (those that can be reached in one move from the dequeued search node). Repeat this procedure until the search node dequeued corresponds to a goal board. The success of this approach hinges on the choice of priority function for a search node. We consider two priority functions:

* Hamming priority function. The number of blocks in the wrong position, plus the number of moves made so far to get to the search node. Intuitively, a search node with a small number of blocks in the wrong position is close to the goal, and we prefer a search node that have been reached using a small number of moves.

* Manhattan priority function. The sum of the Manhattan distances (sum of the vertical and horizontal distance) from the blocks to their goal positions, plus the number of moves made so far to get to the search node. 

For example, the Hamming and Manhattan priorities of the initial search node below are 5 and 10, respectively. 

![image][2]

We make a key observation: To solve the puzzle from a given search node on the priority queue, the total number of moves we need to make (including those already made) is at least its priority, using either the Hamming or Manhattan priority function. (For Hamming priority, this is true because each block that is out of place must move at least once to reach its goal position. For Manhattan priority, this is true because each block must move its Manhattan distance from its goal position. Note that we do not count the blank square when computing the Hamming or Manhattan priorities.) Consequently, when the goal board is dequeued, we have discovered not only a sequence of moves from the initial board to the goal board, but one that makes the fewest number of moves.

## Game tree

![image][3]


[1]: https://github.com/angeldev/Algorithms/blob/master/N-Puzzle/images/N-Puzzle%20legal%20moves.png "Legal Moves"
[2]: https://github.com/angeldev/Algorithms/blob/master/N-Puzzle/images/N-Puzzle%20priority.png "Priority"
[3]: https://github.com/angeldev/Algorithms/blob/master/N-Puzzle/images/N-Puzzle%20Game%20tree.png "Game Tree"
