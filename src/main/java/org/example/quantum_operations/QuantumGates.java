package org.example.quantum_operations;


public class QuantumGates {
    private QuantumGates() {}

    public final static HadamardGate Hadamard = new HadamardGate();
    public final static CNOTgate CNot = new CNOTgate();
    public final static AddInt Add = new AddInt();
    public final static CAddInt CAdd = new CAddInt();
    public final static Measurement Measure = new Measurement();
}
