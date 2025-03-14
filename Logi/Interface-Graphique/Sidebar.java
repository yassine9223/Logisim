import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;

public class Sidebar extends VBox {
    private Button startSimulation, stopSimulation;
    private Zoom zoom;

    public Sidebar(Main mainApp, MainController controller, Zoom zoom) {
        super(15);
        setPrefWidth(250);
        this.zoom = zoom;

        double largeurBouton = 240;
        double hauteurBouton = 55;//String[] portenom = {"Porte AND", "Porte OR", "Porte NOT", "Led", "Porte XOR", "Interrupteur", "Bascule", "Start Simulation", "Stop Simulation"};

        setBackground(new Background(new BackgroundFill(Color.web("#121212"), CornerRadii.EMPTY, Insets.EMPTY)));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setOffsetX(0);
        shadow.setOffsetY(10);
        shadow.setColor(Color.rgb(0, 0, 0, 0.7)); 
        setEffect(shadow);

        String[] portenom = {"Porte AND", "Porte OR", "Porte NOT", "Porte NAND", "Porte XOR", "LED", "PIN", "Start Simulation", "Stop Simulation"};

        for (String nom : portenom) {
            Button bouton = new Button(nom);
            bouton.setPrefSize(largeurBouton, hauteurBouton);

            bouton.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: white; -fx-border-width: 2px;");

            // Association des actions pour chaque bouton

            switch (nom) {
                case "Porte AND": bouton.setOnAction(e -> controller.createGate("AND")); break;
                case "Porte OR": bouton.setOnAction(e -> controller.createGate("OR")); break;
                case "Porte NOT": bouton.setOnAction(e -> controller.createGate("NOT")); break;
                case "Porte NAND": bouton.setOnAction(e -> controller.createGate("NAND")); break;
                case "Porte XOR": bouton.setOnAction(e -> controller.createGate("XOR")); break;
                case "PIN": bouton.setOnAction(e -> controller.createGate("PIN")); break;
                case "LED": bouton.setOnAction(e -> controller.createGate("LED")); break;
                case "Start Simulation": 
                    startSimulation = bouton; 
                    startSimulation.setOnAction(e -> {
                        startSimulation.setDisable(true);
                        stopSimulation.setDisable(false);
                        controller.toggleSimulation();
                    });
                    break;
                case "Stop Simulation": 
                    stopSimulation = bouton;
                    stopSimulation.setDisable(true);
                    stopSimulation.setOnAction(e -> {
                        stopSimulation.setDisable(true);
                        startSimulation.setDisable(false);
                        controller.toggleSimulation();
                    });
                    break;
            }

            getChildren().add(bouton);
        }

        Slider zoomSlider = zoom.createZoomSlider();
        VBox zoomBox = new VBox(10);
        zoomBox.setAlignment(Pos.CENTER);
        zoomBox.getChildren().addAll(zoomSlider);
        
        getChildren().add(zoomBox);
    }

}