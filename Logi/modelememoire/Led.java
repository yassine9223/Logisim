package modelememoire;

/**
 * Une LED reçoit 1 entrée, et n'a pas vraiment de sortie
 * (mais on peut la considérer comme 1 sortie = l'état actuel de la LED).
 */
public class Led extends Composant {

    private State etatInterne;

    public Led(double x, double y) {
        super(1, 0, x, y); // 1 entrée, 0 sorties
        this.etatInterne = State.LOW;
    }

    @Override
    public State evaluerSignal() {
        if (getEntree().isEmpty()) {
            etatInterne = State.UNDEFINED;
        } else {
            State s = getEntree().get(0).getEtat();
            etatInterne = s;
        }
        
        System.out.println("LED - Entrées : " + (getEntree().isEmpty() ? "Aucune" : getEntree().get(0).getEtat()) +
                           ", État actuel : " + etatInterne);
        
        return etatInterne;
    }
    

    @Override
    public void reset() {
        etatInterne = State.LOW;
    }

    /**
     * Retourne l'état interne, pour savoir si la LED est allumée ou éteinte
     */
    public State getEtatInterne() {
        return etatInterne;
    }
}
