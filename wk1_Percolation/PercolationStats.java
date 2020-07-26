import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] trialRes;
    private final int dim;
    private final int trials;

    private final double CONFIDENCE_95 = 1.96;

    // perform independent trials on an dim-by-dim grid
    public PercolationStats(int dim, int trials) {
        trialRes = new double[trials];
        this.dim = dim;
        this.trials = trials;
        double n = dim * dim;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(dim);
            while (!p.percolates()) {
                int x = 0;
                int y = 0;
                while (true) {
                    int randInt = StdRandom.uniform(dim * dim);
                    x = getX(randInt);
                    y = getY(randInt);
                    if (!p.isOpen(x, y)) {
                        break;
                    }
                }
                p.open(x, y);
            }
            trialRes[i] = p.numberOfOpenSites() / n;
        }
    }

    private int getX(int i) {
        int x = (i / this.dim) + 1;
        return x;
    }

    private int getY(int i) {
        int y = (i + 1) % this.dim;
        if (y == 0) {
            return this.dim;
        }
        return y;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.trialRes);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.trialRes);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = StdStats.mean(this.trialRes);
        double std = StdStats.stddev(this.trialRes);
        return mean - (CONFIDENCE_95 * std) / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = StdStats.mean(this.trialRes);
        double std = StdStats.stddev(this.trialRes);
        return mean + (CONFIDENCE_95 * std) / Math.sqrt(this.trials);
    }

    // test client (see below)
    public static void main(String[] args) {

        int dim = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        if (dim <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        PercolationStats ps = new PercolationStats(dim, trials);


        System.out.println(String.format("%-26s", "mean")
                                   + String.format("= %.10f", ps.mean()));
        System.out.println(String.format("%-26s", "stddev")
                                   + String.format("= %.10f", ps.stddev()));
        System.out.println(String.format("%-26s", "95% confidence interval")
                                   + String
                .format("= [%.10f, %.10f]", ps.confidenceLo(), ps.confidenceHi()));

    }
}
