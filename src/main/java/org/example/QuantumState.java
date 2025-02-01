package org.example;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.Arrays;

public class QuantumState {
    public int length;
    public int state;
    public IExpr coefficient;
    public boolean[] predecessors;

    public QuantumState(int _length, int _state, IExpr _coefficient) {
        length = _length;
        state = _state;
        coefficient = _coefficient;

        predecessors = new boolean[1 << _length];
        Arrays.fill(predecessors, false);
    }

    public QuantumState(int _length, int _state, IExpr _coefficient, int _prev) {
        this(_length, _state, _coefficient);
        predecessors[_prev] = true;
    }

    public void addCoefficient(QuantumState oth) {
        assert state == oth.state;
        coefficient = F.Plus(coefficient, oth.coefficient).eval();
    }

    public void addPredecessor(int prev) {
        predecessors[prev] = true;
    }

    public static int[] stateFromIntToArray(int _state, int _length) {
        int[] array = new int[_length];

        for (int i = 0; i < _length; ++i) {
            array[_length - 1 - i] = _state % 2;
            _state /= 2;
        }

        return array;
    }

    public int[] stateFromIntToArray() {
        return stateFromIntToArray(state, length);
    }

    public static int stateFromArrayToInt(int[] _state) {
        int res = 0;

        for (int i = 0; i < _state.length; ++i) {
            res = res + (_state[_state.length - 1 - i] << i);
        }

        return res;
    }

    @Override
    public String toString() {
        //System.out.println(coefficient.toString());
        String texString = F.TeXForm(F.Times(coefficient, F.eval("x")).eval()).eval().toString();
        texString = texString.replace("x", "|" + binaryState() + "\\rangle");
        //System.out.println(texString);
        return texString;
    }

    public String binaryState() {
        String str = Integer.toBinaryString(state);
        return "0".repeat(length - str.length()) + str;
    }

    public boolean isZero() {
        return exprIsZero(coefficient);
    }

    public static boolean exprIsZero(IExpr expr) {
        return F.Equal(expr, F.ZZ(0)).eval().toString().equals("True");
    }
}
