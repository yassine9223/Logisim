package modelememoire;

/**
 * Un "Pin" est une simple entrée/sortie qui peut être HIGH ou LOW,
 * contrôlée manuellement (par l'utilisateur).
 */
public class Pin extends Composant {

    private State etat;

    public Pin(double x, double y) {
        super(0, 1, x, y); // 0 entrées, 1 sortie
        this.etat = State.LOW;
    }

    public void setEtat(State etat) {
        this.etat = etat;
    }

    @Override
    public State evaluerSignal() {
        // La sortie du Pin est l'état interne
        return etat;
    }

    @Override
    public void reset() {
        // On repasse en LOW par défaut
        this.etat = State.LOW;
    }
}
