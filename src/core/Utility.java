package core;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Utility {

    public static boolean containsArray(ArrayList<int[]> list, int[] array) {
        for (int[] a : list) {
            if (Arrays.equals(a, array)) return true;
        }
        return false;
    }

    public static boolean containsPoint(ArrayList<int[]> list, int[] point) {
        for (int[] p : list) {
            if (point[0] == p[0] && point[1] == p[1]) return true;
        }
        return false;
    }

    public static void arrayArrayListToString(ArrayList<int[]> list) {
        for (int[] x : list) {
            System.out.print(Arrays.toString(x));
            System.out.print(", ");
        }
        System.out.println();
    }
}
