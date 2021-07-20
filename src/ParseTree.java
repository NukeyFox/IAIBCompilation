import java.util.*;
import java.util.stream.Collectors;

public class ParseTree {

    private final Symbol item;
    private final State state;

    public State getState() {return state;}
    public Symbol getItem() {
        return item;
    }
    public boolean operator(){
        return children.size() > 1;
    }
    public ParseTree getChild(int i) {
        return children.get(i);
    }

    private List<ParseTree> children;
    int subtreeWidth;

    public ParseTree(State t, Symbol a) {
        state = t;
        item = a;
        children = new ArrayList<>();
        subtreeWidth = -1;
    }

    public ParseTree(State t, Token a) {
        this(t, a.getSymbol());
    }

    public static ParseTree makeParent(State state, Symbol item, List<ParseTree> children){
        return makeParent(new ParseTree(state, item), children);
    }

    public static ParseTree makeParent(ParseTree parent, List<ParseTree> children) {
        parent.children = children;
        return parent;
    }

    /// From here onwards is for drawing the parse tree ///

    //Get the symbol to draw
    public String getSymbolString(){
        String str = item.toString();
        if (item.token != null && item.token.value != null)
            str = item.token.value.toString();
        return str;
    }

    //Draw the tree recursively
    public String drawTree(){
        String sym = getSymbolString();

        if (children.isEmpty())
            return sym;
        else {
            String childrenSubtree = mergeChildren();
            String first = childrenSubtree.split("\n")[0];
            StringBuilder sb = new StringBuilder();
            int widthSubtree = width(first);
            int len = (widthSubtree - sym.length())/2;

            sb.append(" ".repeat(len));
            sb.append(sym);
            sb.append(" ".repeat(len));
            sb.append("\n");
            sb.append(childrenSubtree);
            return sb.toString();
        }
    }


    //draw all childrens side y side
    private String mergeChildren() {
        String out = "";
        for (ParseTree child : children)
            out = mergeTreeStrings(out, child.drawTree());
        return out;
    }

    //Draw two string of trees side by side
    private String mergeTreeStrings(String t1, String t2){
        if (t1.isEmpty())
            return t2;
        if (t2.isEmpty())
            return t1;

        List<String> s1 = t1.lines().collect(Collectors.toList());
        List<String> s2 = t2.lines().collect(Collectors.toList());
        int i = 0;
        int n = s1.size();
        int m = s2.size();
        int w1 = width(s1);
        int w2 = width(s2);
        StringBuilder sb = new StringBuilder();
        while (i < n || i < m){
            String left = " ".repeat(w1);
            String right = " ".repeat(w2);
            if (i < n)
                left = s1.get(i);
            if (i < m)
                right = s2.get(i);
            sb.append(left);
            sb.append(right);
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

    //get the width of the string
    private int width(List<String> string) {
        return string.stream().mapToInt(String::length).max().getAsInt();
    }

    private int width(String string) {
        List<String> s = string.lines().collect(Collectors.toList());
        return width(s);
    }


}
