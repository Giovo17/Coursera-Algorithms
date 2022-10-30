import java.util.Comparator;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    
    private Point[] points;
    private int count;
    private LineSegment[] array;
    private Point[] copy;
    private LineSegment tmpLine;
    private boolean token = true;
    
    
    public FastCollinearPoints(Point[] points){     // finds all line segments containing 4 or more points
        if(points == null)
            throw new IllegalArgumentException();
        for(int i=0; i<points.length; i++){
            if(points[i] == null)
                throw new IllegalArgumentException();
            for(int j=i+1; j<points.length; j++) {
                if(points[j] == null)
                    throw new IllegalArgumentException();
                if(points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        this.points = points;
        copy = new Point[points.length];
        System.arraycopy(points, 0, copy, 0, points.length);

        count = 0;
        array = new LineSegment[points.length];
        
        findAllLines();
    }

    // DEBUG
    private void print() { for(Point i: copy) System.out.println(i); }

    private void findAllLines(){
        
        for(int i=0; i<points.length; i++){         
            
            MergeX.sort(copy, points[i].slopeOrder());  // sort points according to the i element

            // System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\t" + points[i]);
            // print();

            for(int k=0; k<copy.length - 2; k++){
                if(equal(points[i], copy[k], copy[k + 1], copy[k + 2])){
                    insertInArray(points[i], copy[k], copy[k + 1], copy[k + 2]);
                    // StdOut.println("Check: " + points[i] + "; " + copy[k]  + "; " + copy[k + 1]  + "; " + copy[k + 2]);
                }
                    
            }

        }

    }

    private boolean equal(Point a, Point b, Point c, Point d){
        //StdOut.println("\tDEBUG: " + a + ", " + b + ", " + c);
        
        if(a.compareTo(b) == 0 || b.compareTo(c) == 0 || a.compareTo(c) == 0 || 
           d.compareTo(a) == 0 || d.compareTo(b) == 0 || d.compareTo(c) == 0)
            return false;
        if(a.slopeTo(b) == b.slopeTo(c) && b.slopeTo(c) == c.slopeTo(d)) 
            return true;
        return false;
    }

    private void insertInArray(Point a, Point b, Point c, Point d){
        
        tmpLine = new LineSegment(searchTheLowest(a, b, c, d), searchTheHighest(a, b, c, d));

        for(int i=0; i<count; i++)
            if(tmpLine.toString().equals(array[i].toString())) 
                token = false;

        if(token) array[count++] = tmpLine;
        
        token = true;
    }

    private Point searchTheLowest(Point a, Point b, Point c, Point d){
        if(a.compareTo(b) < 0){
            if(a.compareTo(c) < 0){
                if(a.compareTo(d) < 0)
                    return a;
                else return d;
            }
            else{
                if(c.compareTo(d) < 0)
                    return c;
                else return d;
            }
        }
        else{
            if(b.compareTo(c) < 0){
                if(b.compareTo(d) < 0)
                    return b;
                else return d;
            }
            else{
                if(c.compareTo(d) < 0)
                    return c;
                else return d;
            }
        }

    } 
    
    private Point searchTheHighest(Point a, Point b, Point c, Point d){
        if(a.compareTo(b) > 0){
            if(a.compareTo(c) > 0){
                if(a.compareTo(d) > 0)
                    return a;
                else return d;
            }
            else{
                if(c.compareTo(d) > 0)
                    return c;
                else return d;
            }
        }
        else{
            if(b.compareTo(c) > 0){
                if(b.compareTo(d) > 0)
                    return b;
                else return d;
            }
            else{
                if(c.compareTo(d) > 0)
                    return c;
                else return d;
            }
        }
    }

    public int numberOfSegments(){  // the number of line segments
        return count;
    }

    public LineSegment[] segments(){  // the line segments    
        LineSegment[] arr = new LineSegment[count];
        
        for(int i=0; i<count; i++)
            arr[i] = array[i];
        
        return arr;
    }

 }
