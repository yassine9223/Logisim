package modelememoire;

import java.util.List;

/**
 * Implémente la logique d'une porte NOT.
 * Elle inverse l'état de sa seule entrée.
 * Si l'entrée est HIGH, la sortie sera LOW, et inversement.
 * En cas d'entrée UNDEFINED ou ERROR, la sortie sera respectivement UNDEFINED ou ERROR.
 */
public class PorteNot extends Composant {

    public PorteNot(double x, double y) {
        super(1, 1, x, y);
    }

    @Override
    public State evaluerSignal() {
        List<State> inputs = getInputStates();
        System.out.println("Porte NOT - État des entrées : " + inputs);
        
        if (inputs.isEmpty()) {
            return State.UNDEFINED;
        }
        
        State s = inputs.get(0); // Une porte NOT n'a qu'une seule entrée
        if (s == State.ERROR) {
            return State.ERROR;
        }
        if (s == State.UNDEFINED) {
            return State.UNDEFINED;
        }
        
        State output = (s == State.HIGH) ? State.LOW : State.HIGH;
        System.out.println("Porte NOT - Sortie :"  + output );
        return output;
    }
    

}
