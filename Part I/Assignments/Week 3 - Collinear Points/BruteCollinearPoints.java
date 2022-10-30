import java.util.Comparator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    
    private Point[] points;
    private int count;
    private LineSegment[] array;
    private boolean token = true;
    private LineSegment tmpLine;


    public BruteCollinearPoints(Point[] points){  // finds all line segments containing 4 points
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
        count = 0;
        array = new LineSegment[points.length];

        findAllLines();
    }

    private void findAllLines(){

        for(int i=0; i<points.length; i++){
            for(int j=i+1; j<points.length; j++)
                for(int k=j+1; k<points.length; k++)
                    if(equal(points[i], points[j], points[k])){
                        for(int f=k+1; f<points.length; f++)
                            if(equal(points[i], points[j], points[f]) && points[f].compareTo(points[k]) != 0){
                                insertInArray(points[i], points[j], points[k], points[f]);
                                //StdOut.println("DEBUG2: " + points[i] + ", " + points[j]  + ", " + points[k] + ", " + points[f]);
                            }
                            
                    }
                    
        }
        
    }

    private boolean equal(Point a, Point b, Point c){
        //StdOut.println("\tDEBUG: " + a + ", " + b + ", " + c);
        
        if(a.compareTo(b) == 0 || b.compareTo(c) == 0 || a.compareTo(c) == 0)
            return false;
        return a.slopeTo(b) == a.slopeTo(c);
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

    private void insertInArray(Point a, Point b, Point c, Point d){
        
        tmpLine = new LineSegment(searchTheLowest(a, b, c, d), searchTheHighest(a, b, c, d));

        for(int i=0; i<count; i++)
            if(tmpLine.toString().equals(array[i].toString())) token = false;
        if(token) array[count++] = tmpLine;
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
