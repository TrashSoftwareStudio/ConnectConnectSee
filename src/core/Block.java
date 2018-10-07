package core;

public class Block {

    private String top;

    public Block(String top) {
        this.top = top;
    }

    @Override
    public String toString() {
        return top;
    }

    public boolean isSame(Block other) {
        return top.equals(other.top);
    }
}
