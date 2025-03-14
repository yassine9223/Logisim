import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import modelememoire.PorteNand;
import modelememoire.State;

public class PorteNandView extends ComposantView implements CircuitComponent, HasLogique {
    private Canvas canvas;
    private PorteNand modeleLogique;  // L'objet logique associé à la porte NAND

    public PorteNandView(double x, double y) {
        super(x, y, 2, 1);
        this.modeleLogique = new PorteNand(x, y);
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
    
        // Dessin de la porte NAND : arcs et lignes pour représenter une porte logique
        gc.strokeArc(10, 10, 40, 60, -90, 180, ArcType.OPEN);
        gc.strokeLine(35, 10, 65, 10);
        gc.strokeLine(35, 70, 65, 70);
        gc.strokeArc(30, 10, 60, 60, 90, -180, ArcType.OPEN);
    
        // Petit cercle de négation en sortie
        double negationX = 85;
        double negationY = 35;
        double negationRadius = 5;
        gc.strokeOval(negationX, negationY, negationRadius * 2, negationRadius * 2);
    }
    
    @Override
    protected void dessinerPorts() {
        // On vide le contenu pour redessiner correctement
        getChildren().clear();
        getChildren().add(canvas);
        entrees.clear();
        sorties.clear();
    
        // Création des ports d'entrée en bleu
        Circle entree1 = new Circle(35, 10, 5, Color.BLUE);
        Circle entree2 = new Circle(35, 70, 5, Color.BLUE);
        entrees.add(entree1);
        entrees.add(entree2);
        getChildren().addAll(entree1, entree2);
    
        // Création du port de sortie en rouge
        Circle sortie = new Circle(105, 40, 5, Color.RED);
        sorties.add(sortie);
        getChildren().add(sortie);
    }
    
    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        return new PorteNandView(getPosition().getX(), getPosition().getY());
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
        double nouvelAngle = (getRotate() + 90) % 360;
        setRotate(nouvelAngle);
    }
    
    @Override
    public void setPosition(double x, double y) {
        // Met à jour d'abord le modèle logique puis la vue
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
