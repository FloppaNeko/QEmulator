package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

public class HadamardGate extends QunatumGate {
    private final static int gate_length = 1;
    private final static IExpr matrix = F.eval("(1/Sqrt(2) * {{1, 1}, {1, -1}})");

    public static void apply(QuantumRegister reg, int target_qubit) {
        apply(gate_length, matrix, reg, new int[]{target_qubit});
    }
}
