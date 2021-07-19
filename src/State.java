import java.util.*;

public class State {
    StateManager grammar;
    private Set<ProdRule> items;
    public ProdRule reducingPR;
    boolean acceptState;
    public Symbol reducingSymbol;
    Boolean isReducingState;
    Symbol label;

    public State(StateManager grammar, Set<ProdRule> items, Symbol label) {
        this.grammar = grammar;
        this.items = items;
        this.label = label;
        isReducingState = isReducingState();

        this.acceptState = ((isReducingState)
                && (reducingPR.handle.isEmpty()
                || reducingPR.lastSymbol().equals("$")));
    }

    public State(StateManager grammar, Set<ProdRule> items){
        this(grammar, items, null);
    }

    public Action action(Token a) {
        if (isReducingState())
            return null;
        else
            return null;
    }

    public boolean isReducingState(){
        if (isReducingState == null){
            boolean cases = false;
            for (ProdRule x : items) {
                cases = cases || x.dotLast();
                if (cases) {
                    reducingPR = x;
                    reducingSymbol = reducingPR.lastSymbol();
                    break;
                }
            }
            isReducingState = cases;
            return cases;
        }
        else
            return isReducingState;
    }

    public void reduce(BiMap<State, String, State> gotoTable, Stack<ParseTree> parseForest) {
        if (isReducingState){
            List<Symbol> handle = reducingPR.handle;
            int n = handle.size();
            Symbol head = reducingPR.left;
            List<ParseTree> children = new LinkedList<>();

            for (int i = 0; i < n; i++){
                ParseTree tree = parseForest.pop();
                children.add(0, tree);
            }

            State nextState = gotoTable.get(parseForest.peek().state, head.symbol);
            ParseTree pt = ParseTree.makeParent(nextState, head, children);
            parseForest.add(pt);

        }
    }


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

    public State GOTO(Symbol a){
        return GOTO(a.symbol);
    }

    public Set<ProdRule> getItems() {
        return this.items;
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
