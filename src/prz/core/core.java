
package prz.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David 'Q' Rathbun
 */
public class core {

    /**
     *
     * @param data
     * @param compLen
     * @param posX
     * @param posY
     * @return
     */
    public static boolean compare(byte[] data, int compLen, int posX, int posY) {
        for (int i = 0; i < compLen; i++) {
            if ((data[posX + i] ) != (data[posY + i] )) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param bits
     * @param out
     * @return
     */
    public static byte[] compressUp(byte[] bits, ByteArrayOutputStream out) {
        int len = 1;
        int posA = 0;
        int posB = 1;
        out.write(bits[posA]);
        while (posB < bits.length) {
            compressDown(bits, len, posA, posB, out);
            len *= 2;
            posB = posA + len;
            if (posB + len > bits.length) {
                posA = posB;
                posB = posA + 1;
                len = 1;
                if (posA >= bits.length) {
                    break;
                }
                out.write(bits[posA]);
            }
        }
        
        return out.toByteArray();
    }

    /**
     *
     * @param data
     * @param compareLength
     * @param posA
     * @param posB
     * @param out
     * @return
     */
    public static int compressDown(byte[] data, int compareLength, int posA, int posB, ByteArrayOutputStream out) {
        if (compareLength < 1) {
            return -1;
        }
        if (compare(data, compareLength, posA, posB)) {
            out.write(1);
            return 1;
        } else {
            out.write(0);
            if (compareLength > 1) {
                if (compressDown(data, compareLength / 2, posA, posB, out) > 0) {
                    compressCancel(data, compareLength / 2, posA + compareLength / 2, posB + compareLength / 2, out);
                } else {
                    compressDown(data, compareLength / 2, posA + compareLength / 2, posB + compareLength / 2, out);
                }
            }
            return 0;
        }
    }

    /**
     *
     * @param data
     * @param compareLength
     * @param posA
     * @param posB
     * @param out
     * @return
     */
    public static int compressCancel(byte[] data, int compareLength, int posA, int posB, ByteArrayOutputStream out) {
        if (compareLength > 1) {
            if (compressDown(data, compareLength / 2, posA, posB, out) > 0) {
                compressCancel(data, compareLength / 2, posA + compareLength / 2, posB + compareLength / 2, out);
            } else {
                compressDown(data, compareLength / 2, posA + compareLength / 2, posB + compareLength / 2, out);
            }
        }
        return 0;
    }
}
