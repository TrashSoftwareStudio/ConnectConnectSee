package core;

public class Block {

    private String top;
    private int identity;
    private int type;

    Block(int identity, int type) {
        this.identity = identity;
        this.type = type;
    }

    void generate(int i, int occurNumber) {
        if (type == Matrix.ALPHABET) generateText(i, occurNumber);
        else if (type == Matrix.COLOR_BLUE || type == Matrix.COLOR_GREEN ||
                type == Matrix.COLOR_RED) generateColor(i, occurNumber);
    }

    private void generateText(int i, int occurNumber) {
        if (i < 26 * occurNumber) {
            top = String.valueOf((char) (i / occurNumber + 65));
        } else if (i < 52 * occurNumber) {
            top = String.valueOf((char) (i / occurNumber - 26 + 97));
        } else {
            top = String.valueOf((char) (i / occurNumber - 52 + 48));
        }
    }

    private void generateColor(int i, int occurNumber) {
        int n = i / occurNumber * 16;
        int multiplier;
        switch (type) {
            case Matrix.COLOR_BLUE:
                multiplier = 1;
                break;
            case Matrix.COLOR_GREEN:
                multiplier = 256;
                break;
            case Matrix.COLOR_RED:
                multiplier = 65536;
                break;
            default:
                throw new RuntimeException("Impossible");
        }
        int m = n * multiplier;
        top = Utility.numberToHex6(m);
    }

    @Override
    public String toString() {
        return top;
    }

    boolean isSame(Block other) {
        return identity == other.identity;
    }
}
