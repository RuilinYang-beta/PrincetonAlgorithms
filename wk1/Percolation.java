import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF w;
    private final int dim;
    private final int vTop;
    private final int vBot;
    private boolean[] open;
    private int numOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.w = new WeightedQuickUnionUF(n * n + 2);
        this.dim = n;
        // vTop and vBot's index in the one dimensional w
        this.vTop = n * n;
        this.vBot = n * n + 1;
        // // init open
        this.open = new boolean[n * n];
        // this.open = new int[n * n];
        // for (int i = 0; i < n * n; i++) {
        //     this.open[i] = 0; // all closed initially
        // }
        // init numOfOpenSites
        this.numOfOpenSites = 0;
        // union virtualTop and all the top;
        for (int i = 0; i < this.dim; i++) {
            w.union(i, this.vTop);
        }
        // union virtualBot with all the bottom
        for (int i = n * n - 1; i > n * (n - 1) - 1; i--) {
            w.union(i, this.vBot);
        }
    }

    /**
     * input:  (1, 1) is the upper-left site; (n, n) is the bottom-right site
     * return: 0 is the first site; n*n is the virtualTop; n*n+1 is virtualBot
     */
    private int coordTranslate(int row, int col) {
        return (row - 1) * this.dim + col - 1;
    }

    private boolean illegalRange(int n) {
        return (n < 1) || (n > this.dim);
    }

    private boolean illegalRange(int row, int col) {
        return illegalRange(row) || illegalRange(col);
    }

    private void checkIllegalRange(int row, int col) {
        if (illegalRange(row, col)) {
            throw new IllegalArgumentException();
        }
    }

    private void tryUnion(int row1, int col1, int internalIdx) {
        // (row1, col1) is neighbor of the focus (row, col)
        if (!illegalRange(row1, col1) && isOpen(row1, col1)) {
            w.union(coordTranslate(row1, col1), internalIdx);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkIllegalRange(row, col);
        // do open
        int oneDIdx = coordTranslate(row, col);
        if (!this.open[oneDIdx]) {
            this.open[oneDIdx] = true;
            this.numOfOpenSites += 1;
            // do union with opened neighbors
            tryUnion(row - 1, col, oneDIdx);   // north
            tryUnion(row + 1, col, oneDIdx);   // south
            tryUnion(row, col - 1, oneDIdx);   // west
            tryUnion(row, col + 1, oneDIdx);   // east
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIllegalRange(row, col);
        int internalIdx = coordTranslate(row, col);
        return this.open[internalIdx];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkIllegalRange(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        int internalIdx = coordTranslate(row, col);
        return w.find(internalIdx) == w.find(vTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return w.find(vTop) == w.find(vBot);
    }

    // test client (optional)
    public static void main(String[] args) {

        // Percolation p = new Percolation(3);

        // p.open(1, 1);
        // p.open(1, 2);
        // p.open(1, 3);
        // p.open(2, 3);
        // p.open(3, 3);

        // for (int i = 0; i < 9; i++) {
        //     System.out.println(
        //             String.format("i: %d; (x, y): (%d, %d)",
        //                           i, p.getX(i), p.getY(i)));
        // }
        //
        // System.out.println(2 / 3);

        // System.out.println(p.percolates());
        // System.out.println();
        //        System.out.println(Arrays.toString(p.arr));
        //        System.out.println(Arrays.toString(p.open));


    }
}
