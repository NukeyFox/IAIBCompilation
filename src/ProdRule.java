import java.util.List;

public class ProdRule {
    Symbol left;
    List<Symbol> handle;
    int dotPos;

    ProdRule(Symbol left, List<Symbol> handle, int dotPos){
        this.left = left;
        this.handle = handle;
        this.dotPos = dotPos;
    }

    public Symbol lastSymbol(){
        return handle.get(handle.size()-1);
    }

    public boolean dotAtNonterminal() {
        return !dotLast() && handle.get(dotPos).nonTerminal;
    }

    public Symbol dottedSymbol() {
        if (!dotLast())
            return handle.get(dotPos);
        return null;
    }

    public ProdRule moveDot() {
        if (dotPos < handle.size())
            return new ProdRule(left, handle, dotPos + 1);
        return null;
    }

    @Override
    public String toString() {
        String out = left + "->";
        for (int i = 0; i < handle.size(); i++){
            if (i == dotPos)
                out += "•";
            out += handle.get(i);
        }
        if (dotPos >= handle.size())
            out += "•";
        return  out;
    }

    @Override
    public int hashCode() {
        return left.hashCode() + handle.hashCode() + dotPos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdRule){
            ProdRule other = (ProdRule) obj;
            return other.handle.equals(this.handle)
                    && other.left.equals(this.left)
                    && (other.dotPos == this.dotPos);
        }
        return false;
    }

    public boolean dotLast() {
        return (dotPos >= handle.size());
    }
}
