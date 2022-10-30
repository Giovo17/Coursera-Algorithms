import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;  // to manage the connections
    private final int n;
    private final int dim;
    private int[] vect;   // to manage if a site is blocked, open or full open
    private int numberOfOpenSites;
    private int tmp;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        this.n = n;
        dim = n * n;

        uf = new WeightedQuickUnionUF(dim + 2);
        // union the first row with the dim element
        for (int i = 0; i < n; i++) {
            uf.union(dim, i);
        }

        vect = new int[dim];
        for (int i = 0; i < dim; i++) {
            vect[i] = 0;  // 0 = blocked, 1 = open, 2 = full
        }
        numberOfOpenSites = 0;
    }

    private void markFullOpen(int row, int col) {
        int c = ((col + n * (row - 1)) - 1);
        vect[c] = 2;

        if (col != 1)  // if it isn't in the first col
            if (vect[c - 1] == 1) {  // left element
                vect[c - 1] = 2;
                markFullOpen(row, col - 1);
            }

        if (col != n)  // if it isn't in the last col
            if (vect[c + 1] == 1) {  // right element
                vect[c + 1] = 2;
                markFullOpen(row, col + 1);
            }


        if (row != 1)  // if it isn't in the first row
            if (vect[c - n] == 1) {  // upper element
                vect[c - n] = 2;
                markFullOpen(row - 1, col);
            }


        if (row != n)  // if it isn't in the last row
            if (vect[c + n] == 1) {  // lower element
                vect[c + n] = 2;
                markFullOpen(row + 1, col);
            }

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException(
                    "Bad Request! Could not open a site outside ranges!");

        if (!isOpen(row, col)) {
            tmp = ((col + n * (row - 1)) - 1);

            vect[tmp] = 1; // open the site

            numberOfOpenSites++;

            if (row == n)   // connected the element with the bottom virtual node
                uf.union(dim + 1, tmp);

            if (row != 1)  // if it isn't in the first row
                if (isOpen(row - 1, col))
                    // connect to the upper site if it is open
                    uf.union(tmp, ((col + n * (row - 2)) - 1));

            if (row != n)  // if it isn't in the last row
                if (isOpen(row + 1, col))
                    // connect to the lower site if it is open
                    uf.union(tmp, ((col + n * row) - 1));

            if (col != 1)  // if it isn't in the first col
                if (isOpen(row, col - 1))
                    // connect to the site on the left if it is open
                    uf.union(tmp, (col + n * (row - 1)) - 2);

            if (col != n)  // if it isn't in the last col
                if (isOpen(row, col + 1))
                    // connect to the site on the right if it is open
                    uf.union(tmp, (col + n * (row - 1)));

            //---------------------------------------------------------

            // if the site is connected to the node dim (which is connected to the first row), the site become full open and all the sites linked to this one too
            if (uf.find(dim) == uf.find(tmp)) {  // if connected with the top virtual node
                markFullOpen(row, col);
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException(
                    "Bad Request! Could not open a site outside ranges!");

        // int c = linearizedCoordinates(row, col);
        return !(vect[((col + n * (row - 1)) - 1)] == 0);
    }

    // is the site (row, col) full?  //element connected to the 0 element
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException(
                    "Bad Request! Could not open a site outside ranges!");

        // int c = linearizedCoordinates(row, col);
        return vect[((col + n * (row - 1)) - 1)] == 2;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {  // check if the dim element (which is linked to the first row) is linked with the dim+1 element (which is linked to the last row)
        return uf.find(dim) == uf.find(dim + 1);
    }


    // test client (optional)
    public static void main(String[] args) {

    }
}
