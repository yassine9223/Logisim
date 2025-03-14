import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.Slider;

public class Zoom {
    private double zoomLevel = Preferences.getZoom() / 100.0;
    private final double zoomStep = 0.05;
    private final double zoomMax = 2;  // Niveau de zoom maximal
    private double initialX;
    private double initialY;
    private final GrilleCanvas grille;
    private final Pane panneauDessins;
    private final double baseWidth;
    private final double baseHeight;
    private Slider zoomSlider;

    public Zoom(GrilleCanvas grille) {
        this.grille = grille;
        this.panneauDessins = (Pane) grille.getParent();
        this.baseWidth = grille.getWidth();
        this.baseHeight = grille.getHeight();

        updateZoom();
        updateGrilleSize();
    }

    public void zoomIn() {
        zoomLevel += zoomStep;
        zoomLevel = Math.min(2, zoomLevel);
        updateZoom();
        updateGrilleSize();
        if (zoomSlider != null) zoomSlider.setValue(zoomLevel);
    }

    public void zoomOut() {
        zoomLevel -= zoomStep;
        zoomLevel = Math.max(0.5, zoomLevel);
        updateZoom();
        updateGrilleSize();
        if (zoomSlider != null) zoomSlider.setValue(zoomLevel);
    }

    public void resetZoom() {
        zoomLevel = 1.0;
        updateZoom();
        updateGrilleSize();
        if (zoomSlider != null) zoomSlider.setValue(zoomLevel);
    }

    private void onScroll(ScrollEvent event) { //On scroll pas identique
        double mouseX = event.getX() / panneauDessins.getWidth();
        double mouseY = event.getY() / panneauDessins.getHeight();

        if (event.getDeltaY() > 0) {
            zoomIn();
        } else {
            zoomOut();
        }

        double newX = event.getX() - (mouseX * panneauDessins.getWidth());
        double newY = event.getY() - (mouseY * panneauDessins.getHeight());
        grille.deplacer(newX, newY);

        event.consume();
    }

    private void updateGrilleSize() { //Mis la sienne
        double newWidth = baseWidth * (1 / zoomLevel);
        double newHeight = baseHeight * (1 / zoomLevel);
    
        grille.updateWidth(newWidth);
        grille.updateHeight(newHeight);
        grille.redessinerGrille();
    }


    public void updateZoom() {
        grille.setScale(zoomLevel, zoomLevel);
        grille.setLayoutX(0); //Jai pas capte ca sert a quoi ?
        grille.setLayoutY(0);
    }

    public void onMousePressed(MouseEvent event) {
        initialX = event.getSceneX();
        initialY = event.getSceneY();
    }

    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - initialX;
        double deltaY = event.getSceneY() - initialY;
        grille.deplacer(deltaX, deltaY);
        initialX = event.getSceneX();
        initialY = event.getSceneY();
    }

    public void bindMouseEvents(Pane panneauDessins) {
        panneauDessins.setOnMousePressed(this::onMousePressed);
        panneauDessins.setOnMouseDragged(this::onMouseDragged);
        panneauDessins.setOnScroll(this::onScroll);
    }

    public Slider createZoomSlider() {
        Slider zoomSlider = new Slider(1, 2.0, 1.0);
        zoomSlider.setBlockIncrement(0.025);
        zoomSlider.setMajorTickUnit(0.2);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setShowTickLabels(true);
    
        this.zoomSlider = zoomSlider;
        
        zoomSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                zoomLevel = zoomSlider.getValue();
                updateZoom();
                updateGrilleSize();
            }
        });
    
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!zoomSlider.isValueChanging()) {
                zoomLevel = newVal.doubleValue();
                updateZoom();
                updateGrilleSize();
            }
        });
    
        return zoomSlider;
    }

    public void bindSlider(Slider zoomSlider) { //La sienne

        this.zoomSlider = zoomSlider;
        zoomSlider.setMin(0.5);
        zoomSlider.setMax(2.0);
        zoomSlider.setValue(1.0);
        zoomSlider.setBlockIncrement(0.025);
        zoomSlider.setMajorTickUnit(0.2);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setShowTickLabels(true);
    
        // Mise à jour lors d'un déplacement manuel
        zoomSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) { // Quand l'utilisateur a fini de bouger le slider
                zoomLevel = zoomSlider.getValue();
                updateZoom();
                updateGrilleSize();
            }
        });
    
        // Mise à jour lors d'un changement de valeur automatique (molette)
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!zoomSlider.isValueChanging()) { // Ne change que si l'utilisateur ne touche pas au slider
                zoomLevel = newVal.doubleValue();
                updateZoom();
                updateGrilleSize();
            }
        });
    }

}