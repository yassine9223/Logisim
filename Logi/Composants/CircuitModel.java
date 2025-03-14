import java.util.*;

import modelememoire.Circuit;
import modelememoire.Composant;
import modelememoire.Fil;
import modelememoire.State;

/**
 * Gère l'historique (undo/redo), l'ajout/suppression de composants Vue+Modèle,
 * et lance la simulation en appelant circuitLogique.simuler().
 */
public class CircuitModel {

    private boolean isModified = false;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    // Liste des composants pour l'IHM
    private List<CircuitComponent> components; 
    private List<Connection> connections;

    private Circuit circuitLogique; // Le circuit logique (du package modelememoire)

    private boolean isSimulationRunning = false;

    public CircuitModel() {
        components = new ArrayList<>();
        connections = new ArrayList<>();
        circuitLogique = new Circuit();
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public void addComponent(CircuitComponent component) {
        undoStack.push(serializeCircuit());
        redoStack.clear();
        components.add(component);
        // Ajoute aussi le composant logique s'il est disponible
        if (component instanceof HasLogique) {
            Composant c = ((HasLogique) component).getLogique();
            circuitLogique.ajouterComposant(c);
        }
        setModified(true);
    }

    public void deleteComponent(CircuitComponent component) {
        undoStack.push(serializeCircuit());
        redoStack.clear();
        components.remove(component);
        if (component instanceof HasLogique) {
            Composant c = ((HasLogique) component).getLogique();
            circuitLogique.retirerComposant(c);
        }
        setModified(true);
    }

    public void addConnection(CircuitComponent source, CircuitComponent target) {
        undoStack.push(serializeCircuit());
        redoStack.clear();
        connections.add(new Connection(source, target));
        setModified(true);
    
        // Sur le plan logique
        if (source instanceof HasLogique && target instanceof HasLogique) {
            Composant cSource = ((HasLogique) source).getLogique();
            Composant cTarget = ((HasLogique) target).getLogique();
            Fil f = new Fil(cSource, cTarget);
    
            // Les deux appels critiques :
            cSource.connecterSortie(f);
            cTarget.connecterEntree(f);

            circuitLogique.ajouterFil(f);
        }
    }
    

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(serializeCircuit());
            deserializeCircuit(undoStack.pop());
            setModified(true);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(serializeCircuit());
            deserializeCircuit(redoStack.pop());
            setModified(true);
        }
    }

    private String serializeCircuit() {
        // Simplifié : liste les composants et connexions (à améliorer en JSON/XML par exemple)
        StringBuilder sb = new StringBuilder();
        for (CircuitComponent comp : components) {
            sb.append(comp.getName())
              .append(":")
              .append(comp.getClass().getSimpleName())
              .append("\n");
        }
        for (Connection conn : connections) {
            sb.append(conn.toString()).append("\n");
        }
        return sb.toString();
    }

    private void deserializeCircuit(String serializedData) {
        // Stub de désérialisation (à compléter si nécessaire)
        components.clear();
        connections.clear();
        circuitLogique = new Circuit();
        // ...
    }

    public static CircuitModel fromString(String str) {
        CircuitModel model = new CircuitModel();
        model.deserializeCircuit(str);
        model.setModified(false);
        return model;
    }

    // Lancement ou arrêt de la simulation
    public void toggleSimulation() {
        if (isSimulationRunning) {
            stopSimulation();
        } else {
            startSimulation();
        }
    }

    public void startSimulation() {
        if (!isSimulationRunning) {
            isSimulationRunning = true;
            System.out.println("Simulation démarrée");
            circuitLogique.reset();
            circuitLogique.simuler(); // Effectue plusieurs passes pour atteindre la stabilité
            // Mise à jour des composants de l'IHM
            for (CircuitComponent comp : components) {
                comp.initialize();
                comp.evaluate();
            }
        }
    }

    public void stopSimulation() {
        if (isSimulationRunning) {
            isSimulationRunning = false;
            System.out.println("Simulation arrêtée");
            circuitLogique.reset();
            for (CircuitComponent comp : components) {
                comp.reset();
            }
        }
    }

    public void simulationStep() {
        if (isSimulationRunning) {
            System.out.println("Simulation pas à pas");
            // On effectue une passe sur tous les fils
            for (Fil f : circuitLogique.getFils()) {
                f.effectuerCalcul();
            }
            // Mise à jour de l'IHM pour chaque composant
            for (CircuitComponent comp : components) {
                comp.evaluate();
            }
        } else {
            System.out.println("Simulation non démarrée. Démarrez-la d'abord.");
        }
    }

    public void resetSimulation() {
        System.out.println("Simulation réinitialisée");
        circuitLogique.reset();
        circuitLogique.simuler();
        for (CircuitComponent comp : components) {
            comp.reset();
            comp.evaluate();
        }
    }

    @Override
    public String toString() {
        return serializeCircuit();
    }
}
