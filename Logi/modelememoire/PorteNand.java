package modelememoire;

import java.util.List;

/**
 * Implémente la logique d'une porte NAND.
 * C'est l'inverse d'une porte AND.
 * Si une des entrées est LOW, alors AND renverrait LOW, et NAND renverra HIGH.
 * Si toutes les entrées sont HIGH, NAND renverra LOW.
 */
public class PorteNand extends Composant {

    public PorteNand(double x, double y) {
        super(2, 1, x, y);
    }

    @Override
    public State evaluerSignal() {
        List<State> inputs = getInputStates();
        if (inputs.isEmpty()) {
            return State.UNDEFINED;
        }
        // En cas d'erreur ou d'indéfini sur une entrée, retourner cet état
        for (State s : inputs) {
            if (s == State.ERROR) {
                return State.ERROR;
            }
            if (s == State.UNDEFINED) {
                return State.UNDEFINED;
            }
            // Si l'une des entrées est LOW, la porte AND retournerait LOW, donc NAND retourne HIGH
            if (s == State.LOW) {
                return State.HIGH;
            }
        }
        // Si toutes les entrées sont HIGH, alors NAND retourne LOW.
        return State.LOW;
    }

}
