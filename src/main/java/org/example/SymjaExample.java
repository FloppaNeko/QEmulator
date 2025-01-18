package org.example;

import org.hipparchus.analysis.function.Sqrt;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IExpr;

public class SymjaExample {
    private static String toLatex(IExpr expr) {
        return F.TeXForm(expr).eval().toString();
    }

    public static void main(String[] args) {
        ExprEvaluator evaluator = new ExprEvaluator();

        IExpr qubit = F.eval("Sqrt(2)/2");

        // qubit = F.Times(F.eval("Exp(I*-Pi)"));

        System.out.println(toLatex(F.Simplify(qubit)));
    }
}
