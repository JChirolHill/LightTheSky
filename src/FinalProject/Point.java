package FinalProject;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double dist(Point a) {
        return Math.sqrt((a.x - this.x)*(a.x - this.x) + (a.y - this.y)*(a.y - this.y));
    }

    public static Point getVector(Point start, Point end) {
        return new Point(end.x - start.x, end.y - start.y);
    }

    public static double dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }
}
