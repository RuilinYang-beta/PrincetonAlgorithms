import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segList;

    public BruteCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null) || containRepeatedPoints(points)) {
            throw new IllegalArgumentException();
        }

        segList = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slopeIJ = points[i].slopeTo(points[j]);
                for (int p = j + 1; p < points.length; p++) {
                    if (points[i].slopeTo(points[p]) == slopeIJ) {
                        for (int q = p + 1; q < points.length; q++) {
                            if (points[i].slopeTo(points[q]) == slopeIJ) {
                                segList.add(findLineSegment(points, Arrays.asList(i, j, p, q)));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean containRepeatedPoints(Point[] points) {
        // copy and sort the copy, so that the original array is not mutated
        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, points.length);
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * Find the two points that are farthest from each other, and use them to construct a LineSegment object.
     *
     * @param idxList: for every two ele in idxList, points[ele1] is on the same with points[ele2]
     */
    private LineSegment findLineSegment(Point[] points, List<Integer> idxList) {
        // assume the 4 points that are one the same line does not all have exactly the same coordinates
        // that is, there are at least 2 points out of 4 that are not overlapping
        // so the 4 points can definitely make a line

        Point maxPoint = points[idxList.get(0)];
        Point minPoint = points[idxList.get(0)];

        for (int i = 1; i < idxList.size(); i++) {
            if (maxPoint.compareTo(points[idxList.get(i)]) < 0) {
                maxPoint = points[idxList.get(i)];
            }
            if (minPoint.compareTo(points[idxList.get(i)]) > 0) {
                minPoint = points[idxList.get(i)];
            }
        }

        return new LineSegment(minPoint, maxPoint);
    }

    public int numberOfSegments() {         // the number of line segments
        return segList.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segArr = new LineSegment[segList.size()];
        for (int i = 0; i < segList.size(); i++) {
            segArr[i] = segList.get(i);
        }

        return segArr;
    }
}
