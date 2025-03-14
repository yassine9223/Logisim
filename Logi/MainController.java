import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class MainController {

    private CircuitModel currentCircuit = new CircuitModel();
    private final Stage primaryStage;
    private CircuitComponent selectedComponent;
    private static MainController instance;
    private FilInteraction filInteraction;
    private Pane panneauDessins;
    
    // Indicateur d'état de simulation
    private boolean simulationActive = false;

    public MainController(Stage primaryStage, Pane panneauDessins) {
        instance = this;
        this.primaryStage = primaryStage;
        this.panneauDessins = panneauDessins;
        this.filInteraction = new FilInteraction(panneauDessins);
    }

    public static MainController getInstance() {
        return instance;
    }

    public void setSelectedComponent(CircuitComponent component) {
        this.selectedComponent = component;
    }

    public CircuitComponent getSelectedComponent() {
        return selectedComponent;
    }

    public void createGate(String type) {
        System.out.println("createGate appelée avec : " + type);
        ComposantView vue = null; 
        Random random = new Random();
        double x = random.nextDouble(500);
        double y = random.nextDouble(500);
    
        switch (type) {
            case "AND":
                vue = new PorteAndView(x, y);
                break;
            case "NOT":
                vue = new PorteNotView(x, y);
                break;
            case "OR":
                vue = new PorteOrView(x, y);
                break;
            case "NAND":
                vue = new PorteNandView(x, y);
                break;
            case "XOR":
                vue = new PorteXorView(x, y);
                break;
            case "LED":
                vue = new LedView(x, y);
                break;
            case "PIN":
                vue = new PinView(x, y);
                break;
            default:
                System.out.println("Porte logique inconnue : " + type);
                return;
        }
    
        if (vue != null) {
            panneauDessins.getChildren().add(vue);
            // Ajout du composant dans le CircuitModel (lié au modèle logique)
            currentCircuit.addComponent(vue);
            // Gestion des clics sur les ports pour créer des connexions
            for (Circle sortie : vue.getSorties()) {
                sortie.setOnMouseClicked(event -> filInteraction.commencerConnexion(sortie));
            }
            for (Circle entree : vue.getEntrees()) {
                entree.setOnMouseClicked(event -> filInteraction.terminerConnexion(entree));
            }
        }
    }

    public void startSimulation() {
        currentCircuit.startSimulation();
        rafraichirAffichage(); // ou evaluate() sur chaque composant
    }
    
    public void stopSimulation() {
        currentCircuit.stopSimulation();
        rafraichirAffichage();
    }
    
    public void simulationStep() {
        currentCircuit.simulationStep();
        rafraichirAffichage();
    }
    
    
    
    public void newCircuit() {
        if (currentCircuit != null && currentCircuit.isModified()) {
            boolean confirm = showConfirmationDialog("Nouveau Circuit", "Voulez-vous sauvegarder le circuit actuel avant de continuer ?");
            if (confirm) {
                saveCircuit();
            }
        }
        currentCircuit = new CircuitModel();
        panneauDessins.getChildren().clear();
        System.out.println("Nouveau circuit créé !");
    }
    
    public void openCircuit() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un circuit");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier Circuit (*.circuit)", "*.circuit"));
        File file = fileChooser.showOpenDialog(primaryStage);
    
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                currentCircuit = CircuitModel.fromString(content);
                panneauDessins.getChildren().clear();
                // TODO : Recharger les vues à partir du modèle désérialisé.
                System.out.println("Circuit ouvert : " + file.getName());
                rafraichirAffichage();
            } catch (IOException e) {
                showErrorDialog("Erreur", "Impossible d'ouvrir le fichier.");
                e.printStackTrace();
            }
        }
    }
    
    public void saveCircuit() {
        if (currentCircuit == null) {
            showErrorDialog("Erreur", "Aucun circuit à sauvegarder.");
            return;
        }
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le circuit");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier Circuit (*.circuit)", "*.circuit"));
        File file = fileChooser.showSaveDialog(primaryStage);
    
        if (file != null) {
            try {
                Files.write(Paths.get(file.getPath()), currentCircuit.toString().getBytes());
                currentCircuit.setModified(false);
                System.out.println("Circuit sauvegardé : " + file.getName());
            } catch (IOException e) {
                showErrorDialog("Erreur", "Impossible de sauvegarder le fichier.");
                e.printStackTrace();
            }
        }
    }
    
    public void undoAction() {
        currentCircuit.undo();
        rafraichirAffichage();
    }
    
    public void redoAction() {
        currentCircuit.redo();
        rafraichirAffichage();
    }
    
    public void deleteComponent() {
        if (selectedComponent != null) {
            if (selectedComponent instanceof ComposantView) {
                ComposantView composant = (ComposantView) selectedComponent;
                for (FilView fil : composant.getFilsConnectes()) {
                    panneauDessins.getChildren().remove(fil);
                }
            }
            
            currentCircuit.deleteComponent(selectedComponent);
            panneauDessins.getChildren().remove(selectedComponent);
            selectedComponent = null;
            System.out.println("Composant supprimé avec ses connexions.");
            rafraichirAffichage();
        }
    }
    
    public void copyComponent() {
        if (selectedComponent != null) {
            try {
                CircuitComponent copie = selectedComponent.clone();
                copie.setPosition(selectedComponent.getPosition().getX() + 20,
                                  selectedComponent.getPosition().getY() + 20);
                currentCircuit.addComponent(copie);
                panneauDessins.getChildren().add((ComposantView) copie);
                rafraichirAffichage();
            } catch (CloneNotSupportedException e) {
                showErrorDialog("Erreur", "Impossible de copier le composant.");
            }
        }
    }
    
    public void pasteComponent() {
        System.out.println("Fonctionnalité de collage à implémenter");
    }
    
    public void rotateComponent() {
        for (Node node : panneauDessins.getChildren()) {
            if (node instanceof ComposantView) {
                ComposantView composant = (ComposantView) node;
                composant.rotateIfSelected();
            }
        }
        currentCircuit.setModified(true);
        rafraichirAffichage();
    }
    
    public void toggleSimulation() {
        currentCircuit.toggleSimulation();
        simulationActive = !simulationActive;
        rafraichirAffichage();
        System.out.println("Simulation " + (simulationActive ? "démarrée" : "arrêtée"));
    }
    
    public void resetSimulation() {
        if (currentCircuit != null) {
            currentCircuit.resetSimulation();
            rafraichirAffichage();
        }
    }
    
    /**
     * Rafraîchit l'affichage en appelant evaluate() sur chaque composant de l'IHM.
     * Utilise Platform.runLater() pour s'assurer que l'UI est mise à jour sur le thread JavaFX.
     */
    private void rafraichirAffichage() {
        Platform.runLater(() -> {
            for (Node node : panneauDessins.getChildren()) {
                if (node instanceof ComposantView) {
                    ((ComposantView) node).evaluate();
                }
            }
        });
    }
    
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(null) == javafx.scene.control.ButtonType.OK;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public CircuitModel getCurrentCircuit() {
        return currentCircuit;
    }
}
