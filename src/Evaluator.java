public class Evaluator {

    public static double evaluate(ParseTree parseTree) throws MathsError {

        if (parseTree == null)
            return 0;

        Symbol symbol = parseTree.getItem();
        String first;
        switch (symbol.symbol){
            case "S": return evaluateSingle(parseTree);
            case "N": return (double)  symbol.token.value;
            case "E":
            case "M":
            case "T":
                    if (parseTree.operator())
                            return evaluateBinOp(parseTree);
                      else
                          return evaluateSingle(parseTree);
            case "F":
                    first = parseTree.getChild(0).getItem().symbol;
                    switch (first){
                        case "(": return evaluateBracket(parseTree);
                        case "cos":
                        case "-": return evaluatePrefixUnaryOp(parseTree);
                        default:
                            return evaluateSingle(parseTree);
                    }
            case "G":
               first = parseTree.getChild(0).getItem().symbol;
                switch (first) {
                    case "(":
                        return evaluateBracket(parseTree);
                    default:
                        if (parseTree.operator())
                            return evaluateSuffixUnaryOp(parseTree);
                        else
                            return evaluateSingle(parseTree);
                }
            default: return 0;

        }

    }

    private static double evaluateSingle(ParseTree pt) throws MathsError {
        return evaluate(pt.getChild(0));
    }

    private static double evaluateBracket(ParseTree pt) throws MathsError {
        return evaluate(pt.getChild(1));
    }

    private static double evaluateBinOp(ParseTree pt) throws MathsError {
        ParseTree operand1 = pt.getChild(0);
        ParseTree operator = pt.getChild(1);
        ParseTree operand2 = pt.getChild(2);

        switch (operator.getItem().symbol){
            case "-" : return evaluate(operand1) - evaluate(operand2);
            case "+" : return evaluate(operand1) + evaluate(operand2);
            case "*" : return evaluate(operand1) * evaluate(operand2);
            default: return 0;
        }
    }

    private static double evaluatePrefixUnaryOp(ParseTree pt) throws MathsError {
        ParseTree operator = pt.getChild(0);
        ParseTree operand = pt.getChild(1);
        switch (operator.getItem().symbol){
            case "cos" : return Math.cos(evaluate(operand)) ;
            case "-" : return (- evaluate(operand));
            default: return 0;
        }
    }

    private static double evaluateSuffixUnaryOp(ParseTree pt) throws MathsError {
        ParseTree operand = pt.getChild(0);
        ParseTree operator = pt.getChild(1);
        switch (operator.getItem().symbol){
            case "!" : return factorial(evaluate(operand)) ;
            default: return 0;
        }
    }

    private static double factorial(double n) throws MathsError{
        double k = 1;
        if (n < 0){
            throw new MathsError("Factorial of negative number");
        }
        else
            for (int i = 1; i <= n; i++ )
                k = k * i;
        return k;
    }

}
