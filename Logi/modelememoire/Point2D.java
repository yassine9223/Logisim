package modelememoire;


public class Point2D {
    private double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setCoordonnees(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
