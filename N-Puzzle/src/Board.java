/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    none
 *  Dependencies: Stack.java StdRandom.java
 *  
 *  An immutable data type for boards. It is constructed from a N-by-N array 
 *  of blocks. To store the blocks, it uses a char[] array instead of an int[][] 
 *  array to reduce the amount of memory a Board uses. The Board has a N  
 *  dimension and NxN number of blocks, from 0 to N-1
 *
 ******************************************************************************/

import java.util.Arrays;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

    /** An N array of blocks. */
    private char[] blocks;
    
    /** Board dimension. */
    private int N;
    
    /**
     * Construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j).
     *
     * @param  blocks an N-by-N array of blocks
     */
    public Board(int[][] blocks) {
        
        this.N = blocks.length;
        
        char[] tempBlocks = new char[this.N*this.N];
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                tempBlocks[xyTo1D(i, j)] = (char) blocks[i][j];
            }
        }
        
        this.blocks = tempBlocks;
        
    }

    /**
     * Board dimension N.
     *
     * @return  the dimension of the board
     */
    public int dimension() {
        
        return this.N;
        
    }
    
    /**
     * Number of blocks out of place.
     *
     * @return the number of blocks out of position
     */
    public int hamming() {
        
        int hammingLocal = 0;
        for (int i = 1; i < this.blocks.length; i++) {
            if (i != this.blocks[i-1]) {
                hammingLocal++;
            }
        }
        
        return hammingLocal;
        
    }
    
    /**
     * Sum of Manhattan distances between blocks and goal.
     *
     * @return the sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        
        int manhattanLocal = 0;
        
        for (int i = 0; i < this.blocks.length; i++) {
            int block = this.blocks[i];
            if (block != 0) {
                manhattanLocal += this.distanceBetweenBlocks(i, block-1);
            }
        }
        
        return manhattanLocal;
        
    }
    
    /**
     * Returns if this goal is the goal board.
     *
     * @return <tt>true</tt> if the board is the goal board;
     *         <tt>false</tt> otherwise
     */
    public boolean isGoal() {
        
        boolean isGoal = true;
        
        for (int i = 0; i < this.blocks.length-1; i++) {
            if (this.blocks[i] != i+1) {
                return false;
            }
        }
        
        return isGoal;
        
    }
    
    /**
     * A board that is obtained by exchanging any pair of blocks.
     *
     * @return a new board with a pair of blocks exchanged
     */
    public Board twin() {
        
        char[] blocksChar = Arrays.copyOf(this.blocks, this.blocks.length);
        int i = StdRandom.uniform(this.N*this.N);
        int j = StdRandom.uniform(this.N*this.N);
        
        while (blocksChar[i] == 0 || blocksChar[j] == 0 || i == j) {
            i = StdRandom.uniform(this.N*this.N);
            j = StdRandom.uniform(this.N*this.N);
        }
        
        int[][] blocksInt = new int[this.N][this.N];
        for (int k = 0; k < blocksChar.length; k++) {
            blocksInt[k / this.N][k % this.N] = blocksChar[k];
        }
        
        Board boardTwin = new Board(blocksInt);
        boardTwin.swap(i, j); 
        
        return boardTwin;
        
    }
    
    /**
     * Compares this board to the specified board.
     *
     * @param  other the other board
     * @return <tt>true</tt> if this board equals <tt>other</tt>; 
     *         <tt>false</tt> otherwise
     */
    @Override
    public boolean equals(Object y) {
        
        if (y == this) {
            return true;
        }
        
        if (y == null) {
            return false;
        }
        
        if (y.getClass() != this.getClass()) {
            return false;
        }
        
        Board that = (Board) y;
        
        if (this.N != that.N) {
            return false;
        }
        
        for (int i = 0; i < this.blocks.length; i++) {
            if (this.blocks[i] != that.blocks[i]) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns all neighboring boards.
     * 
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
        
        Stack<Board> boards = new Stack<Board>();
        
        int[][] blocksInt = new int[Board.this.N][Board.this.N];
        for (int i = 0; i < Board.this.blocks.length; i++) {
            blocksInt[i / Board.this.N][i % Board.this.N] = Board.this.blocks[i];
        }
        
        int i0 = 0;
        int j0 = 0;
        for (int i = 0; i < blocksInt.length; i++) {
            for (int j = 0; j < blocksInt.length; j++) {
                if (blocksInt[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }
        
        int i = Board.this.xyTo1D(i0, j0);
        
        if (j0+1 < Board.this.N) {
            int j = Board.this.xyTo1D(i0, j0+1);
            Board board = new Board(blocksInt);
            board.swap(i, j); 
            boards.push(board);
        }
        
        if (j0-1 >= 0) {
            int j = Board.this.xyTo1D(i0, j0-1);
            Board board = new Board(blocksInt);
            board.swap(i, j); 
            boards.push(board);
        }
        
        if (i0+1 < Board.this.N) {
            int j = Board.this.xyTo1D(i0+1, j0);
            Board board = new Board(blocksInt);
            board.swap(i, j); 
            boards.push(board);
        }
        
        if (i0-1 >= 0) {
            int j = Board.this.xyTo1D(i0-1, j0);
            Board board = new Board(blocksInt);
            board.swap(i, j); 
            boards.push(board);
        }
        
        return boards;
        
    }
    
    /**
     * String representation of this board.
     *
     * @return a String representing this board
     */
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        //s.append(this.N + "\n");
        for (int i = 0; i < this.N*this.N; i++) {
            s.append(String.format("%2d ", (int) this.blocks[i]));
            
            if ((i+1) % this.N == 0) {
                s.append("\n");
            }
        }
        
        return s.toString();
        
    }
    
    // Maps from a 2-dimensional (row, column) pair to a 1-dimensional
    private int xyTo1D(int i, int j) {
        
        return j + (this.N * i);
        
    }
    
    // Calculates the distance between two blocks
    private int distanceBetweenBlocks(int originBlock, int goalBlock) {
        
        int verticalDist = originBlock / this.N - goalBlock / this.N;
        int horizontalDist = originBlock % this.N - goalBlock % this.N; 
        
        return Math.abs(verticalDist) + Math.abs(horizontalDist);
        
    }
    
    // Swaps two positions of a board
    private void swap(int i, int j) {
        
        char temp = this.blocks[i];
        this.blocks[i] = this.blocks[j];
        this.blocks[j] = temp;
        
    }

}
