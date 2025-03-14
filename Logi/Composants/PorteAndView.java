import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import modelememoire.PorteAnd;
import modelememoire.State;

public class PorteAndView extends ComposantView implements CircuitComponent, HasLogique {
    private Canvas canvas;
    private PorteAnd modeleLogique;  // L'objet logique associé à la porte AND

    public PorteAndView(double x, double y) {
        super(x, y, 2, 1);
        this.modeleLogique = new PorteAnd(x, y);
        canvas = new Canvas(100, 80);
        getChildren().add(canvas);
        dessinerComposant();
        dessinerPorts();
    }

    @Override
    protected void dessinerComposant() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.TRANSPARENT);
        gc.strokeLine(10, 10, 40, 10);
        gc.strokeLine(10, 70, 40, 70);
        gc.strokeLine(10, 10, 10, 70);
        gc.strokeArc(10, 10, 60, 60, 90, -180, ArcType.OPEN);
    }

    @Override
    protected void dessinerPorts() {
        entrees.clear();
        sorties.clear();
        Circle entree1 = new Circle(5, 10, 5, Color.BLUE);
        Circle entree2 = new Circle(5, 70, 5, Color.BLUE);
        entrees.add(entree1);
        entrees.add(entree2);
        getChildren().addAll(entree1, entree2);
        Circle sortie = new Circle(80, 40, 5, Color.RED);
        sorties.add(sortie);
        getChildren().add(sortie);
    }

    // Cette méthode permet de créer une nouvelle instance identique.
    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        return new PorteAndView(getPosition().getX(), getPosition().getY());
    }
    
    @Override
    public void reset() {
        modeleLogique.reset();
        if (!sorties.isEmpty()) {
            sorties.get(0).setFill(Color.GRAY);
        }
    }
    
    @Override
    public void evaluate() {
        State s = modeleLogique.evaluerSignal();
        if (!sorties.isEmpty()) {
            Circle c = sorties.get(0);
            if (s == State.HIGH) {
                c.setFill(Color.RED);
            } else if (s == State.LOW) {
                c.setFill(Color.BLUE);
            } else {
                c.setFill(Color.GRAY);
            }
        }
    }
    
    @Override
    public void initialize() {
        modeleLogique.reset();
    }
    
    @Override
    public void rotate() {
        // Utilise la méthode héritée qui effectue une rotation par pas de 90°
        rotateIfSelected();
    }
    
    @Override
    public void setPosition(double x, double y) {
        // Met à jour d'abord le modèle logique, puis la vue
        modeleLogique.setCoordonnees(x, y);
        super.setPosition(x, y);
    }
    
    @Override
    public modelememoire.Composant getLogique() {
        return modeleLogique;
    }

    @Override
    public String getName() {
        return modeleLogique.getUID();
    }
}
