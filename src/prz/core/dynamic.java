package prz.core;

import static prz.core.core.compare;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import prz.util.explode;

/**
 *
 * @author David 'Q' Rathbun
 */
public class dynamic {

    private static final int propTrigger = 8;

    private static class counter implements Comparable<counter> {

        public int value;
        public int count;

        @Override
        public int compareTo(counter o) {
            return Integer.compare(o.count, count);
        }

        public int getValue() {
            return value;
        }

        public counter(int value) {
            this.value = value;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    private static class core8 implements Comparable<core8> {

        public int value;
        public int length;

        public core8(int vbyte) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            value = vbyte;
            byte[] v = new byte[1];
            v[0] = (byte) vbyte;
            v = explode.BytesToBits(v);
            dynamic.compressUp(v, baos);
            v = baos.toByteArray();
            length = v.length;
            baos = null;
        }

        @Override
        public int compareTo(core8 o) {
            return Integer.compare(this.length, o.length);
        }
    }

    /**
     *
     * @param implodedBytes
     * @param outStream
     * @return
     */
    public static byte[] prep(byte[] implodedBytes, ByteArrayOutputStream outStream) {
        int uniques = 0;
        int tmp;
        counter ct;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        counter[] table = new counter[256];
        for (int i = 0; i < 256; i++) {
            table[i] = (new counter(i));
        }
        for (int i = 0; i < implodedBytes.length; i++) {
            tmp = Byte.toUnsignedInt(implodedBytes[i]);
            ct = table[tmp];
            if (ct.count == 0) {
                uniques++;
                ct.setCount(1);
            } else {
                ct.setCount(ct.getCount() + 1);
            }
        }
        Arrays.sort(table);
        for (int i = 0; i < implodedBytes.length; i++) {
            tmp = Byte.toUnsignedInt(implodedBytes[i]);
            if (tmp == table[0].value) {
                baos.write(0);
                continue;
            }
            for (int j = 0; j < 256; j++) {
                if (tmp == table[j].value) {
                    baos.write(j);
                    break;
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     *
     * @param explodedBytes
     * @param outStream
     */
    public static void compressUp(byte[] explodedBytes, ByteArrayOutputStream outStream) {
        int n = 0;
        int len;
        int posA = 0;
        int posB = 0;
        int nextBlock = 0;
        int blockLength;
        ByteArrayOutputStream tmpStream;// = new ByteArrayOutputStream();
        //ByteArrayOutputStream rot = new ByteArrayOutputStream();
        //Rotary.Rotarize(explodedBytes.array(), explodedBytes.array().length, 0, rot);
        //explodedBytes = ByteBuffer.wrap(rot.toByteArray());
        thresher t = new thresher();
        while (true) {
            if (t.currentPos >= explodedBytes.length) {
                break;
            }
            tmpStream = new ByteArrayOutputStream();
            compressUp_proxy(explodedBytes, t.currentPos, tmpStream, t);
            try {
                tmpStream.writeTo(outStream);
                tmpStream.close();
            } catch (IOException ex) {
                Logger.getLogger(dynamic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static int compressUp_proxy(byte[] data, int pos, ByteArrayOutputStream outStream, thresher t) {
        t.posA = pos;
        t.currentFailures = 0;
        t.trigger = false;
        //ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
        int len = 1;
        outStream.write(data[pos]);
        while (!t.trigger) {
            if (data.length < pos + len * 2) {
                t.currentPos = pos + len;
                break;
            }
            compressDown(data, len, pos, pos + len, outStream, t);
            len *= 2;
            t.topLen = len;
        }
        return 0;
    }

    private static int compressDown(byte[] data, int len, int posA, int posB, ByteArrayOutputStream out, thresher t) {
        if (len < 1) {
            return -1;
        }
        t.currentPos = posB;
        if (compare(data, len, posA, posB)) {
            out.write(1);
            return 1;
        } else {
            out.write(0);
            if (len > 1) {
                if (compressDown(data, len / 2, posA, posB, out, t) > 0) {
                    compressCancel(data, len / 2, posA + len / 2, posB + len / 2, out, t);
                } else {
                    if (len > propTrigger) {
                        if (len == t.topLen / 2) {
                            t.trigger = true;
                            return -1;
                        }
                    }
                    compressDown(data, len / 2, posA + len / 2, posB + len / 2, out, t);
                }
            }
            return 0;
        }
    }

    private static int compressCancel(byte[] data, int len, int posA, int posB, ByteArrayOutputStream out, thresher t) {
        if (len > 1) {
            if (compressDown(data, len / 2, posA, posB, out, t) > 0) {
                compressCancel(data, len / 2, posA + len / 2, posB + len / 2, out, t);
            } else {
                if (len > propTrigger) {
                    if (len == t.topLen / 2) {
                        t.trigger = true;
                        return -1;
                    }
                }
                compressDown(data, len / 2, posA + len / 2, posB + len / 2, out, t);
            }
        }
        return 0;
    }

//    public static int compare(byte[] data, int len, int posA, int posB) {
//        byte o = 1;
//        for (int i = 0; i < len; i++) {
//            o &= ~(data[posA + i] ^ data[posB + i]);
//            if (o == 0) {
//                return 0;
//            }
//        }
//        return o;
//    }
    private static class thresher {

        public int posA;
        public int currentPos;
        int currentFailures;
        int topLen;
        boolean trigger;
    }
}
