import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class StateManager {

    @Override
    public String toString() {
        return grammar.toString();
    }

    public State startState;
    ProdRule startProdRule;
    Set<ProdRule> grammar;
    Set<String> symbols;
    Set<String> nonTerminalSymbols;
    Set<String> terminalSymbols;
    Set<State> states;
    BiMap<State, String, Action> actionTable;
    BiMap<State, String, State> gotoTable;

    public StateManager(String... productionRules){
        actionTable = new BiMap<>();
        gotoTable = new BiMap<>();
        createGrammar(productionRules);
        computeCanonicalCollection();

    }

    public Set<ProdRule> createGrammar(String... productionRules){
        symbols = new HashSet<>();
        grammar = new HashSet<>();
        this.nonTerminalSymbols = new HashSet<>();

        for (String s : productionRules) {
            int a = 0;
            String symbol = "";
            while (a < s.length() && s.charAt(a) != ' '){
                symbol += s.charAt(a);
                a++;
            }
            symbols.add(symbol);
            nonTerminalSymbols.add(symbol);
        }

        for (String s : productionRules) {
            int i = 0;
            String symbol = "";
            String left = "";
            List<Symbol> right = new ArrayList<>();
            while (i < s.length()) {
                char c = s.charAt(i);
                switch (c) {
                    // Test for "->" if not "-" is part of a symbol
                    case '-':
                        if ((i < s.length() - 1) && (s.charAt(i + 1) != '>'))
                            symbol += c;
                        else {
                            left = symbol;
                            symbol = "";
                            i++; }
                        break;
                    // Test for the divider "|", if so
                    case '|':
                        Symbol l = new Symbol(left, true);
                        ProdRule pr = new ProdRule(l, right, 0);
                        grammar.add(pr);
                        symbol = "";
                        right = new ArrayList<>();
                        break;
                    // A space is encountered
                    case ' ':
                        if (left != "" && symbol != "") {
                            Symbol r = new Symbol(symbol, nonTerminalSymbols.contains(symbol));
                            right.add(r);
                            symbols.add(symbol);
                            symbol = "";};
                        break;
                    default:
                        symbol += c;
                        break;
                }
                i++;
            }
            Symbol l = new Symbol(left, true);
            ProdRule pr = new ProdRule(l, right, 0);
            grammar.add(pr);
        }
        for (ProdRule pr : grammar){
            if (pr.left.equals("S")){
                startProdRule = pr;
                break;
            }
        }
        terminalSymbols = new HashSet<>();
        for (String s : symbols){
            if (!nonTerminalSymbols.contains(s))
                terminalSymbols.add(s);
        }

        return grammar;
    }


    public Set<ProdRule> getProdRule(Symbol sym) {
        return grammar.stream()
                .filter(x -> x.left.equals(sym))
                .collect(toCollection(HashSet::new));
    }

    public Set<ProdRule> closure(Set<ProdRule> items){
        Stack<ProdRule> stack = new Stack();
        Set<ProdRule> seen = new HashSet<>();
        for (ProdRule pr : items) {
            stack.add(pr);
            seen.add(pr);
        }
        while (!stack.isEmpty()) {
                ProdRule pr = stack.pop();
                seen.add(pr);

                if (pr.dotAtNonterminal()) {
                    Symbol sym = pr.dottedSymbol();

                    for (ProdRule pr2 : getProdRule(sym)) {
                        if (!seen.contains(pr2)) {
                            stack.add(pr2);
                        }
                    }
                }
            }
        return seen;
    }

    public Set<ProdRule> closure(ProdRule item){
        Set<ProdRule> set = new HashSet<>();
        set.add(item);
        return closure(set);
    }

    public Set<State> computeCanonicalCollection(){
        Set<State> seen = new HashSet<>();
        Stack<State> C = new Stack<>();
        startState = new State(this, closure(startProdRule));
        C.add(startState);

        while(!C.isEmpty()){
            State state = C.pop();
            seen.add(state);


            for (String sym : terminalSymbols){
                State t = state.GOTO(sym);
                if (!seen.contains(t))
                    C.add(t);
                if (state.isReducingState){
                    //accept
                    if (sym.equals("$") && state.acceptState)
                        actionTable.put(state,sym,new Action(Action.Move.ACCEPT, state));
                    else
                    //reduce
                    if (t.isEmpty())
                        actionTable.put(state, sym, new Action(Action.Move.REDUCE, state));
                    else
                        actionTable.put(state, sym, new Action(Action.Move.SHIFT, t));
                }
                else
                    //shift
                    if (!t.isEmpty())
                        actionTable.put(state, sym, new Action(Action.Move.SHIFT, t));
                    //error
                    else
                        actionTable.put(state, sym, new Action(Action.Move.ERROR, t));
            }
            for (String sym : nonTerminalSymbols){
                State t = state.GOTO(sym);
                if (!seen.contains(t))
                        C.add(t);
                if (!t.isEmpty()) {
                    gotoTable.put(state, sym, t);
                }
            }
        }
        states = seen;
        return seen;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StateManager){
            StateManager other = (StateManager) obj;
            return other.grammar.equals(this.grammar);
        }
        return false;
    }
}
