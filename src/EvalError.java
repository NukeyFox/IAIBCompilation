public class EvalError extends Exception{
    private String message;
    EvalError(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
