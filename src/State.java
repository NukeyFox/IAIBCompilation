import java.util.*;

public class State {
    //State is a set of production rule
    StateManager grammar;
    private final Set<ProdRule> items;
    private ProdRule reducingPR;
    boolean acceptState;
    Boolean isReducingState;

    public State(StateManager grammar, Set<ProdRule> items) {
        this.grammar = grammar;
        this.items = items;
        isReducingState = isReducingState();

        this.acceptState = ((isReducingState)
                && (reducingPR.handle.isEmpty()
                || reducingPR.lastSymbol().equals("$")));
    }

    //Returns true if the current state is a reducing state, i.e. the sequence of tokens matches the handle
    public boolean isReducingState(){
        if (isReducingState == null){
            boolean cases = false;
            for (ProdRule x : items) {
                cases = x.dotLast();
                if (cases) {
                    reducingPR = x;
                    break;
                }
            }
            isReducingState = cases;
            return cases;
        }
        else
            return isReducingState;
    }

    //Give a gotoTable and the stack of parsing
    public void reduce(BiMap<State, String, State> gotoTable, Stack<ParseTree> parseForest) {
        if (isReducingState){
            List<Symbol> handle = reducingPR.handle;
            int n = handle.size();
            Symbol head = reducingPR.left;
            List<ParseTree> children = new LinkedList<>();

            //pop off the n items from the stack
            for (int i = 0; i < n; i++){
                ParseTree tree = parseForest.pop();
                children.add(0, tree);
            }

            //add the reduced state and symbol
            State nextState = gotoTable.get(parseForest.peek().getState(), head.symbol);
            ParseTree pt = ParseTree.makeParent(nextState, head, children);
            parseForest.add(pt);

        }
    }

    //Return the next state if this state is given input a
    public State GOTO(String a) {
        Set<ProdRule> out = new HashSet<>();
        for (ProdRule pr : items) {
            if (!pr.dotLast() && pr.dottedSymbol().equals(a)) {
                out.add(pr.moveDot());
            }
        }
        out.addAll(grammar.closure(out));
        return new State(grammar, out);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State){
            State other = (State) obj;
            return other.grammar.equals(this.grammar) &&
                    other.items.equals(this.items);
        }
        return false;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }


}
