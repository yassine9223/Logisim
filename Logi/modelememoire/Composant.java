package modelememoire;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe abstraite représentant un composant logique,
 * possédant un nombre maximum d'entrées et de sorties.
 * Chaque composant doit implémenter evaluerSignal() pour calculer sa sortie.
 */
public abstract class Composant {

    private final String UID;
    private final int maxEntree;
    private final int maxSortie;

    private Point2D coordonnees;
    private List<Fil> entree;
    private List<Fil> sortie;

    public Composant(int maxEntree, int maxSortie, double x, double y) {
        this.UID = UUID.randomUUID().toString();
        this.coordonnees = new Point2D(x, y);
        this.entree = new ArrayList<>();
        this.sortie = new ArrayList<>();
        this.maxEntree = maxEntree;
        this.maxSortie = maxSortie;
    }

    /**
     * Retourne un identifiant unique pour ce composant.
     */
    public String getUID() {
        return UID;
    }

    /**
     * Retourne la position 2D (x, y) de ce composant.
     */
    public Point2D getCoordonnees() {
        return coordonnees;
    }

    /**
     * Met à jour les coordonnées de ce composant.
     */
    public void setCoordonnees(double x, double y) {
        this.coordonnees.setCoordonnees(x, y);
    }

    /**
     * Retourne la coordonnée X.
     */
    public double getX() {
        return this.coordonnees.getX();
    }

    /**
     * Retourne la coordonnée Y.
     */
    public double getY() {
        return this.coordonnees.getY();
    }

    /**
     * Retourne la liste des fils d'entrée connectés à ce composant.
     */
    public List<Fil> getEntree() {
        return this.entree;
    }

    /**
     * Retourne la liste des fils de sortie connectés à ce composant.
     */
    public List<Fil> getSortie() {
        return this.sortie;
    }

    /**
     * Connecte un fil en entrée si le nombre maximum n'est pas atteint.
     * @return true si la connexion a été ajoutée, false sinon.
     */
    public boolean connecterEntree(Fil fil) {
        if (this.entree.size() < this.maxEntree) {
            this.entree.add(fil);
            return true;
        }
        return false;
    }

    /**
     * Connecte un fil en sortie si le nombre maximum n'est pas atteint.
     * @return true si la connexion a été ajoutée, false sinon.
     */
    public boolean connecterSortie(Fil fil) {
        if (this.sortie.size() < this.maxSortie) {
            this.sortie.add(fil);
            return true;
        }
        return false;
    }

    /**
     * Déconnecte un fil d'entrée.
     * @return true si le fil a été supprimé, false sinon.
     */
    public boolean deconnecterEntree(Fil fil) {
        return this.entree.remove(fil);
    }

    /**
     * Déconnecte un fil de sortie.
     * @return true si le fil a été supprimé, false sinon.
     */
    public boolean deconnecterSortie(Fil fil) {
        return this.sortie.remove(fil);
    }

    public int getMaxEntree() {
        return this.maxEntree;
    }

    public int getMaxSortie() {
        return this.maxSortie;
    }

    /**
     * Récupère la liste des états (Signal) de tous les fils d'entrée.
     * Les sous-classes (PorteAnd, PorteOr, etc.) peuvent s'appuyer dessus pour calculer la sortie.
     */
    protected List<State> getInputStates() {
        List<State> inputStates = new ArrayList<>();
        for (Fil f : this.entree) {
            inputStates.add(f.getEtat());
        }
        return inputStates;
    }

    /**
     * Méthode abstraite que chaque sous-classe doit implémenter
     * pour calculer la sortie logique en fonction des entrées.
     */
    public abstract State evaluerSignal();

    /**
     * Réinitialise l'état interne du composant, si nécessaire.
     * Par défaut, ne fait rien. Les sous-classes peuvent la surcharger.
     */
    public void reset() {
        // Par défaut, ne rien faire.
        // Les sous-classes (ex. bascules, mémoires) peuvent la redéfinir pour remettre un état interne à UNDEFINED.
    }
}
