// Classe Connection concrète
public class Connection {
    protected Object source;
    protected Object target;

    public Connection(Object source, Object target) {
        this.source = source;
        this.target = target;
    }

    public Object getSource() {
        return source;
    }

    public Object getTarget() {
        return target;
    }

    public void displayConnection() {
        System.out.println("Connection from: " + source + " to " + target);
    }
}