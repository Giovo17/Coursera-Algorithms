import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board{

    private int[][] tiles;
    private int n;
    private Stack<Board> neighborList;
    private Board twin;
    private int manhattan;
    private int hamming;
    private String str = " ";
    

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        n = tiles.length;
        this.tiles = new int[n][n];

        for(int i=0; i<n; i++)
            for(int k=0; k<n; k++)
                this.tiles[i][k] = tiles[i][k];

        hamming = -1;
        manhattan = -1;
        
    }
                                           
    // string representation of this board
    public String toString(){
        if(str.equals(" ")){
            StringBuffer buff = new StringBuffer(Integer.toString(n));
            buff.append("\n");

            for(int i=0; i<n; i++){
                for(int k=0; k<n; k++){
                    if(tiles[i][k]/10 == 0){
                        buff.append("  "); buff.append(Integer.toString(tiles[i][k]));
                    }
                    else { buff.append(" "); buff.append(Integer.toString(tiles[i][k])); }
                    
                }
                
                buff.append("\n");
            }

            str = buff.toString();
        }
         
        return str;
    }

    // board dimension n
    public int dimension() { return n; }

    // number of tiles out of place
    public int hamming(){
        if(hamming == -1){
            hamming = 0;
            int tmp = 1;
            for(int i=0; i<n; i++)
                for(int k=0; k<n; k++){
                    if(i == n-1 && k == n-1){
                        break;
                    }
                    else if(tiles[i][k] != tmp++)
                        hamming++;      
                }
        }
        
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        if(manhattan == -1){
            int x, y;
            manhattan = 0;

            for(int j=0; j<n; j++)
                for(int i=0; i<n; i++){
                    if(tiles[j][i] == 0){
                        continue;
                    }
                    else if(tiles[j][i] % n == 0){
                        y = tiles[j][i] / n - 1;
                        x = n - 1;
                    }
                    else{
                        y = tiles[j][i] / n;
                        x = tiles[j][i] % n - 1;
                    }
                
                    manhattan += (Math.abs(x - i) + Math.abs(y - j));
                }
        }
        
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() { return hamming() == 0; }

    // does this board equal y?
    public boolean equals(Object y){
        if(y == null) return false;
        if(!(y instanceof Board)) return false;

        if(n == ((Board) y ).dimension()){
            
            for(int i=0; i<n; i++)
                for(int k=0; k<n; k++)
                    if(tiles[i][k] != ((Board) y).tiles[i][k])
                        return false;
            
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){

        if(neighborList == null){
            neighborList = new Stack<Board>();
            boolean[] hasNeigh = new boolean[4];
            
            int tiles2[][] = new int[n][n];

            for(int i=0; i<n; i++)
                for(int k=0; k<n; k++)
                    tiles2[i][k] = tiles[i][k];

            for(int i=0; i<hasNeigh.length; i++)
                hasNeigh[i] = true;
        

            for(int i=0; i<n; i++)
                for(int k=0; k<n; k++)
                    if(tiles[i][k] == 0){
                        if(i == 0)
                            hasNeigh[0] = false;    
                        if(i == n-1)
                            hasNeigh[2] = false;
                        if(k == 0)
                            hasNeigh[3] = false;    
                        if(k == n-1)
                            hasNeigh[1] = false;                    
                    
                        if(hasNeigh[0]){ // top element
                            // exchange
                            tiles2[i][k] = tiles2[i-1][k];
                            tiles2[i-1][k] = 0;
                            neighborList.push(new Board(tiles2));  // add in list
                            // reset array
                            tiles2[i-1][k] = tiles2[i][k];
                            tiles2[i][k] = 0;
                        }

                        if(hasNeigh[1]){ // right element
                            // exchange
                            tiles2[i][k] = tiles2[i][k+1];
                            tiles2[i][k+1] = 0;
                            neighborList.push(new Board(tiles2));  // add in list
                            // reset array
                            tiles2[i][k+1] = tiles2[i][k];
                            tiles2[i][k] = 0;
                        
                        }

                        if(hasNeigh[2]){ // bottom element
                            // exchange
                            tiles2[i][k] = tiles2[i+1][k];
                            tiles2[i+1][k] = 0;
                            neighborList.push(new Board(tiles2));  // add in list
                            // reset array
                            tiles2[i+1][k] = tiles2[i][k];
                            tiles2[i][k] = 0;
                        
                        }

                        if(hasNeigh[3]){ // left element
                            // exchange
                            tiles2[i][k] = tiles2[i][k-1];
                            tiles2[i][k-1] = 0;
                            neighborList.push(new Board(tiles2));  // add in list
                        }

                        break;
                    }
            return neighborList;
        }

        return neighborList;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        if(twin == null)
        {
            int tiles2[][] = new int[n][n];
            int tmp;

            for(int i=0; i<n; i++)
                for(int k=0; k<n; k++)
                    tiles2[i][k] = tiles[i][k];
        
            if(tiles2[0][0] != 0){
                if(tiles2[0][1] != 0){
                    tmp = tiles2[0][0];
                    tiles2[0][0] = tiles2[0][1];
                    tiles2[0][1] = tmp;
                }
                else{
                    tmp = tiles2[0][0];
                    tiles2[0][0] = tiles2[1][0];
                    tiles2[1][0] = tmp;
                }
            }
            else{
                tmp = tiles2[0][1];
                tiles2[0][1] = tiles2[1][0];
                tiles2[1][0] = tmp;
            }

            twin = new Board(tiles2);

            return twin;
        }

        return twin;
        
    }

    // unit testing (not graded)
    public static void main(String[] args){
        
    }

}