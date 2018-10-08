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

    public static ArrayList<int[]> findSmallestList(ArrayList<ArrayList<int[]>> lists) {
        if (lists.size() == 0) return null;
        ArrayList<int[]> smallestList = lists.get(0);
        for (int i = 1; i < lists.size(); i++) {
            ArrayList<int[]> currentList = lists.get(i);
            if (currentList.size() < smallestList.size()) {
                smallestList = currentList;
            }
        }
        return smallestList;
    }

    /**
     * Convert a long into a 8-byte array in big-endian.
     *
     * @param l the long.
     * @return 8-byte array.
     */
    private static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) result[i] = (byte) ((l >> ((7 - i) << 3)) & 0xff);
        return result;
    }

    /**
     * Converts a {@code long} into a readable hexadecimal {@code String}.
     *
     * @param value   the number to be converted
     * @return the hexadecimal {@code String} of <code>value</code>
     */
    private static String longToHex(long value) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        byte[] array = longToBytes(value);
        char[] temp = new char[16];
        for (int i = 0; i < 8; i++) {
            int num = array[i] & 0xff;
            temp[i * 2] = hexArray[num >> 4];
            temp[i * 2 + 1] = hexArray[num & 0x0f];
        }
        return new String(temp);
    }

    static String numberToHex6(long number) {
        return longToHex(number).substring(10);
    }
}
