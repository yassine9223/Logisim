package modelememoire;

import java.util.List;

/**
 * Implémente la logique d'une porte OR.
 * La sortie est HIGH si au moins une entrée est HIGH, sinon LOW.
 */
public class PorteOr extends Composant {

    public PorteOr(double x, double y) {
        super(2, 1, x, y);
    }

    @Override
    public State evaluerSignal() {
        List<State> inputs = getInputStates();
        System.out.println("Porte OR - États des entrées : " + inputs);  // 🛠️ Debug

        if (inputs.isEmpty()) {
            System.out.println("Porte OR - Retourne UNDEFINED");
            return State.UNDEFINED;
        }

        for (State s : inputs) {
            if (s == State.ERROR) {
                System.out.println("Porte OR - Retourne ERROR");
                return State.ERROR;
            }
        }
        
        for (State s : inputs) {
            if (s == State.HIGH) {
                System.out.println("Porte OR - Retourne HIGH");
                return State.HIGH;
            }
        }

        for (State s : inputs) {
            if (s == State.UNDEFINED) {
                System.out.println("Porte OR - Retourne UNDEFINED");
                return State.UNDEFINED;
            }
        }

        System.out.println("Porte OR - Retourne LOW");
        return State.LOW;
    }


}
