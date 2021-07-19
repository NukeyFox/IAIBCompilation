public class SyntaxError extends Exception {
    private String message;
    SyntaxError(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
