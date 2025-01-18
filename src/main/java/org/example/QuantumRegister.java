package org.example;

import org.matheclipse.core.expression.F;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class QuantumRegister {
    public int length;

    public QuantumState[] states;

    public QuantumRegister(int _length) {
        length = _length;
        states = new QuantumState[1 << _length];
        Arrays.fill(states, null);

        states[0] = new QuantumState(length, 0, F.CC(1, 0));
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
