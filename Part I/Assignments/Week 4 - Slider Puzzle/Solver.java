import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;

public class Solver {

    private MinPQ<SearchNode> PQ;
    private MinPQ<SearchNode> TwinPQ;
    private int moves;
    private SearchNode finalNode;
    private Stack<Board> ll;
    private Iterable<Board> neigh, neigh2;
    private boolean chooser;
    private ManhattanComp comp;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null) throw new IllegalArgumentException();

        comp = new ManhattanComp();

        PQ = new MinPQ<SearchNode>(comp);
        PQ.insert(new SearchNode(initial, null));
        
        TwinPQ = new MinPQ<SearchNode>(comp);
        TwinPQ.insert(new SearchNode(initial.twin(), null));  

        moves = 0;
        chooser = true; //true -> PQ, false -> TwinPQ
        
        solve();
    }

    private void solve(){
        SearchNode tmp;
        boolean addable1 = true, addable2 = true;
        int moves2 = 0;

        while(true){
            
            if(chooser){  // processing PQ
                
                tmp = PQ.delMin();
                
                if(tmp.board.isGoal()){
                    finalNode = tmp;
                    moves = 0;

                    ll = new Stack<Board>();
                    ll.push(finalNode.board);
                    
                    
                    while(finalNode.previous != null){
                        moves++;
                        ll.push(finalNode.previous.board);
                        finalNode.previous = finalNode.previous.previous;
                    }

                    break;
                }

                neigh = tmp.board.neighbors();
                for(Board i: neigh){
                    if(tmp.previous != null && i.equals(tmp.previous.board))
                        addable1 = false;

                    if(addable1){
                        if(i.isGoal())  PQ.insert(new SearchNode(i, tmp));
                        else PQ.insert(new SearchNode(i, tmp));
                    }
                    addable1 = true;
                }

                chooser = false;
            }

            else{  // Processing TwinPQ
                
                tmp = TwinPQ.delMin();

                if(tmp.board.isGoal())
                    break;
                    
                moves2++;

                neigh = tmp.board.neighbors();
                for(Board i: neigh){
                    if(moves2 > 1){
                        if(tmp.previous != null && i.equals(tmp.previous.board))
                            addable2 = false;
                        
                        if(addable2){
                            if(i.isGoal())  TwinPQ.insert(new SearchNode(i, tmp));
                            else TwinPQ.insert(new SearchNode(i, tmp));
                        }

                        addable2 = true;
                    }

                    else {
                        if(i.isGoal())  TwinPQ.insert(new SearchNode(i, tmp));
                        else TwinPQ.insert(new SearchNode(i, tmp));
                    }
                
                }
                
                chooser = true;
            }

        }

    }

    private class SearchNode{
        
        public Board board;
        public int moves;
        public SearchNode previous;

        public SearchNode(Board board, SearchNode previous){
            this.board = board;
            this.previous = previous;
            moves = 0;
            while(previous != null){
                moves++;
                previous = previous.previous;
            }
        }

    }

    private class ManhattanComp implements Comparator<SearchNode>{

        public int compare(SearchNode a, SearchNode b){
            
            return (a.board.manhattan() + a.moves - (b.board.manhattan() + b.moves));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() { return chooser; }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if(!isSolvable()) return -1;

        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if(!isSolvable()) return null;

        return ll;
    }

    // test client (see below) 
    public static void main(String[] args){

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
                
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
