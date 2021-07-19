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

    //Associate each Token with a symbol
    public Symbol getSymbol(){
        return switch (type) {
            case PLUS -> new Symbol("+", false, this);
            case MINUS -> new Symbol("-", false, this);
            case STAR -> new Symbol("*", false, this);
            case DOT -> new Symbol(".", false, this);
            case BANG -> new Symbol("!", false, this);
            case LEFT_BRACKET -> new Symbol("(", false, this);
            case RIGHT_BRACKET -> new Symbol(")", false, this);
            case COS -> new Symbol("cos", false, this);
            case NUMBER -> new Symbol("N", false, this);
            case EOF -> new Symbol("$", false, this);
        };
    }

    @Override
    public String toString() {
        String val = (value == null)? "" : " : " + value.toString();
        return type + val;
    }
}
