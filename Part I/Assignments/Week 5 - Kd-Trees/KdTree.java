import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class KdTree {

    private static final boolean RED = true;
    private static final boolean BLUE = false;

    private TwoDTree tree;
    private Stack<Point2D> stack;
    private Point2D closest;
    private double distance;


    // construct an empty set of points
    public KdTree() {
        tree = new TwoDTree();
    }

    private class TwoDTree {
        private Node root;
        private boolean tmpInsert, tmpGet;
        private double upper = 1.0, lower = 0.0;


        private class Node {
            public Double key;
            public Point2D val;
            public Node left, right;
            public int size;
            public boolean color;
            public double lowerLimit;
            public double upperLimit;

            public Node(Double key, Point2D val, int size, boolean color, double lowerLimit, double upperLimit) {
                this.key = key;
                this.val = val;
                this.size = size;
                this.color = color;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
            }
        }

        public TwoDTree() {
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public int size() {
            return size(root);
        }

        private int size(Node x) {
            if (x == null) return 0;
            else return x.size;
        }

        public boolean contains(Point2D p) {
            if (p == null) throw new IllegalArgumentException("argument to contains() is null");
            return get(p) != null;
        }

        public Point2D get(Point2D p) {
            tmpGet = true;
            return get(root, null, p);
        }

        private Point2D get(Node x, Double key, Point2D p) {
            if (p == null)
                throw new IllegalArgumentException("calls get() with a null key");
            if (x == null)
                return null;

            double cmp;
            if (tmpGet) cmp = p.x() - x.key;
            else cmp = p.y() - x.key;


            if (cmp < 0) {
                tmpGet = !tmpGet;
                return get(x.left, key, p);
            } else if (cmp > 0) {
                tmpGet = !tmpGet;
                return get(x.right, key, p);
            } else {
                if (x.val.x() == p.x() && x.val.y() == p.y())
                    return x.val;
                else {
                    if (x.color == RED) {
                        if (x.val.y() > p.y()) {
                            tmpGet = !tmpGet;
                            return get(x.left, key, p);
                        } else {
                            tmpGet = !tmpGet;
                            return get(x.right, key, p);
                        }
                    } else {
                        if (x.val.x() > p.x()) {
                            tmpGet = !tmpGet;
                            return get(x.left, key, p);
                        } else {
                            tmpGet = !tmpGet;
                            return get(x.right, key, p);
                        }
                    }
                }
            }

        }

        public void put(Double key, Point2D val) {
            if (val == null) throw new IllegalArgumentException("calls put() with a null key");

            tmpInsert = true;
            lower = 0.0;
            upper = 1.0;
            root = put(root, key, val);
        }

        private Node put(Node x, Double key, Point2D val) {
            if (x == null) {
                if (tmpInsert) return new Node(val.x(), val, 1, tmpInsert, lower, upper);
                return new Node(val.y(), val, 1, tmpInsert, lower, upper);
            }
            double cmp;
            if (tmpInsert) cmp = val.x() - x.key;
            else cmp = val.y() - x.key;

            if (cmp < 0) {
                if (tmpInsert == RED) {
                    upper = x.val.x();
                } else if (tmpInsert == BLUE) {
                    upper = x.val.y();
                }
                tmpInsert = !tmpInsert;

                x.left = put(x.left, key, val);
            } else if (cmp > 0) {
                if (tmpInsert == RED) {
                    lower = x.val.x();
                } else if (tmpInsert == BLUE) {
                    lower = x.val.y();
                }
                tmpInsert = !tmpInsert;

                x.right = put(x.right, key, val);
            } else {
                if (x.val.x() == val.x() && x.val.y() == val.y())
                    x.val = val;
                else {
                    if (x.color == RED) {
                        if (x.val.y() > val.y()) {
                            tmpInsert = !tmpInsert;
                            x.left = put(x.left, key, val);
                        } else {
                            tmpInsert = !tmpInsert;
                            x.right = put(x.right, key, val);
                        }
                    } else if (x.color == BLUE) {
                        if (x.val.x() > val.x()) {
                            tmpInsert = !tmpInsert;
                            x.left = put(x.left, key, val);
                        } else {
                            tmpInsert = !tmpInsert;
                            x.right = put(x.right, key, val);
                        }
                    }

                }
            }

            x.size = 1 + size(x.left) + size(x.right);
            return x;
        }

        public void draw() {
            draw(root);
        }


        private void draw(Node x) {
            /*
            if (x == null) return;
            x.val.draw();
            if (x.color == RED) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.line(x.val.x(), x.lowerLimit, x.val.x(), x.upperLimit);
            }
            if (x.color == BLUE) {
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.line(x.lowerLimit, x.val.y(), x.upperLimit, x.val.y());
            }

            draw(x.left);
            draw(x.right);

             */
        }

        public Iterable<Point2D> range(RectHV rect) {
            Point2D hi = new Point2D(rect.xmax(), rect.ymax());
            Point2D lo = new Point2D(rect.xmin(), rect.ymin());
            stack = new Stack<Point2D>();

            range(root, stack, hi, lo);

            return stack;
        }

        private void range(Node x, Stack<Point2D> stack, Point2D hi, Point2D lo) {
            if (x == null) return;
            if (x.val.x() >= lo.x() && x.val.x() <= hi.x() && x.val.y() >= lo.y() && x.val.y() <= hi.y()) { //cointained into the rectangle
                stack.push(x.val);
                range(x.left, stack, hi, lo);
                range(x.right, stack, hi, lo);
            } else {
                if (x.color == RED) {
                    if (x.val.x() > hi.x()) { // rectangle on the left
                        range(x.left, stack, hi, lo);
                    } else if (x.val.x() < lo.x()) { // rectangle on the right
                        range(x.right, stack, hi, lo);
                    } else {
                        range(x.left, stack, hi, lo);
                        range(x.right, stack, hi, lo);
                    }
                } else if (x.color == BLUE) {
                    if (x.val.y() > hi.y()) { // rectangle on the left
                        range(x.left, stack, hi, lo);
                    } else if (x.val.y() < lo.y()) { // rectangle on the right
                        range(x.right, stack, hi, lo);
                    } else {
                        range(x.left, stack, hi, lo);
                        range(x.right, stack, hi, lo);
                    }
                }
            }

        }

        public Point2D nearest(Point2D query) {
            closest = root.val;
            distance = closest.distanceTo(query);
            nearest(root, query);
            return closest;
        }

        private void nearest(Node x, Point2D query) {
            if (x == null) return;
            if (x.val.distanceTo(query) < distance) {
                distance = x.val.distanceTo(query);
                closest = x.val;
            }
            if (x.color == RED) {
                if (query.x() < x.val.x()) {
                    nearest(x.left, query);
                    if (distance > query.distanceTo(new Point2D(x.val.x(), query.y())))
                        nearest(x.right, query);
                } else {
                    nearest(x.right, query);
                    if (distance > query.distanceTo(new Point2D(x.val.x(), query.y())))
                        nearest(x.left, query);
                }
            } else if (x.color == BLUE) {
                if (query.y() < x.val.y()) {
                    nearest(x.left, query);
                    if (distance > query.distanceTo(new Point2D(query.x(), x.val.y())))
                        nearest(x.right, query);
                } else {
                    nearest(x.right, query);
                    if (distance > query.distanceTo(new Point2D(query.x(), x.val.y())))
                        nearest(x.left, query);
                }
            }


        }
    }


    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        tree.put(null, p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        tree.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        return tree.range(rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (tree.isEmpty()) return null;
        return tree.nearest(p);
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree k = new KdTree();

        //k.insert(new Point2D(0.625, 0.4375));
        //k.insert(new Point2D(0.625, 0.0625));

        k.insert(new Point2D(0.5, 0.5));

        StdOut.println(k.contains(new Point2D(0.5, 0.69)));

        StdOut.println(k.size());
    }
}
