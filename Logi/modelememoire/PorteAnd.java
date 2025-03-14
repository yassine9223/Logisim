package modelememoire;

import java.util.List;

/**
 * Implémente la logique d'une porte AND.
 * Si toutes les entrées sont HIGH, la sortie est HIGH.
 * Si une seule entrée est LOW, la sortie est LOW.
 * Si l'une des entrées est UNDEFINED ou ERROR, la sortie sera respectivement UNDEFINED ou ERROR.
 */
public class PorteAnd extends Composant {

    public PorteAnd(double x, double y) {
        super(2, 1, x, y);
    }

    @Override
    public State evaluerSignal() {
        List<State> inputs = getInputStates();
        if (inputs.isEmpty()) {
            return State.UNDEFINED;
        }
        for (State etat : inputs) {
            if (etat == State.ERROR) {
                return State.ERROR;
            }
            if (etat == State.UNDEFINED) {
                return State.UNDEFINED;
            }
            if (etat == State.LOW) {
                return State.LOW;
            }
        }
        return State.HIGH;
    }

}
