import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelememoire.Led;
import modelememoire.State;

public class LedView extends ComposantView implements CircuitComponent, HasLogique {

    private Canvas canvas;
    private Led modeleLogique;

    public LedView(double x, double y) {
        // Une LED a 1 port d'entrée et 0 port de sortie
        super(x, y, 1, 0);
        // Création d'un canvas pour dessiner la LED
        canvas = new Canvas(70, 70);
        getChildren().add(canvas);
        // Instanciation du modèle logique de la LED
        this.modeleLogique = new Led(x, y);
        dessinerComposant();
        dessinerPorts();
    }

    @Override
    protected void dessinerComposant() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Couleur par défaut : gris (LED éteinte)
        Color couleurLED = Color.GRAY;
        gc.setFill(couleurLED);
        gc.fillOval(10, 10, 40, 40);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(10, 10, 40, 40);
    }

    @Override
    protected void dessinerPorts() {
        entrees.clear();
        sorties.clear();
        // La LED possède un port d'entrée (en bleu)
        Circle entree = new Circle(5, 30, 5, Color.BLUE);
        entrees.add(entree);
        getChildren().add(entree);
    }

    // Méthodes de l'interface CircuitComponent

    @Override
    public String getName() {
        return modeleLogique.getUID();
    }

    @Override
    public Position getPosition() {
        return new Position(modeleLogique.getX(), modeleLogique.getY());
    }

    @Override
    public void setPosition(double x, double y) {
        // Met à jour le modèle logique et la position de la vue
        modeleLogique.setCoordonnees(x, y);
        setLayoutX(x);
        setLayoutY(y);
    }

    @Override
    public void rotate() {
        // Pour une LED, la rotation n'est pas critique ; on utilise la rotation par défaut
        rotateIfSelected();
    }

    @Override
    public void initialize() {
        modeleLogique.reset();
    }

    @Override
    public void evaluate() {
        State s = modeleLogique.evaluerSignal();
        
        System.out.println("LED - État après évaluation (affichage) : " + s);
    
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (s == State.HIGH) {
            gc.setFill(Color.RED);
        } else if (s == State.LOW) {
            gc.setFill(Color.GRAY);
        } else {
            gc.setFill(Color.YELLOW); // UNDEFINED ou erreur
        }
        gc.fillOval(10, 10, 40, 40);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(10, 10, 40, 40);
    }
    

    @Override
    public void reset() {
        modeleLogique.reset();
        evaluate();
    }

    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        return new LedView(getPosition().getX(), getPosition().getY());
    }

    @Override
    public modelememoire.Composant getLogique() {
        return modeleLogique;
    }
}
