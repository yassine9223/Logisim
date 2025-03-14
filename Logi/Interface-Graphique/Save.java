import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Save {

    private Stage stage;
    private ToggleButton themeSwitch;
    private static final String PATH = "file:///C:/Users/adem/Documents/LogiL2/logil2/Interface-Graphique/styles/images/";

    public Save() {
        this.stage = new Stage();
        stage.setTitle("Préférences");

        VBox layout = new VBox(10);

        // Thème avec ToggleButton
        Label themeLabel = new Label("Mode :");
        themeSwitch = new ToggleButton();
        themeSwitch.setSelected(Preferences.getTheme().equals("Sombre"));
        themeSwitch.getStyleClass().add("img-theme.css");


        // Mise à jour du thème lors du changement d'état du switch
        themeSwitch.setOnAction(event -> {
            String nouveauTheme = themeSwitch.isSelected() ? "Sombre" : "Clair";
            Preferences.save(nouveauTheme, Preferences.getZoom());
        });

        // Images pour le thème
        ImageView soleil = new ImageView(new Image(PATH + "soleil.png"));
        soleil.setFitWidth(20);
        soleil.setFitHeight(20);

        ImageView lune = new ImageView(new Image(PATH + "lune.png"));
        lune.setFitWidth(20);
        lune.setFitHeight(20);

        // HBox pour switch avec images
        HBox switchBox = new HBox(10, soleil, themeSwitch, lune);
        switchBox.setSpacing(10);

        // Zoom
        Label zoomLabel = new Label("Niveau de zoom :");
        TextField zoomTextField = new TextField();
        zoomTextField.setText(String.valueOf(Preferences.getZoom()));  // Récupérer et définir la valeur enregistrée
        zoomTextField.setPromptText("Exemple : 100");

        zoomTextField.setPrefWidth(30);  // Fixer la largeur du champ de texte

        // HBox pour niveau de zoom (texte + champ + %)
        HBox zoomBox = new HBox(5);
        zoomBox.getChildren().addAll(zoomLabel, zoomTextField);

        // Label "%" à droite du champ
        Label percentLabel = new Label("%");
        zoomBox.getChildren().add(percentLabel);

        // Bouton Sauvegarder
        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> savePreferences(zoomTextField.getText()));

        layout.getChildren().addAll(themeLabel, switchBox, zoomBox, saveButton);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    private void savePreferences(String zoomText) {
        try {
            // Enlever le '%' de la fin avant de convertir en entier
            zoomText = zoomText.replace("%", "").trim();
            int zoom = Integer.parseInt(zoomText);  // Convertir le texte en entier

            // Vérifier que le niveau de zoom est compris entre 50 et 200
            if (zoom < 50 || zoom > 200) {
                throw new NumberFormatException();  // Si le zoom est hors de la plage, lever une exception
            }

            // Sauvegarder les préférences avec le thème sélectionné par le ToggleButton
            String theme = themeSwitch.isSelected() ? "Sombre" : "Clair";
            Preferences.save(theme, zoom);

            stage.close();  // Fermer la fenêtre après la sauvegarde
        } catch (NumberFormatException e) {
            // Afficher une alerte si la saisie n'est pas valide
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Niveau de zoom invalide");
            alert.setContentText("Veuillez entrer un niveau de zoom entre 50% et 200%.");
            alert.showAndWait();
        }
    }
}
