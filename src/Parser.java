import java.util.List;
import java.util.Stack;

public class Parser {

    List<Token> source;
    Stack<ParseTree> parseForest;
    StateManager grammar;
    int index;
    BiMap<State, String, Action> actionTable;
    BiMap<State, String, State> gotoTable;

    //Given a set of production rules, create the action and goto tables
    Parser(String... productionRules){
        grammar = new StateManager(productionRules);
        actionTable = grammar.actionTable;
        gotoTable = grammar.gotoTable;
    }

    //Use the LR parsing algorithm to
    public ParseTree parse(List<Token> tokens) throws SyntaxError {
        index = 0;
        source = tokens;
        ParseTree startPT = new ParseTree(grammar.startState, new Symbol("$"));
        String symbol = "$";
        parseForest = new Stack<>();
        parseForest.add(startPT);

        while(true){
            Token token = null;
            ParseTree pt = parseForest.peek();
            State state = pt.getState();

            if (index < tokens.size()){
                token = peek();
                symbol = token.symbol.symbol;}

            Action action = actionTable.get(state, symbol);

            switch (action.move) {
                case REDUCE -> reduce(state);
                case SHIFT -> shift(action.state, token);
                case ACCEPT -> {
                    reduce(state);
                    return accept(); }
                case ERROR -> {
                    error(token);
                    return null;  }
            }
        }
    }

    //peek the next token without consuming it
    public Token peek(){
        return source.get(index);
    }

    //reduce
    public void reduce(State state){
        state.reduce(gotoTable, parseForest);
    }

    //shift
    public void shift(State t, Token a){

        parseForest.add(new ParseTree(t, a));
        index++;
    }

    //accept -- returns the parse tree
    public ParseTree accept() {
            return parseForest.pop();
    }

    //when the tokens are ungrammatical, throw a syntax error
    private void error(Token token) throws SyntaxError {
        if (token != null) {
            String sym = token.symbol.symbol;
            if (sym.equals("N"))
                sym = token.value.toString();
            if (sym.equals("$"))
                throw new SyntaxError("Invalid syntax at end of file");
            throw new SyntaxError("Invalid syntax '" + sym + "' at position " + token.position);
        }
    }

}
