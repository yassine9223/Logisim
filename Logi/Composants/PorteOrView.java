import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import modelememoire.PorteOr;
import modelememoire.State;

public class PorteOrView extends ComposantView implements CircuitComponent, HasLogique {

    private Canvas canvas;
    private PorteOr modeleLogique;

    public PorteOrView(double x, double y) {
        super(x, y, 2, 1);
        this.modeleLogique = new PorteOr(x, y);
        canvas = new Canvas(100, 80);
        getChildren().add(canvas);
        dessinerComposant();
        dessinerPorts();
    }

    @Override
    protected void dessinerComposant() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 100, 80);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.TRANSPARENT);
        // Dessin d'une porte OR simplifiée :
        gc.strokeArc(10, 10, 40, 60, -90, 180, ArcType.OPEN);  // Arc gauche
        gc.strokeLine(35, 10, 65, 10);  // Barre du haut
        gc.strokeLine(35, 70, 65, 70);  // Barre du bas
        gc.strokeArc(30, 10, 60, 60, 90, -180, ArcType.OPEN);  // Arc à droite
    }

    @Override
    protected void dessinerPorts() {
        entrees.clear();
        sorties.clear();
        // Création des ports d'entrée en bleu
        Circle entree1 = new Circle(35, 10, 5, Color.BLUE);
        Circle entree2 = new Circle(35, 70, 5, Color.BLUE);
        entrees.add(entree1);
        entrees.add(entree2);
        getChildren().addAll(entree1, entree2);
        // Création du port de sortie en rouge
        Circle sortie = new Circle(95, 40, 5, Color.RED);
        sorties.add(sortie);
        getChildren().add(sortie);
    }

    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        return new PorteOrView(getPosition().getX(), getPosition().getY());
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
        System.out.println("Porte OR - État calculé : " + s);
    
        if (!sorties.isEmpty()) {
            Circle c = sorties.get(0);
            System.out.println("🔵 Mise à jour couleur : " + s); // Vérification
            if (s == State.HIGH) {
                c.setFill(Color.RED);
            } else if (s == State.LOW) {
                c.setFill(Color.GRAY);
            } else {
                c.setFill(Color.YELLOW);
            }
        }
    }
    

    @Override
    public void initialize() {
        modeleLogique.reset();
    }

    @Override
    public void rotate() {
        double nouvelAngle = (getRotate() + 90) % 360;
        setRotate(nouvelAngle);
    }

    @Override
    public void setPosition(double x, double y) {
        // Mise à jour du modèle logique et de la vue
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
