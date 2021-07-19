public class Action {
    public enum Move{
        REDUCE, SHIFT, ACCEPT, ERROR;
    }
    Move move;
    State state;

    Action(Move move, State state){
        this.move = move;
        this.state = state;
    }

    @Override
    public String toString() {
        return move.toString();
    }
}
