import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;

/**
 * FilView représente graphiquement un fil reliant deux ports (entree et sortie).
 * Il met à jour sa position en fonction du déplacement des ports et propose un menu contextuel pour sa suppression.
 */
public class FilView extends Line {
    private Circle entree;
    private Circle sortie;

    public FilView(Circle entree, Circle sortie) {
        if (entree == null || sortie == null) {
            throw new IllegalArgumentException("Les ports ne peuvent pas être null.");
        }
        this.entree = entree;
        this.sortie = sortie;

        setStrokeWidth(4);

        // Création du menu contextuel pour supprimer le fil
        ContextMenu contextMenu = new ContextMenu();
        MenuItem supprimerItem = new MenuItem("Supprimer le fil");
        supprimerItem.setOnAction(e -> supprimer());
        contextMenu.getItems().add(supprimerItem);

        // Affiche le menu contextuel lors d'un clic droit sur ce fil
        this.setOnContextMenuRequested(event -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        });

        // Initialisation de la position du fil
        mettreAJourPosition();
    }

    /**
     * Met à jour la position du fil en se basant sur la position des ports dans l'espace de la scène.
     */
    public void mettreAJourPosition() {
        // Convertir les coordonnées locales du port en coordonnées de la scène
        Point2D scenePosEntree = entree.localToScene(entree.getCenterX(), entree.getCenterY());
        Point2D scenePosSortie = sortie.localToScene(sortie.getCenterX(), sortie.getCenterY());
        
        // Récupère le parent du fil (qui est un Pane)
        Pane parent = (Pane) this.getParent();
        if (parent != null) {
            // Convertit les coordonnées de la scène en coordonnées locales du parent
            Point2D posEntree = parent.sceneToLocal(scenePosEntree);
            Point2D posSortie = parent.sceneToLocal(scenePosSortie);
            
            setStartX(posEntree.getX());
            setStartY(posEntree.getY());
            setEndX(posSortie.getX());
            setEndY(posSortie.getY());
        }
    }
    
    /**
     * Supprime ce fil de son parent.
     */
    public void supprimer() {
        if (getParent() != null) {
            ((Pane) getParent()).getChildren().remove(this);
        }
    }
}
