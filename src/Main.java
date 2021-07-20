
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;


public class Main {
    private static boolean displayTokens = true;
    private static boolean displayParseTree = true;
    private static boolean displayEval = true;

    public static void main(String[] args) throws IOException{
        //initalise the lexer and the parser for a particular grammar.
        Lexer lexer = new Lexer();
        Parser parser = new Parser(
                "S -> E $ ",
                "E -> E + M | M ",
                "M -> M - T | T ",
                "T -> F * T | F  ",
                "F -> - G | cos G | G ",
                "G -> G ! | ( E ) | N ");

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(System.in));

        System.out.println("Type 'tokens' to toggle displaying tokens");
        System.out.println("Type 'parsetree' to toggle displaying parse tree");
        System.out.println("Type 'evaluation' to toggle displaying the expression evaluated");

        //main loop
        while(true){
            System.out.println("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            switch (line.toLowerCase(Locale.ROOT)){
                case "tokens":
                    displayTokens = !displayTokens;
                    System.out.println("Tokens display is " + (displayTokens ? "on." : "off."));
                    break;
                case "parsetree":
                    displayParseTree = !displayParseTree;
                    System.out.println("Parse tree display is " + (displayParseTree ? "on." : "off."));
                    break;
                case "evaluation" :
                    displayEval = !displayEval;
                    System.out.println("Evaluation display is " + (displayEval ? "on." : "off."));
                    break;
                default:
                    //if none of the predefined commands are input, then execute the line.
                    if (!line.isEmpty())
                            run(line, lexer, parser); break;
            }
        }
    }

    private static void run(String source, Lexer lexer, Parser parser) {
        try {
            List<Token> tokens = lexer.scanTokens(source);
            ParseTree parseTree = parser.parse(tokens);

            //Print the display information.
            if (displayTokens) {
                System.out.println("Tokens: ");
                System.out.println(tokens);
                System.out.println();
            }
            if (displayParseTree) {
                System.out.println("Parse Tree: ");
                System.out.println(parseTree.drawTree());
            }
            if (displayEval) {
                System.out.println("Evaluation: ");
                System.out.println(Evaluator.evaluate(parseTree));
            }

        }
        catch (SyntaxError pe){
            errorReport("Syntax Error: " +pe.getMessage());
        }
        catch (EvalError pe){
            errorReport("Eval Error: " +pe.getMessage());
        }
    }

    //Prints the error report
    public static void errorReport(String message){
        System.err.println(message);
    }

}
