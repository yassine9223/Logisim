import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modelememoire.Pin;
import modelememoire.State;

public class PinView extends ComposantView implements CircuitComponent, HasLogique {

    private Canvas canvas;
    private Text valueText;
    private boolean state; // false = LOW, true = HIGH
    private Pin modelePin; // Le modèle logique de la Pin

    public PinView(double x, double y) {
        // Une Pin est considérée comme ayant 0 entrées et 1 sortie
        super(x, y, 0, 1);
        canvas = new Canvas(100, 80);
        getChildren().add(canvas);
        // Instanciation du modèle logique de la Pin
        modelePin = new Pin(x, y);
        // Création d'un affichage textuel pour représenter l'état (0 ou 1)
        valueText = new Text("0");
        valueText.setFont(new Font(40));
        valueText.setFill(Color.BLACK);
        valueText.setX(28);
        valueText.setY(55);
        getChildren().add(valueText);
        dessinerComposant();
        dessinerPorts();
        // Permet de basculer l'état lors d'un clic sur le texte
        valueText.setOnMouseClicked(this::toggleState);
    }

    @Override
    protected void dessinerComposant() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.TRANSPARENT);
        gc.strokeLine(10, 10, 70, 10);
        gc.strokeLine(10, 70, 70, 70);
        gc.strokeLine(10, 10, 10, 70);
        gc.strokeLine(70, 70, 70, 10);
    }

    @Override
    protected void dessinerPorts() {
        entrees.clear();
        sorties.clear();
        // Création d'un port de sortie (rouge)
        Circle sortie = new Circle(80, 40, 5, Color.RED);
        sorties.add(sortie);
        getChildren().add(sortie);
    }

    /**
     * Bascule l'état de la Pin lors d'un clic sur le texte.
     */
    private void toggleState(MouseEvent event) {
        state = !state;
        valueText.setText(state ? "1" : "0");
        // Met à jour l'état dans le modèle logique
        modelePin.setEtat(state ? State.HIGH : State.LOW);
    }

    // Méthodes de l'interface CircuitComponent

    @Override
    public String getName() {
        return modelePin.getUID();
    }

    @Override
    public Position getPosition() {
        return new Position(modelePin.getX(), modelePin.getY());
    }

    @Override
    public void setPosition(double x, double y) {
        modelePin.setCoordonnees(x, y);
        setLayoutX(x);
        setLayoutY(y);
    }

    @Override
    public void rotate() {
        // Pour une Pin, la rotation n'est généralement pas pertinente
        rotateIfSelected();
    }

    @Override
    public void initialize() {
        modelePin.reset();
    }

    @Override
    public void evaluate() {
        // Dans le cas d'une Pin, l'évaluation peut être simplement laissée vide,
        // car c'est l'utilisateur qui détermine son état.
    }

    @Override
    public void reset() {
        modelePin.reset();
        state = false;
        valueText.setText("0");
    }

    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        return new PinView(getPosition().getX(), getPosition().getY());
    }

    @Override
    public modelememoire.Composant getLogique() {
        return modelePin;
    }
}
