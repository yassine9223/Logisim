import com.sun.tools.javac.Main;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class KeyBindingsManagerFX {

    private final Scene scene;
    private final MainController controller;
    private final Stage scenePrincipale;  // Ajouter la variable pour Stocker le Stage
    private final Zoom zoom;
    private final ThemeManager themeManager;

    public KeyBindingsManagerFX(Stage scenePrincipale, Scene scene, MainController controller, Zoom zoom, ThemeManager themeManager) {
        this.scenePrincipale = scenePrincipale;  // Stocker le Stage
        this.scene = scene;
        this.controller = controller;
        this.zoom = zoom;
        this.themeManager = themeManager;
    }

    public void setupKeyBindings() {

        // Navigation et édition
        bindKey("CTRL+N", controller::newCircuit);
        bindKey("CTRL+O", controller::openCircuit);
        bindKey("CTRL+S", controller::saveCircuit);
        bindKey("CTRL+Z", controller::undoAction);
        bindKey("CTRL+Y", controller::redoAction);

        // Manipulation de composants
        bindKey("DELETE", controller::deleteComponent);
        bindKey("CTRL+C", controller::copyComponent);
        bindKey("CTRL+V", controller::pasteComponent);
        bindKey("R", controller::rotateComponent);
        bindKey("CTRL+W", () -> scenePrincipale.close());

        // Simulation
        bindKey("SPACE", controller::toggleSimulation);
        bindKey("CTRL+T", controller::resetSimulation);

        // Affichage
        bindKey("CTRL+EQUALS", zoom::zoomIn);
        bindKey("CTRL+MINUS", zoom::zoomOut);
        bindKey("CTRL+0", zoom::resetZoom);
        bindKey("CTRL+P", themeManager::toggleTheme);
        
    }

    private void bindKey(String keyCombination, Runnable action) {
        KeyCombination combination = KeyCombination.valueOf(keyCombination);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (combination.match(event)) {
                    action.run();
                event.consume();
            }
        });
    }

}
