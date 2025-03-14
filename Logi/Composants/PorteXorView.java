import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import modelememoire.PorteXor;
import modelememoire.State;

public class PorteXorView extends ComposantView implements CircuitComponent, HasLogique {
    private Canvas canvas;
    private PorteXor modeleLogique;  // L'objet logique associé à la porte XOR

    public PorteXorView(double x, double y) {
        super(x, y, 2, 1);
        // Création du modèle logique pour la porte XOR
        this.modeleLogique = new PorteXor(x, y);
        // Création d'un canvas dédié au dessin du composant
        canvas = new Canvas(100, 80);
        getChildren().add(canvas);
        // Dessiner le composant et ses ports
        dessinerComposant();
        dessinerPorts();
    }

    @Override
    protected void dessinerComposant() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Effacer le canvas pour redessiner proprement
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.TRANSPARENT);
        // Premier arc gauche, similaire à une porte OR
        gc.strokeArc(10, 10, 40, 60, -90, 180, ArcType.OPEN);
        // Deuxième arc gauche pour distinguer le XOR
        gc.strokeArc(5, 10, 40, 60, -90, 180, ArcType.OPEN);
        // Dessin des barres horizontales (top et bottom)
        gc.strokeLine(35, 10, 65, 10);
        gc.strokeLine(35, 70, 65, 70);
        // Arc arrondi à droite pour la sortie
        gc.strokeArc(30, 10, 60, 60, 90, -180, ArcType.OPEN);
    }

    @Override 
    protected void dessinerPorts() {
        // On vide les enfants pour redessiner proprement
        getChildren().clear();
        // On réintègre le canvas
        getChildren().add(canvas);
        entrees.clear();
        sorties.clear();
        // Création des ports d'entrée (en bleu)
        Circle entree1 = new Circle(35, 10, 5, Color.BLUE);
        Circle entree2 = new Circle(35, 70, 5, Color.BLUE);
        entrees.add(entree1);
        entrees.add(entree2);
        getChildren().addAll(entree1, entree2);
        // Création du port de sortie (en rouge)
        Circle sortie = new Circle(95, 40, 5, Color.RED);
        sorties.add(sortie);
        getChildren().add(sortie);
    }

    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        // Retourne une nouvelle instance à la même position
        return new PorteXorView(getPosition().getX(), getPosition().getY());
    }
    
    @Override
    public void reset() {
        // Réinitialise le modèle logique et met à jour la couleur du port de sortie
        modeleLogique.reset();
        if (!sorties.isEmpty()) {
            sorties.get(0).setFill(Color.GRAY);
        }
    }
    
    @Override
    public void evaluate() {
        // Évalue l'état de la porte XOR via le modèle logique et met à jour l'affichage du port de sortie
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
        // Réinitialise le modèle logique
        modeleLogique.reset();
    }
    
    @Override
    public void rotate() {
        // Effectue une rotation par incréments de 90°
        double nouvelAngle = (getRotate() + 90) % 360;
        setRotate(nouvelAngle);
    }
    
    @Override
    public void setPosition(double x, double y) {
        // Met à jour la position dans le modèle logique et la vue
        modeleLogique.setCoordonnees(x, y);
        setLayoutX(x);
        setLayoutY(y);
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
