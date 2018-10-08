package core;

public class Block {

    private String top;

    Block(String top) {
        this.top = top;
    }

    @Override
    public String toString() {
        return top;
    }

    boolean isSame(Block other) {
        return top.equals(other.top);
    }
}
