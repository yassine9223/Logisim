import javafx.scene.layout.Pane;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public abstract class ComposantView extends Pane implements CircuitComponent {

    protected Point2D position;
    protected int nombreEntrees;
    protected int nombreSorties;
    protected List<Circle> entrees;
    protected List<Circle> sorties;
    protected List<FilView> filsConnectes;
    private Point2D offset;
    private boolean estSelectionne = false;

    public ComposantView(double x, double y, int nombreEntrees, int nombreSorties) {
        this.position = new Point2D(x, y);
        this.nombreEntrees = nombreEntrees;
        this.nombreSorties = nombreSorties;
        setLayoutX(position.getX());
        setLayoutY(position.getY());
        entrees = new ArrayList<>();
        sorties = new ArrayList<>();
        filsConnectes = new ArrayList<>();

        configurerDeplacement();
        configurerSelection();
    }

    protected abstract void dessinerComposant();
    protected abstract void dessinerPorts();

    // Méthode clone() déclarée publique pour respecter l'interface CircuitComponent.
    @Override
    public abstract CircuitComponent clone() throws CloneNotSupportedException;

    @Override
    public Position getPosition() {
        return new Position(position.getX(), position.getY());
    }

    // Implémentation par défaut de setPosition() pour mettre à jour la position
    @Override
    public void setPosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
        this.position = new Point2D(x, y);
    }

    // Par défaut, la rotation délègue à rotateIfSelected()
    @Override
    public void rotate() {
        rotateIfSelected();
    }

    // Méthodes par défaut pour initialize, evaluate et reset (à surcharger dans les sous-classes)
    @Override
    public void initialize() {
        // Par défaut, ne rien faire
    }
    
    @Override
    public void evaluate() {
        // Par défaut, ne rien faire
    }
    
    @Override
    public void reset() {
        // Par défaut, ne rien faire
    }

    /**
     * Permet de tourner le composant s'il est sélectionné.
     */
    public void rotateIfSelected() {
        if (estSelectionne) {
            double nouvelAngle = (getRotate() + 90) % 360; // Rotation par pas de 90°
            applyCss();
            layout();
            setRotate(nouvelAngle);
            mettreAJourFils();
            System.out.println("Composant tourné à " + nouvelAngle + " degrés");
        }
    }

    /**
     * Configure la sélection du composant (clic gauche).
     */
    private void configurerSelection() {
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                estSelectionne = !estSelectionne;
                if (estSelectionne) {
                    setEffect(new DropShadow(10, Color.BLUE));
                } else {
                    setEffect(null);
                }
            }
        });
    }

    /**
     * Configure le déplacement du composant et met à jour les fils connectés.
     */
    private void configurerDeplacement() {
        setOnMousePressed(event -> {
            Point2D parentPoint = getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
            offset = new Point2D(parentPoint.getX() - getLayoutX(), parentPoint.getY() - getLayoutY());
            event.consume();
        });

        setOnMouseDragged(event -> {
            Point2D parentPoint = getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
            double nouvelleX = parentPoint.getX() - offset.getX();
            double nouvelleY = parentPoint.getY() - offset.getY();
            double tailleGrille = 30;
            nouvelleX = Math.round(nouvelleX / tailleGrille) * tailleGrille;
            nouvelleY = Math.round(nouvelleY / tailleGrille) * tailleGrille;
            setLayoutX(nouvelleX);
            setLayoutY(nouvelleY);
            position = new Point2D(nouvelleX, nouvelleY);
            mettreAJourFils();
            event.consume();
        });

        setOnMouseReleased(event -> {
            double posX = getLayoutX();
            double posY = getLayoutY();
            double tailleGrille = 30;
            double snappedX = Math.round(posX / tailleGrille) * tailleGrille;
            double snappedY = Math.round(posY / tailleGrille) * tailleGrille;
            setLayoutX(snappedX);
            setLayoutY(snappedY);
            position = new Point2D(snappedX, snappedY);
            mettreAJourFils();
            event.consume();
        });
    }

    /**
     * Met à jour la position de tous les fils connectés.
     */
    private void mettreAJourFils() {
        for (FilView fil : filsConnectes) {
            fil.mettreAJourPosition();
        }
    }

    public void ajouterFil(FilView fil) {
        filsConnectes.add(fil);
    }

    public List<FilView> getFilsConnectes() {
        return filsConnectes;
    }

    public List<Circle> getEntrees() {
        return entrees;
    }

    public List<Circle> getSorties() {
        return sorties;
    }
}
