/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    none
 *  Dependencies: MinPQ.java Stack.java
 *  
 *  This class implements a solution for a N-puzzle 
 *  using the A* search algorithm. It solves the initial
 *  board with the minimum number of movements and provides
 *  the sequence of boards to reach the solution.
 *
 ******************************************************************************/

import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    /** Compares SearchNodes by priority. */
    private static final Comparator<SearchNode> BY_PRIORITY = new ByPriority();
    
    /** A priority queue with the real initial board. */
    private MinPQ<SearchNode> minPQ;
    
    /** A priority queue with a twin initial board. */
    private MinPQ<SearchNode> minPQTwin;
    
    /** Final solved search node. */
    private SearchNode finalSearchNode;
    
    /** Define a search node of the game to be a board, the number of moves
     *  made to reach the board, and the previous search node.  */
    private class SearchNode implements Comparable<SearchNode> {
        
        /** Number of moves to reach this board from the initial. */
        private int moves;
        
        /** The corresponding board of this search node. */
        private Board board;
        
        /** The previous search node of this. */
        private SearchNode prevSearchNode;
        
        /** Manhattan priority of the board. */
        private int manhattan;
        
        SearchNode(int moves, Board board, SearchNode prevSearchNode) {
            
            this.moves = moves;
            this.board = board;
            this.prevSearchNode = prevSearchNode;
            this.manhattan = this.board.manhattan();
            
        }
        
        /** Compares this search node with another. */
        public int compareTo(SearchNode that) {
            
            int thisPriority = this.manhattan + this.moves;
            int thatPriority = that.manhattan + that.moves;
            
            if (thisPriority < thatPriority) {
                return -1;
            } else if (thisPriority > thatPriority) {
                return 1;
            } else {
                return 0;
            }
            
        }
        
    }
    
    /** Compares SearchNodes by priority. */
    private static class ByPriority implements Comparator<SearchNode> {
        
        public int compare(SearchNode v, SearchNode w) {
            
            return v.compareTo(w);
            
        }
        
    }
    
    /**
     * Find a solution to the initial board (using the A* algorithm).
     *
     * @param  initial the initial board
     */
    public Solver(Board initial) {
        
        if (initial == null) {
            throw new NullPointerException();
        }
        
        Board initialTwin = initial.twin();
        
        minPQ = new MinPQ<SearchNode>(BY_PRIORITY);
        minPQTwin = new MinPQ<SearchNode>(BY_PRIORITY);
        
        int moves = 0;
        int movesTwin = 0;
        SearchNode initialSearchNode = new SearchNode(moves, initial, null);
        SearchNode initialSearchNodeTwin = new SearchNode(movesTwin, initialTwin, null);
        minPQ.insert(initialSearchNode);
        minPQTwin.insert(initialSearchNodeTwin);
        
        SearchNode minPrioritySearchNode = minPQ.delMin();
        SearchNode minPrioritySearchNodeTwin = minPQTwin.delMin();
        
        Board neighbor;
        SearchNode neighborSearchNode;
        SearchNode prevSearchNode = null;
        Iterable<Board> iterable;
        Iterator<Board> iterator;
        Iterable<Board> iterableTwin;
        Iterator<Board> iteratorTwin;
        while (!minPrioritySearchNode.board.isGoal() && !minPrioritySearchNodeTwin.board.isGoal()) {
            moves = minPrioritySearchNode.moves + 1;
            movesTwin = minPrioritySearchNodeTwin.moves + 1;
            
            iterable = minPrioritySearchNode.board.neighbors();
            iterator = iterable.iterator();
            while (iterator.hasNext()) {
                neighbor = iterator.next();
                neighborSearchNode = new SearchNode(moves, neighbor, minPrioritySearchNode);
                prevSearchNode = minPrioritySearchNode.prevSearchNode;
                if (prevSearchNode != null) {
                    if (!neighborSearchNode.board.equals(prevSearchNode.board)) {
                        minPQ.insert(neighborSearchNode);
                    }
                } else {
                    minPQ.insert(neighborSearchNode);
                }
            }
            iterableTwin = minPrioritySearchNodeTwin.board.neighbors();
            iteratorTwin = iterableTwin.iterator();
            while (iteratorTwin.hasNext()) {
                neighbor = iteratorTwin.next();
                neighborSearchNode = new SearchNode(movesTwin, neighbor, minPrioritySearchNodeTwin);
                prevSearchNode = minPrioritySearchNodeTwin.prevSearchNode;
                if (prevSearchNode != null) {
                    if (!neighborSearchNode.board.equals(prevSearchNode.board)) {
                        minPQTwin.insert(neighborSearchNode);
                    }
                } else {
                    minPQTwin.insert(neighborSearchNode);
                }
            }
            
            minPrioritySearchNode = minPQ.delMin();
            minPrioritySearchNodeTwin = minPQTwin.delMin();
        }
        
        if (minPrioritySearchNode.board.isGoal()) {
            this.finalSearchNode = minPrioritySearchNode;
        }
        
    }
    
    /**
     * Returns if the initial board is solvable.
     *
     * @return <tt>true</tt> if it is solvable;
     *         <tt>false</tt> otherwise
     */
    public boolean isSolvable() {
        
        return this.finalSearchNode != null;
        
    }
    
    /**
     * Minimum number of moves to solve initial board; -1 if unsolvable.
     *
     * @return number of moves to solve initial board;
     *         <tt>-1</tt> if unsolvable
     */
    public int moves() {
        
        if (this.finalSearchNode == null) {
            return -1;
        }
        
        return this.finalSearchNode.moves;
        
    }
    
    /**
     * Returns a sequence of boards in a shortest solution.
     * 
     * @return a sequence of boards in a shortest solution;
     *         <tt>null</tt> if unsolvable
     */
    public Iterable<Board> solution() {
        
        if (!this.isSolvable()) {
            return null;
        }
        
        Stack<Board> boards = new Stack<Board>();
        
        SearchNode searchNode = Solver.this.finalSearchNode;
        boards.push(searchNode.board);
        searchNode = searchNode.prevSearchNode;
        while (searchNode != null) {
            boards.push(searchNode.board);
            searchNode = searchNode.prevSearchNode;
        }
        
        return boards;
        
    }
    
}
