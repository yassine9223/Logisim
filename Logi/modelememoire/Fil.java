package modelememoire;

public class Fil {
    private State etat;
    private Composant composantDepart;
    private Composant composantArrive;
    private Point2D debut;
    private Point2D fin;

    /**
     * Construit un fil reliant un composant de départ à un composant d'arrivée.
     * @param composantDepart Le composant source (doit être non null).
     * @param composantArrive Le composant destination (doit être non null).
     */
    public Fil(Composant composantDepart, Composant composantArrive) {
        if (composantDepart == null || composantArrive == null) {
            throw new IllegalArgumentException("Les composants de départ et d'arrivée ne peuvent pas être null.");
        }
        this.composantDepart = composantDepart;
        this.composantArrive = composantArrive;
        this.etat = State.UNDEFINED;
        mettreAJourPosition();
    }

    /**
     * Renvoie l'état actuel du fil en recalculant l'état à partir du composant de départ.
     * @return L'état actuel du fil.
     */
    public State getEtat() {
        return effectuerCalcul();
    }

    public Composant getComposantDepart() {
        return composantDepart;
    }

    public Composant getComposantArrive() {
        return composantArrive;
    }

    public Point2D getDebut() {
        return debut;
    }

    public Point2D getFin() {
        return fin;
    }

    public void setEtat(State etat) {
        this.etat = etat;
    }

    /**
     * Définit le composant de départ et met à jour les coordonnées.
     * @param nouveauComposant Le nouveau composant source (non null).
     */
    public void setComposantDepart(Composant nouveauComposant) {
        if (nouveauComposant == null) {
            throw new NullPointerException("Le composant de départ ne peut pas être null.");
        }
        this.composantDepart = nouveauComposant;
        mettreAJourPosition();
    }

    /**
     * Définit le composant d'arrivée et met à jour les coordonnées.
     * @param nouveauComposant Le nouveau composant d'arrivée (non null).
     */
    public void setComposantArrive(Composant nouveauComposant) {
        if (nouveauComposant == null) {
            throw new NullPointerException("Le composant d'arrivée ne peut pas être null.");
        }
        this.composantArrive = nouveauComposant;
        mettreAJourPosition();
    }

    /**
     * Met à jour les coordonnées de début et de fin du fil en fonction des positions
     * des composants de départ et d'arrivée.
     */
    public void mettreAJourPosition() {
        if (composantDepart != null) {
            this.debut = new Point2D(composantDepart.getX(), composantDepart.getY());
        }
        if (composantArrive != null) {
            this.fin = new Point2D(composantArrive.getX(), composantArrive.getY());
        }
    }

    /**
     * Calcule et met à jour l'état du fil en se basant sur le signal produit par le composant de départ.
     * Si une exception survient lors du calcul, l'état passe à ERROR.
     * @return L'état mis à jour.
     */
    public State effectuerCalcul() {
        if (composantDepart == null) {
            this.etat = State.UNDEFINED;
        } else {
            try {
                this.etat = composantDepart.evaluerSignal();
            } catch (Exception e) {
                this.etat = State.ERROR;
            }
        }
        return this.etat;
    }
}
