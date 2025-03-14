import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.nio.file.Paths;

public class ThemeManager {
    private final Scene scene;
    private final GrilleCanvas grille;
    private boolean isDarkMode = false;
    private static final String LIGHT_THEME = "light-theme.css";
    private static final String DARK_THEME = "dark-theme.css";
    private static final String PATH = Paths.get("Interface-Graphique/styles/").toUri().toString(); 

    public ThemeManager(Scene scene, GrilleCanvas grille) {
        this.scene = scene;
        this.grille = grille;
        this.isDarkMode = Preferences.getTheme().equals("Sombre");
        initializeTheme();
    }

    private void initializeTheme() {
        // Initialiser le thème en fonction des préférences
        if (isDarkMode) {
            scene.getStylesheets().add(Paths.get("Interface-Graphique/styles/" + DARK_THEME).toUri().toString()); 
        } else {
            scene.getStylesheets().add(Paths.get("Interface-Graphique/styles/" + LIGHT_THEME).toUri().toString()); 
        }
    }

    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        scene.getStylesheets().clear();
        
        if (isDarkMode) {
            scene.getStylesheets().add(Paths.get("Interface-Graphique/styles/" + DARK_THEME).toUri().toString()); // ✅ Utilisation dynamique
            grille.getGraphicsContext2D().setFill(Color.LIGHTGRAY); // Points plus clairs en mode sombre
        } else {
            scene.getStylesheets().add(Paths.get("Interface-Graphique/styles/" + LIGHT_THEME).toUri().toString()); // ✅ Utilisation dynamique
            grille.getGraphicsContext2D().setFill(Color.GRAY); // Points normaux en mode clair
        }
        
        // Forcer le redessin de la grille
        grille.redessinerGrille();
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }
}
