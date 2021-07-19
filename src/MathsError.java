public class MathsError extends Exception{
    private String message;
    MathsError(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
