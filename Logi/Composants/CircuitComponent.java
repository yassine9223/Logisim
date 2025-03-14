public interface CircuitComponent extends Cloneable {
    String getName();
    Position getPosition();
    void setPosition(double x, double y);
    void rotate();
    void initialize();
    void evaluate();
    void reset();
    public abstract CircuitComponent clone() throws CloneNotSupportedException;
}