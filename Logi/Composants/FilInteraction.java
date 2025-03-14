import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * Cette classe gère l'interaction pour créer des connexions (fils) entre composants.
 * Elle enregistre d'abord le port source lors d'un clic, puis, lorsqu'un port destination est sélectionné,
 * elle crée et ajoute une instance de FilView sur le panneau.
 */
public class FilInteraction {
    private Circle portSource;
    private Pane panneauDessins;

    public FilInteraction(Pane panneauDessins) {
        if (panneauDessins == null) {
            throw new IllegalArgumentException("Le panneau de dessin ne peut pas être null.");
        }
        this.panneauDessins = panneauDessins;
    }

    /**
     * Démarre la connexion en enregistrant le port source.
     * @param portSource Le port (Circle) à partir duquel la connexion démarre.
     */
    public void commencerConnexion(Circle portSource) {
        this.portSource = portSource;
    }

    /**
     * Termine la connexion en reliant le port source au port destination,
     * crée un FilView, l'ajoute au panneau, et informe le modèle du circuit.
     * @param portDestination Le port destination pour la connexion.
     */
    public void terminerConnexion(Circle portDestination) {
        if (portSource != null && portDestination != null) {
            // Création et ajout du FilView sur le panneau
            FilView filView = new FilView(portSource, portDestination);
            panneauDessins.getChildren().add(filView);

            // Récupération des vues parents des ports
            ComposantView composantSource = (ComposantView) portSource.getParent();
            ComposantView composantDest = (ComposantView) portDestination.getParent();

            // Ajout du FilView aux composants pour suivre les déplacements
            if (composantSource != null) {
                composantSource.ajouterFil(filView);
            }
            if (composantDest != null) {
                composantDest.ajouterFil(filView);
            }

            // Ajout de la connexion au modèle logique via le contrôleur
            // (Assurez-vous que MainController.getInstance() retourne l'instance actuelle)
            MainController.getInstance().getCurrentCircuit().addConnection(composantSource, composantDest);

            // Met à jour la position initiale du fil
            filView.mettreAJourPosition();
            // Réinitialise le port source pour une prochaine connexion
            portSource = null;
        }
    }

    /**
     * Retourne le port source actuellement enregistré.
     * @return le port source (Circle) ou null si aucun.
     */
    public Circle getPortSource() {
        return portSource;
    }
}
