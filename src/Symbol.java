public class Symbol {
    final String symbol;
    final boolean nonTerminal;
    final Token token;

    Symbol(String symbol,  boolean nonTerminal, Token token){
        this.symbol = symbol;
        this.nonTerminal = nonTerminal;
        this.token = token;
    }

    Symbol(String symbol,  boolean nonTerminal){
        this.symbol = symbol;
        this.nonTerminal = nonTerminal;
        this.token = null;
    }

    Symbol(String symbol){
        this.symbol = symbol;
        this.nonTerminal = false;
        this.token = null;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String){
            String other = (String) obj;
            return symbol.equals(other);
        }
        if (obj instanceof Symbol){
            Symbol other = (Symbol) obj;
            return symbol.equals(other.symbol);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
