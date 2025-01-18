package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

public class PauliXgate extends HadamardGate {
    private final static int gate_length = 1;
    private final static IExpr matrix = F.eval("{{0, 1}, {1, 0}}");

    public static void apply(QuantumRegister reg, int... target_qubits) {
        apply(gate_length, matrix, reg, target_qubits);
    }
}
