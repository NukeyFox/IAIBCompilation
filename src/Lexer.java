import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Lexer {
    private String source;  //source code
    private List<Token> tokens = new ArrayList<>();
    //two indices used to keep track of a range of characters
    private int start = 0;
    private int current = 0;
    private static final Map<String, TokenType> keywords;

    //Using keywords just in case I want to add more functionality later
    static {
        keywords = new HashMap<>();
        keywords.put("cos", TokenType.COS);
    }

    private boolean isAtEnd(){
        //if program is at the end
        return current >= source.length();
    }

    private char advance(){
        //return the current character and move to the next
        return source.charAt(current++);
    }

    //add Token to the list
    private void addToken(TokenType type, Object value){
        String lexeme = source.substring(start, current);
        tokens.add(new Token(type, value, start));
    }
    private void addToken(TokenType type){
        addToken(type, null);
    }

    private char peek() {
        if (current < source.length())
             return source.charAt(current);
        return '\0';
    }
    private char peekNext() {
        if (current + 1 < source.length())
            return source.charAt(current + 1);
        return '\0';
    }

    //code to deal with numbers
    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private boolean isFloat(char c){
        return isDigit(c) || (c == '.');
    }

    private void number(){
        while (isDigit(peek()))
            advance();

        //Look for fractional part
        if (peek() == '.' && isDigit(peekNext())){
            advance();
            advance();}

        while (isDigit(peek()))
            advance();

        String number = source.substring(start, current);

        addToken(TokenType.NUMBER, Double.parseDouble(number));
    }

    //code to deal with strings and identifiers
    private boolean isAlpha(char c){
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    //Checks if any keywords match the string
    private void checkForKeyword() throws SyntaxError {
        while (isAlpha(peek()))
            advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null)
            throw new SyntaxError("Unexpected token '" + text + "' at " + start);

        addToken(type);
    }

    //Main scanner
    List<Token> scanTokens(String source) throws SyntaxError {
        tokens = new ArrayList<>();
        start = 0;
        current = 0;
        this.source = source;
        //
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, null, source.length()));
        return tokens;
    }

    //Scans individual tokens. If an unrecognised token is found then throw an error with information on where the error is.
    private void scanToken() throws SyntaxError {
        char c = advance();
        switch (c){
            case '(' : addToken(TokenType.LEFT_BRACKET); break;
            case ')' : addToken(TokenType.RIGHT_BRACKET); break;
            case '-' : addToken(TokenType.MINUS); break;
            case '+' : addToken(TokenType.PLUS); break;
            case '*' : addToken(TokenType.STAR); break;
            case '!' : addToken(TokenType.BANG); break;
            //ignore all whitespaces
            case ' ':
            case '\r':
            case '\t':
            case '\n': break;
            case '.': if (isDigit(peekNext()))
                            number();
                        else
                            throw new SyntaxError("Unexpected token '.' at " + start);
                      break;
            default:
                if (isFloat(c))
                    number();
                 else if (isAlpha(c))
                    checkForKeyword();
                 else {
                     String token = source.substring(start, current);
                    throw new SyntaxError("Unexpected token '" + token + "' at " + start);
                }
                break;
        }
    }


}
