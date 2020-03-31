public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Point reflect(Point v, Point n) {
        double dot = Point.dot(v, n);
        return new Point(v.x - 2.0f * dot * n.x, v.y - 2.0f * dot * n.y);
    }

    public double length() {
        return (Math.sqrt(x*x + y*y));
    }

    public static Point normalize(Point p) {
        double length = p.length();
        p.x /= length;
        p.y /= length;
        return p;
    }
}
