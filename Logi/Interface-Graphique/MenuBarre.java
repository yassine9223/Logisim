import javafx.scene.control.*;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.net.URI;

public class MenuBarre extends MenuBar {
    private Main mainApp;

    public MenuBarre(Stage primaryStage, Main mainApp, MainController controller) {
        this.mainApp = mainApp;

        // Menu "Fichier"
        Menu menuFichier = new Menu("Fichier");
        MenuItem nouveau = new MenuItem("Nouveau");
        nouveau.setOnAction(e -> controller.newCircuit());
        MenuItem ouvrir = new MenuItem("Ouvrir...");
        ouvrir.setOnAction(e -> controller.openCircuit());
        MenuItem enregistrer = new MenuItem("Enregistrer");
        enregistrer.setOnAction(e -> controller.saveCircuit());
        MenuItem enregistrerSous = new MenuItem("Enregistrer sous...");
        enregistrerSous.setOnAction(e -> controller.saveCircuit());
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> primaryStage.close());
        menuFichier.getItems().addAll(nouveau, ouvrir, enregistrer, enregistrerSous, new SeparatorMenuItem(), quitter);

        // Menu "Édition"
        Menu menuEdition = new Menu("Édition");
        MenuItem annuler = new MenuItem("Annuler");
        annuler.setOnAction(e -> controller.undoAction());
        MenuItem retablir = new MenuItem("Rétablir");
        retablir.setOnAction(e -> controller.redoAction());
        MenuItem copier = new MenuItem("Copier");
        copier.setOnAction(e -> controller.copyComponent());
        MenuItem coller = new MenuItem("Coller");
        coller.setOnAction(e -> controller.pasteComponent());
        MenuItem supprimer = new MenuItem("Supprimer");
        supprimer.setOnAction(e -> controller.deleteComponent());
        MenuItem tourner = new MenuItem("Tourner");
        tourner.setOnAction(e -> controller.rotateComponent());
        menuEdition.getItems().addAll(annuler, retablir, new SeparatorMenuItem(), copier, coller, supprimer, tourner);

        // Menu "Composants"
        Menu menuComposants = new Menu("Composants");
        MenuItem porteAND = new MenuItem("Porte AND");
        porteAND.setOnAction(e -> controller.createGate("AND"));
        MenuItem porteOR = new MenuItem("Porte OR");
        porteOR.setOnAction(e -> controller.createGate("OR"));
        MenuItem porteNOT = new MenuItem("Porte NOT");
        porteNOT.setOnAction(e -> controller.createGate("NOT"));
        MenuItem porteNAND = new MenuItem("Porte NAND");
        porteNAND.setOnAction(e -> controller.createGate("NAND"));
        MenuItem porteXOR = new MenuItem("Porte XOR");
        porteXOR.setOnAction(e -> controller.createGate("XOR"));
        MenuItem led = new MenuItem("LED");
        led.setOnAction(e -> controller.createGate("LED"));
        MenuItem pin = new MenuItem("PIN");
        pin.setOnAction(e -> controller.createGate("PIN"));
        menuComposants.getItems().addAll(porteAND, porteOR, porteNOT, porteNAND, porteXOR, led, pin);

        // Menu "Simulation"
        Menu menuSimulation = new Menu("Simulation");
        MenuItem demarrer = new MenuItem("Démarrer");
        demarrer.setOnAction(e -> controller.startSimulation());
        MenuItem arreter = new MenuItem("Arrêter");
        arreter.setOnAction(e -> controller.stopSimulation());
        MenuItem pasAPas = new MenuItem("Pas à pas");
        pasAPas.setOnAction(e -> controller.simulationStep());
        menuSimulation.getItems().addAll(demarrer, arreter, pasAPas);

        // Menu "Options"
        Menu menuOptions = new Menu("Options");
        MenuItem preferences = new MenuItem("Préférences");
        preferences.setOnAction(e -> openPreferencesWindow());
        MenuItem toggleTheme = new MenuItem("Changer de thème");
        toggleTheme.setOnAction(e -> mainApp.getThemeManager().toggleTheme());
        menuOptions.getItems().addAll(preferences, toggleTheme);

        // Menu "Fenetre" (sans accent dans le nom de la variable)
        Menu menuFenetre = new Menu("Fenêtre");
        MenuItem reinitialiser = new MenuItem("Réinitialiser la vue");
        reinitialiser.setOnAction(e -> controller.resetSimulation());
        menuFenetre.getItems().addAll(reinitialiser);

        // Menu "Aide"
        Menu menuAide = new Menu("Aide");
        MenuItem manuel = new MenuItem("Manuel");
        manuel.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.aidelogisim.vercel.app"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        MenuItem aPropos = new MenuItem("À propos");
        aPropos.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("À propos");
            alert.setHeaderText("LogiL2");
            alert.setContentText("Version 1.0\nDéveloppé par votre équipe.");
            alert.showAndWait();
        });
        menuAide.getItems().addAll(manuel, aPropos);

        // Ajout des menus à la barre (utilisez le nom correct menuFenetre)
        this.getMenus().addAll(menuFichier, menuEdition, menuComposants, menuSimulation, menuOptions, menuFenetre, menuAide);
    }

    private void openPreferencesWindow() {
        PreferencesWindow preferencesWindow = new PreferencesWindow();
        preferencesWindow.show();
    }
}
