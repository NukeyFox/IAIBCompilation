public class Token {

    final TokenType type;
    final Object value;
    final Symbol symbol;
    final int position;

    public Token(TokenType type, Object value, int position){
        this.type = type;
        this.value = value;
        this.symbol = getSymbol();
        this.position = position;
    }

    public Symbol getSymbol(){
        switch (type){
            case PLUS: return new Symbol("+", false, this);
            case MINUS: return new Symbol("-", false, this);
            case STAR: return new Symbol("*", false, this);
            case DOT: return new Symbol(".", false, this);
            case BANG: return new Symbol("!", false, this);
            case LEFT_BRACKET: return new Symbol("(", false, this);
            case RIGHT_BRACKET: return new Symbol(")", false, this);
            case COS: return new Symbol("cos", false, this);
            case NUMBER: return new Symbol("N", false, this);
            case EOF: return new Symbol("$", false, this);
            default: return null;
        }
    }

    @Override
    public String toString() {
        String val = (value == null)? "" : " : " + value.toString();
        return type + val;
    }
}
