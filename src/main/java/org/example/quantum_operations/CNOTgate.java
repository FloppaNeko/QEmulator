package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

public class CNOTgate extends QunatumGate {
    private static final int gate_length = 2;
    private static final IExpr matrix = F.eval("{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}}");

    public static void apply(QuantumRegister reg, int... target_qubits) {
        apply(gate_length, matrix, reg, target_qubits);
    }
}
