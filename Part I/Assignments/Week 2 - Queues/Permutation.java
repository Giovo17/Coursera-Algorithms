import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args){
        
        int k;
        if (args.length == 1) 
            k = Integer.parseInt(args[0]);
        else
            throw new IllegalArgumentException("Please insert an integer >= 0 as argument");
        
        RandomizedQueue<String> r = new RandomizedQueue<String>();
        int i = 0;
        String s;
        
        // così va all'infinito
        while(!StdIn.isEmpty() || i < k){    
            s = StdIn.readString();
            r.enqueue(s);
            i++;
        }


        i = 0;
        while(!r.isEmpty() && i != k){
            StdOut.println(r.dequeue());
            i++;
        }


    }
}
