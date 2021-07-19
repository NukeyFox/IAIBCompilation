import java.util.List;

//class for handling a production rule
public class ProdRule {
    Symbol left;        //the head of the production rule
    List<Symbol> handle;//the body of the production rule that is matched
    int dotPos;         //position of the dot when

    ProdRule(Symbol left, List<Symbol> handle, int dotPos){
        this.left = left;
        this.handle = handle;
        this.dotPos = dotPos;
    }

    //returns the last symbol of the handle
    public Symbol lastSymbol(){
        return handle.get(handle.size()-1);
    }

    //returns true if the dot is on a non-terminal symbol
    public boolean dotAtNonterminal() {
        return !dotLast() && handle.get(dotPos).nonTerminal;
    }

    //get the symbol immediately right to the dot
    public Symbol dottedSymbol() {
        if (!dotLast())
            return handle.get(dotPos);
        return null;
    }

    //return the same production rule but with the dot shifted left (if there is space)
    public ProdRule moveDot() {
        if (dotPos < handle.size())
            return new ProdRule(left, handle, dotPos + 1);
        return null;
    }

    //returns true if the dot is at the end of the handle
    public boolean dotLast() {
        return (dotPos >= handle.size());
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder(left + "->");
        for (int i = 0; i < handle.size(); i++){
            if (i == dotPos)
                out.append("•");
            out.append(handle.get(i));
        }
        if (dotPos >= handle.size())
            out.append("•");
        return out.toString();
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

}
