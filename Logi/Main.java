import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.transform.Scale;
import java.nio.file.Paths;
import javafx.scene.shape.Rectangle;

public class Main extends Application {

    private MainController controller;
    private GrilleCanvas grille;
    private Scale scale;
    private Pane panneauDessins;
    private ThemeManager themeManager;
    private Zoom zoom;    
    // Chemin vers le dossier de styles (à adapter si besoin)
    private static final String STYLE_PATH = Paths.get("Interface-Graphique/styles/").toUri().toString();

    @Override
    public void start(Stage primaryStage) {
        // Récupérer la taille de l'écran principal pour adapter l'interface
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Initialisation du panneau principal qui contiendra la grille et les composants
        panneauDessins = new Pane();
        panneauDessins.setPrefSize(screenWidth, screenHeight);
        // Définir un clip pour restreindre l'affichage au panneau
        Rectangle clipRect = new Rectangle(screenWidth, screenHeight);
        panneauDessins.setClip(clipRect);
        panneauDessins.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clipRect.setWidth(newBounds.getWidth());
            clipRect.setHeight(newBounds.getHeight());
        });

        // Création du contrôleur principal en lui passant la scène principale et le panneau
        controller = new MainController(primaryStage, panneauDessins);

        // Construction de la scène principale à l'aide d'un BorderPane
        BorderPane root = new BorderPane();

        // Configuration du zoom et de la grille
        scale = new Scale(1, 1);
        grille = new GrilleCanvas((int) screenWidth, (int) screenHeight, scale);
        panneauDessins.getChildren().add(grille);
        zoom = new Zoom(grille);
        zoom.updateZoom(); // Applique le zoom depuis les préférences
        zoom.bindMouseEvents(panneauDessins); // Lier les événements souris pour le zoom
        // Appliquer la transformation de zoom sur la grille
        grille.getTransforms().add(scale);

        // Intégration d'un menu et d'une sidebar dans le BorderPane
        root.setTop(new MenuBarre(primaryStage, this, controller));
        root.setLeft(new Sidebar(this, controller, zoom));
        root.setCenter(panneauDessins);

        // Création de la scène et application du stylesheet
        Scene scene = new Scene(root, screenWidth, screenHeight);
        try {
            scene.getStylesheets().add(Paths.get("Interface-Graphique/styles/img-theme.css").toUri().toString());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du stylesheet : " + e.getMessage());
        }

        // Initialisation du ThemeManager et application du thème
        themeManager = new ThemeManager(scene, grille);
        themeManager.toggleTheme();

        // Configuration des raccourcis clavier
        KeyBindingsManagerFX keyManager = new KeyBindingsManagerFX(primaryStage, scene, controller, zoom, themeManager);
        keyManager.setupKeyBindings();

        // Configuration finale de la fenêtre principale
        primaryStage.setTitle("LogiL2");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Exemple d'exécution asynchrone (si besoin de mises à jour périodiques)
        Platform.runLater(() -> System.out.println("Application lancée avec succès !"));
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
