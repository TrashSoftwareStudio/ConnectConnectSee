package core;

import java.util.ArrayList;

import static core.Utility.findSmallestList;

public class Matrix {

    public final static int ALPHABET = 0;

    public final static int COLOR_BLUE = 1;

    public final static int COLOR_GREEN = 2;

    public final static int COLOR_RED = 3;

    private Block[][] matrix;

    private boolean[][] boolMatrix;

    private int height, width;

    private int remaining;

    private int occurNumber = 4;

    public Matrix(int height, int width) {
        this.height = height;
        this.width = width;
        matrix = new Block[height][width];
        boolMatrix = new boolean[height + 2][width + 2];
    }

    public void initialize(int cardsSet) {
        int size = width * height;
        remaining = size;

        for (int i = 0; i < size; i++) {
            int y = i / width;
            int x = i % width;
            Block b = new Block(i / occurNumber, cardsSet);
            b.generate(i, occurNumber);
            matrix[y][x] = b;
        }

        for (int i = 0; i < width + 2; i++) {
            boolMatrix[0][i] = true;
            boolMatrix[height + 1][i] = true;
        }
        for (int i = 0; i < height + 2; i++) {
            boolMatrix[i][0] = true;
            boolMatrix[i][width + 1] = true;
        }
    }


    public void wash() {
        int size = width * height;
        Block[] list = new Block[remaining];
        int count = 0;
        for (int i = 0; i < size; i++) {
            int y = i / width;
            int x = i % width;
            if (!boolMatrix[y + 1][x + 1]) {
                list[count] = matrix[y][x];
                count++;
            }
        }

        for (int k = list.length - 1; k >= 0; k--) {
            int r = (int) (Math.random() * (k - 1));
            Block temp = list[k];
            list[k] = list[r];
            list[r] = temp;
        }

        count = 0;
        for (int i = 0; i < size; i++) {
            int y = i / width;
            int x = i % width;
            if (!boolMatrix[y + 1][x + 1]) {
                matrix[y][x] = list[count];
                count++;
            }
        }
    }

    public Block getBlock(int row, int column) {
        return matrix[row][column];
    }

    public boolean isEliminated(int row, int column) {
        return boolMatrix[row + 1][column + 1];
    }

    public ArrayList<int[]> tryConnect(int r1, int c1, int r2, int c2) {
        if (r1 == r2 && c1 == c2) return null;
        ArrayList<int[]> list = tryConnectInternal(r1 + 1, c1 + 1, r2 + 1, c2 + 1);
        if (list != null) {
            boolMatrix[r1 + 1][c1 + 1] = true;
            boolMatrix[r2 + 1][c2 + 1] = true;
            matrix[r1][c1] = null;
            matrix[r2][c2] = null;
            remaining -= 2;
        }
        return list;
    }

    /**
     * This method takes ABSOLUTE positions (the indices in boolMatrix)
     *
     * @param r1 the absolute row of the 1st point
     * @param c1 the absolute column of the 1st point
     * @param r2 the absolute row of the 2nd point
     * @param c2 the absolute column of the 2nd point
     * @return the list of all points int the path, null if no such a path
     */
    private ArrayList<int[]> tryConnectInternal(int r1, int c1, int r2, int c2) {
        if (!matrix[r1 - 1][c1 - 1].isSame(matrix[r2 - 1][c2 - 1])) {
            return null;
        }
        ArrayList<int[]> list = directConnect(r1, c1, r2, c2);
        if (list != null) return list;
        list = connect1(r1, c1, r2, c2);
        if (list != null) return list;
        return connect2(r1, c1, r2, c2);

    }

    private ArrayList<int[]> verticalConnect(int r1, int c1, int r2, int c2) {
        if (c1 == c2) {
            int minR = Math.min(r1, r2);
            int maxR = Math.max(r1, r2);
            ArrayList<int[]> arrays = new ArrayList<>();
            for (int r = minR + 1; r < maxR; r++) {
                if (!boolMatrix[r][c1]) {
                    return null;
                } else {
                    int[] path = new int[]{r, c1, 1};  // 1 means vertical
                    arrays.add(path);
                }
            }
            return arrays;
        } else {
            return null;
        }
    }

