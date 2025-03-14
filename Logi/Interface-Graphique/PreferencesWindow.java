import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PreferencesWindow {
    private Stage stage;
    private StackPane toggleSwitch;
    private Circle toggleButton;
    private Rectangle background;
    private boolean isDarkMode;
    private static final String PATH = "file:///C:/Users/adem/Documents/LogiL2/logil2/Interface-Graphique/styles/images/";
    private static final String PATHCSS = "file:///C:/Users/adem/Documents/LogiL2/logil2/Interface-Graphique/styles/";

    // Constantes pour le toggle switch
    private static final double SWITCH_WIDTH = 50;
    private static final double SWITCH_HEIGHT = 24;
    private static final double BUTTON_RADIUS = 10;
    private static final double TOGGLE_TRANSLATE = SWITCH_WIDTH - (BUTTON_RADIUS * 2) - 4; // Espace pour le déplacement

    public PreferencesWindow() {
        this.stage = new Stage();
        stage.setTitle("Préférences");

        // Initialisation avec le thème actuel
        isDarkMode = Preferences.getTheme().equals("Sombre");

        // Création du layout principal
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20;");

        // Création du toggle switch personnalisé
        createToggleSwitch();

        // Images pour le thème
        ImageView soleil = new ImageView(new Image(PATH + "soleil.png"));
        soleil.setFitWidth(20);
        soleil.setFitHeight(20);

        ImageView lune = new ImageView(new Image(PATH + "lune.png"));
        lune.setFitWidth(20);
        lune.setFitHeight(20);

        // Layout pour le switch et les icônes
        HBox themeControls = new HBox(10);
        themeControls.getChildren().addAll(soleil, toggleSwitch, lune);

        // Zoom controls
        HBox zoomBox = createZoomControls();

        // Bouton de sauvegarde
        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> savePreferences(
            ((TextField) zoomBox.getChildren().get(1)).getText()
        ));

        layout.getChildren().addAll(new Label("Mode :"), themeControls, zoomBox, saveButton);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(PATHCSS + "img-theme.css");
        stage.setScene(scene);
    }

    private void createToggleSwitch() {
        // Création du fond du switch
        background = new Rectangle(SWITCH_WIDTH, SWITCH_HEIGHT);
        background.setArcWidth(SWITCH_HEIGHT);
        background.setArcHeight(SWITCH_HEIGHT);
        background.setFill(isDarkMode ? Color.DARKGRAY : Color.LIGHTGRAY);

        // Création du bouton toggle
        toggleButton = new Circle(BUTTON_RADIUS);
        toggleButton.setFill(Color.WHITE);
        
        // Positionnement initial du bouton
        toggleButton.setTranslateX(isDarkMode ? TOGGLE_TRANSLATE : 0);
        
        // Container pour le switch
        toggleSwitch = new StackPane();
        toggleSwitch.getChildren().addAll(background, toggleButton);
        
        // Ajustement de la position du bouton dans le StackPane
        toggleButton.setTranslateX(isDarkMode ? TOGGLE_TRANSLATE : -TOGGLE_TRANSLATE/2);
        
        // Définition de la taille du conteneur
        toggleSwitch.setMinSize(SWITCH_WIDTH, SWITCH_HEIGHT);
        toggleSwitch.setMaxSize(SWITCH_WIDTH, SWITCH_HEIGHT);

        // Animation pour le toggle
        toggleSwitch.setOnMouseClicked(e -> toggleTheme());
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;

        // Animation du bouton
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), toggleButton);
        transition.setToX(isDarkMode ? TOGGLE_TRANSLATE : -TOGGLE_TRANSLATE/2);
        
        // Changement de couleur du fond
        background.setFill(isDarkMode ? Color.DARKGRAY : Color.LIGHTGRAY);
        
        transition.play();
    }

    private HBox createZoomControls() {
        Label zoomLabel = new Label("Niveau de zoom :");
        TextField zoomTextField = new TextField(String.valueOf(Preferences.getZoom()));
        zoomTextField.setPromptText("Exemple : 100");
        zoomTextField.setPrefWidth(60);

        Label percentLabel = new Label("%");

        HBox zoomBox = new HBox(5);
        zoomBox.getChildren().addAll(zoomLabel, zoomTextField, percentLabel);
        return zoomBox;
    }

    private void savePreferences(String zoomText) {
        try {
            zoomText = zoomText.replace("%", "").trim();
            int zoom = Integer.parseInt(zoomText);

            if (zoom < 50 || zoom > 200) {
                throw new NumberFormatException();
            }

            Preferences.save(isDarkMode ? "Sombre" : "Clair", zoom);
            stage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Niveau de zoom invalide");
            alert.setContentText("Veuillez entrer un niveau de zoom entre 50% et 200%.");
            alert.showAndWait();
        }
    }

    public void show() {
        stage.show();
    }
}

//Le cercle va trop loin + ajoute l'image dans le cercle genre with claude