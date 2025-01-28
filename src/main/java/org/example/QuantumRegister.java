package org.example;

import org.matheclipse.core.expression.F;
import java.util.Arrays;


public class QuantumRegister {
    public int length;
    public QuantumState[] states;

    public QuantumRegister(int _length) {
        this(_length, 0);
    }

    public QuantumRegister(int _length, int _initial_state) {
        length = _length;
        states = new QuantumState[1 << _length];
        Arrays.fill(states, null);

        if (_initial_state != -1) {
            states[_initial_state] = new QuantumState(length, _initial_state, F.CC(1, 0));
        }
    }

    public QuantumRegister unite(QuantumRegister oth) {
        QuantumRegister new_reg = new QuantumRegister(this.length + oth.length, -1);

        for (QuantumState qs1 : this.states) {
            if (qs1 == null || qs1.isZero()) {
                continue;
            }

            for (QuantumState qs2: oth.states) {
                if (qs2 == null || qs2.isZero()) {
                    continue;
                }

                int q = (qs1.state << qs2.length) + qs1.state;
                new_reg.states[q] = new QuantumState(
                        new_reg.length,
                        q,
                        F.Times(qs1.coefficient, qs2.coefficient)
                );
            }
        }
        return new_reg;
    }

    @Override
    public String toString() {
        return String.join(" \\ ", Arrays.stream(states).map((s) -> {
            if (s == null) {
                return "\\text{null}";
            } else {
                return s.toString();
            }
        }).toList());
    }

    public void print() {
        for (QuantumState qs : states) {
            if (qs == null) {
                continue;
            }

            System.out.print(qs.toString() + " : ");
            System.out.println(Arrays.toString(qs.predecessors));
        }
        System.out.println();
    }
}