    private ArrayList<int[]> horizontalConnect(int r1, int c1, int r2, int c2) {
        if (r1 == r2) {
            int minC = Math.min(c1, c2);
            int maxC = Math.max(c1, c2);
            ArrayList<int[]> arrays = new ArrayList<>();
            for (int c = minC + 1; c < maxC; c++) {
                if (!boolMatrix[r1][c]) {
                    return null;
                } else {
                    int[] path = new int[]{r1, c, 0};  // 0 means horizontal
                    arrays.add(path);
                }
            }
            return arrays;
        }
        return null;
    }

    private ArrayList<int[]> directConnect(int r1, int c1, int r2, int c2) {
        ArrayList<int[]> ver = verticalConnect(r1, c1, r2, c2);
        if (ver != null) return ver;
        return horizontalConnect(r1, c1, r2, c2);
    }

    private ArrayList<int[]> connect1(int r1, int c1, int r2, int c2) {
        int r3 = r1;
        int c3 = c2;
        if (boolMatrix[r3][c3]) {
            ArrayList<int[]> d1 = directConnect(r1, c1, r3, c3);
            ArrayList<int[]> d2 = directConnect(r2, c2, r3, c3);
            if (d1 != null && d2 != null) {
                d1.addAll(d2);
                d1.add(new int[]{r3, c3, 2});  // 2 means turning point
                return d1;
            }
        }
        r3 = r2;
        c3 = c1;
        if (boolMatrix[r3][c3]) {
            ArrayList<int[]> d1 = directConnect(r1, c1, r3, c3);
            ArrayList<int[]> d2 = directConnect(r2, c2, r3, c3);
            if (d1 != null && d2 != null) {
                d1.add(new int[]{r3, c3, 2});
                d1.addAll(d2);
                return d1;
            }
        }
        return null;
    }

    private ArrayList<int[]> connect2(int r1, int c1, int r2, int c2) {

        int[][] pointsToCheck = getAllPossiblePoints(r1, c1, r2, c2);
        ArrayList<ArrayList<int[]>> allSolutions = new ArrayList<>();

        for (int[] cor : pointsToCheck) {
            int y = cor[0];
            int x = cor[1];
            if ((y == r1 || y == r2 || x == c1 || x == c2) && boolMatrix[y][x]) {
                ArrayList<int[]> l1 = directConnect(y, x, r1, c1);
                ArrayList<int[]> l2 = connect1(y, x, r2, c2);
                if (l1 != null && l2 != null) {
                    l1.addAll(l2);
                    l1.add(new int[]{y, x, 2});
                    allSolutions.add(l1);
                }
                l1 = directConnect(y, x, r2, c2);
                l2 = connect1(y, x, r1, c1);
                if (l1 != null && l2 != null) {
                    l1.addAll(l2);
                    l1.add(new int[]{y, x, 2});
                    allSolutions.add(l1);
                }
            }
        }
        return findSmallestList(allSolutions);  // This function will return null if the list is empty
    }

    private int[][] getAllPossiblePoints(int r1, int c1, int r2, int c2) {
        int[][] pointsToCheck = new int[(width + 2) * 2 + (height + 2) * 2 + 4][2];
        int j = 0;
        for (int y = 0; y < height + 2; y++) {
            if (y != r1) {
                pointsToCheck[j++] = new int[]{y, c1};
            }
            if (y != r2) {
                pointsToCheck[j++] = new int[]{y, c2};
            }
        }
        for (int x = width + 1; x >= 0; x--) {  // This is bullshit
            if (x != c1) {
                pointsToCheck[j++] = new int[]{r1, x};
            }
            if (x != c2) {
                pointsToCheck[j++] = new int[]{r2, x};
            }
        }
        return pointsToCheck;
    }

    public boolean isWin() {
        return remaining == 0;
    }
}
