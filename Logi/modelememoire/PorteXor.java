package modelememoire;

import java.util.List;

/**
 * Implémente la logique d'une porte XOR.
 * La sortie est HIGH si un nombre impair d'entrées est HIGH, LOW sinon.
 * Si l'une des entrées est UNDEFINED ou ERROR, le résultat sera respectivement UNDEFINED ou ERROR.
 */
public class PorteXor extends Composant {

    public PorteXor(double x, double y) {
        super(2, 1, x, y);
    }
    
    @Override
    public State evaluerSignal() {
        List<State> inputs = getInputStates();
        if (inputs.isEmpty()) {
            return State.UNDEFINED;
        }
        int count = 0;
        for (State s : inputs) {
            if (s == State.ERROR) {
                return State.ERROR;
            }
            if (s == State.UNDEFINED) {
                return State.UNDEFINED;
            }
            if (s == State.HIGH) {
                count++;
            }
        }
        return (count % 2 == 1) ? State.HIGH : State.LOW;
    }

}
