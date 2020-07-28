import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segList = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {      // finds all line segments containing 4 or more points
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException();
        }

        int pointsCount = points.length;
        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, points.length);

        if (containRepeatedPoints(pointsCopy)) {
            throw new IllegalArgumentException();
        }

        for (Point p : points) {
            Comparator<Point> orderBySlopeToP = p.slopeOrder();
            Arrays.sort(pointsCopy, orderBySlopeToP);

//            StdOut.println("===== in constructor =====");
//            StdOut.println("p is: " + p);
//            StdOut.println("sorted points is: " + Arrays.deepToString(pointsCopy));
//            for (Point other : pointsCopy) {
//                StdOut.println(p.slopeTo(other));
//            }


            int i = 0;
            while (i < pointsCount) {
                double slopePI = p.slopeTo(pointsCopy[i]);
                int j = i + 1;
                while (j < pointsCount && slopePI == p.slopeTo(pointsCopy[j])) {
                    j++;
                }
                // if j - i >=4, then from [i, j-1] are points collinear with p
                // with these points can make a line,
                // if p is the lowest point in the line, then add the line to segList
                if (j - i >= 3) {
                    List<Point> collinearPoints = new ArrayList<>();
                    collinearPoints.add(p);
                    for (int idx = i; idx < j; idx++) {
                        collinearPoints.add(pointsCopy[idx]);
                    }

                    LineSegment ls = findLineSegment(collinearPoints);
                    if (ls != null) {
                        segList.add(ls);
                    }
                }
                i = j;
            }
        }
    }


    /**
     * Find the two points that are farthest from each other within the given index range,
     * if the given point p is the lowest point among them, construct a LineSegment object and return it;
     * otherwise return null.
     *
     * @return a LineSegment object if p is the lowest point; otherwise null.
     */
    private LineSegment findLineSegment(List<Point> collinearPoints) {
        // assume the 4 points that are one the same line does not all have exactly the same coordinates
        // that is, there are at least 2 points out of 4 that are not overlapping
        // so the 4 points can definitely make a line

        // the first element of collinearPoints is p
        // the line segment will only be added if p is the lowest point of all collinear points
        Point maxPoint = collinearPoints.get(0);
        Point minPoint = collinearPoints.get(0);

        for (int i = 1; i < collinearPoints.size(); i++) {
            if (maxPoint.compareTo(collinearPoints.get(i)) < 0) {
                maxPoint = collinearPoints.get(i);
            }
            if (minPoint.compareTo(collinearPoints.get(i)) > 0) {
                return null;
            }
        }

//        StdOut.println("----- in findLineSegment -----");
//        StdOut.println(collinearPoints);
//        StdOut.println("minPoint: " + minPoint + "; p: " + collinearPoints.get(0));

        return new LineSegment(minPoint, maxPoint);
    }


    private boolean containRepeatedPoints(Point[] points) {
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    public int numberOfSegments() {       // the number of line segments
        return segList.size();
    }

    public LineSegment[] segments() {                 // the line segments
        LineSegment[] segArr = new LineSegment[segList.size()];
        for (int i = 0; i < segList.size(); i++) {
            segArr[i] = segList.get(i);
        }

        return segArr;
    }
}
