import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.Stack;

public class PointSET {

    private SET<Point2D> set;
    private Stack<Point2D> rangeList;


    // construct an empty set of points
    public PointSET() {
        set = new SET<Point2D>();

    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        set.add(p);

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return set.contains(p);

    }

    // draw all points to standard draw
    public void draw() {
        if (!set.isEmpty())
            for (Point2D p : set)
                p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        rangeList = new Stack<Point2D>();

        for (Point2D p : set)
            if (rect.contains(p))
                rangeList.push(p);

        return rangeList;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;

        Point2D nearest = set.min();

        for (Point2D i : set)
            if (p.distanceTo(i) < p.distanceTo(nearest))
                nearest = i;

        return nearest;

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}
