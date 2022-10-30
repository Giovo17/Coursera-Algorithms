import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation perc;
    private final int n;
    private final int dim;
    private final int trials;
    private double[] percThreshold;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n and trails must be > 0");
        this.n = n;
        dim = n * n;
        perc = new Percolation(n);
        this.trials = trials;
        percThreshold = new double[trials];

        simulation();
    }

    private int getRow(int num) {
        return num / n + 1;
    }

    private int getCol(int num) {
        return num % n + 1;
    }

    private void simulation() {
        int rand;
        int row, col;
        for (int i = 0; i < trials; i++) {
            while (!perc.percolates()) {
                rand = StdRandom.uniform(dim);
                row = getRow(rand);
                col = getCol(rand);

                perc.open(row, col);  // opens a random site
            }

            percThreshold[i] = (double) perc.numberOfOpenSites() / (double) dim;

            // reset percolation
            perc = null;
            if (i != trials - 1)
                perc = new Percolation(n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(percThreshold);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stddev = StdStats.stddev(percThreshold);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - (1.96 * stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + (1.96 * stddev()) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {

        int n = 200;
        int t = 100;

        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }

        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException("Illegal Argument Exception");

        PercolationStats percSt = new PercolationStats(n, t);


        StdOut.println("mean                   = " + percSt.mean());
        StdOut.println("stddev                 = " + percSt.stddev());
        StdOut.println("95% confidenc interval = " + "[" + percSt.confidenceLo() + ", " + percSt
                .confidenceHi() + "]");

    }

}
