package modelememoire;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe gérant la liste des Composant et des Fil,
 * ainsi que la propagation des signaux lors de la simulation.
 */
public class Circuit {

    private List<Composant> composants;
    private List<Fil> fils;

    public Circuit() {
        this.composants = new ArrayList<>();
        this.fils = new ArrayList<>();
    }

    public void ajouterComposant(Composant c) {
        composants.add(c);
    }

    public void retirerComposant(Composant c) {
        composants.remove(c);
        // Supprimer aussi les fils associés
        fils.removeIf(f -> f.getComposantDepart() == c || f.getComposantArrive() == c);
    }

    public void ajouterFil(Fil f) {
        fils.add(f);
    }

    public void retirerFil(Fil f) {
        fils.remove(f);
    }

    public List<Composant> getComposants() {
        return composants;
    }

    public List<Fil> getFils() {
        return fils;
    }

    /**
     * Lance la simulation :
     *  - On effectue plusieurs passes pour résoudre un éventuel point fixe.
     *  - On s'arrête dès qu'aucun fil n'a changé d'état ou qu'on atteint le max d'itérations.
     */
    public void simuler() {
        final int MAX_ITER = 20;  // Limite pour éviter une boucle infinie
        boolean stable = false;

        // On itère jusqu'à MAX_ITER ou jusqu'à ce qu'aucun fil ne change d'état
        for (int iteration = 0; iteration < MAX_ITER && !stable; iteration++) {
            stable = true;  // On suppose que le circuit est stable, on infirmera si on détecte un changement

            // Met à jour tous les composants
            for (Composant composant : composants) {
                composant.evaluerSignal();
            }

            // Met à jour tous les fils
            for (Fil fil : fils) {
                State ancienEtat = fil.getEtat();
                fil.effectuerCalcul();  // Met à jour fil.setEtat(...) en fonction du composant de départ
                if (fil.getEtat() != ancienEtat) {
                    stable = false;  // On a détecté un changement, donc on devra refaire une passe
                }
            }
        }
    }

    /**
     * Remet tous les composants et fils à l'état UNDEFINED
     */
    public void reset() {
        for (Composant c : composants) {
            c.reset();
        }
        for (Fil f : fils) {
            f.setEtat(State.UNDEFINED);
        }
    }
}
