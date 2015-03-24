/*
 * The MIT License
 *
 * Copyright 2014 David Rathbun.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package PRZ.util.discretebytes;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David 'dracoix' Rathbun
 */
public class UniqueHash {

    private static final String strSha = "SHA-256";
    private static MessageDigest shaFast;

    private static byte[] digest(byte[] b, MessageDigest md) {
        try {
            if (md == null) {
                md =  MessageDigest.getInstance(strSha);
            }
           //md.reset();
            byte[] k = md.digest(b);
            md = null;
            return k;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UniqueHash.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static byte long7(long x) {
        return (byte) (x >> 56);
    }

    private static byte long6(long x) {
        return (byte) (x >> 48);
    }

    private static byte long5(long x) {
        return (byte) (x >> 40);
    }

    private static byte long4(long x) {
        return (byte) (x >> 32);
    }

    private static byte long3(long x) {
        return (byte) (x >> 24);
    }

    private static byte long2(long x) {
        return (byte) (x >> 16);
    }

    private static byte long1(long x) {
        return (byte) (x >> 8);
    }

    private static byte long0(long x) {
        return (byte) (x);
    }

    private static int m(byte b)
    {
        return ((int) b) & 255;
    }
    
    private static long digestFast(long a, MessageDigest md) {
        try {
            if (md == null) {
                md = MessageDigest.getInstance(strSha);
            }
            
            byte[] data = new byte[8];
            data[7] = long0(a);
            data[6] = long1(a);
            data[5] = long2(a);
            data[4] = long3(a);
            data[3] = long4(a);
            data[2] = long5(a);
            data[1] = long6(a);
            data[0] = long7(a);
            byte[] d = md.digest(data);
            data[0] = (byte) (m(d[0]) ^ m(d[8]) ^ m(d[16]) ^ m(d[24]));
            data[1] = (byte) (m(d[1]) ^ m(d[9]) ^ m(d[17]) ^ m(d[25]));
            data[2] = (byte) (m(d[2]) ^ m(d[10]) ^ m(d[18]) ^ m(d[26]));
            data[3] = (byte) (m(d[3]) ^ m(d[11]) ^ m(d[19]) ^ m(d[27]));
            data[4] = (byte) (m(d[4]) ^ m(d[12]) ^ m(d[20]) ^ m(d[28]));
            data[5] = (byte) (m(d[5]) ^ m(d[13]) ^ m(d[21]) ^ m(d[29]));
            data[6] = (byte) (m(d[6]) ^ m(d[14]) ^ m(d[22]) ^ m(d[30]));
            data[7] = (byte) (m(d[7]) ^ m(d[15]) ^ m(d[23]) ^ m(d[31]));
            long o = ((long) data[0] & 255);
            o = o << 8;
            o = o | ((long) data[1] & 255);
            o = o << 8;
            o = o | ((long) data[2] & 255);
            o = o << 8;
            o = o | ((long) data[3] & 255);
            o = o << 8;
            o = o | ((long) data[4] & 255);
            o = o << 8;
            o = o | ((long) data[5] & 255);
            o = o << 8;
            o = o | ((long) data[6] & 255);
            o = o << 8;
            o = o | ((long) data[7] & 255);
            md = null;
            //d = null;
            //data = null;
            return o;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UniqueHash.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    private static byte[] digest(long a, MessageDigest md) {
        return digest(ByteBuffer.allocate(8).putLong(a).array(), md);
    }

    private static byte[] digest(String s, MessageDigest md) {
        return digest(s.getBytes(), md);
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[] toDigested8Bytes(long a) {
        return ByteBuffer.allocate(8).putLong(toDigestedLong(a)).array();
    }

    /**
     *
     * @param s
     * @return
     */
    public static byte[] toDigested8Bytes(String s) {
        return ByteBuffer.allocate(8).putLong(toDigestedLong(s)).array();
    }

    /**
     *
     * @param b
     * @return
     */
    public static byte[] toDigested8Bytes(byte[] b) {
        return ByteBuffer.allocate(8).putLong(toDigestedLong(b)).array();
    }
    
    /**
     *
     * @param a
     * @return
     */
    public static byte[] toDigested32Bytes(long a) {
        return digest(a, null);
    }

    /**
     *
     * @param a
     * @param md
     * @return
     */
    public static byte[] toDigested32Bytes(long a,  MessageDigest md) {
        return digest(a, md);
    }

    /**
     *
     * @param s
     * @return
     */
    public static byte[] toDigested32Bytes(String s) {
        return digest(s, null);
    }

    /**
     *
     * @param b
     * @return
     */
    public static byte[] toDigested32Bytes(byte[] b) {
        return digest(b, null);
    }

    /**
     *
     * @param a
     * @return
     */
    public static String toDigestedString(long a) {
        return StringUtility.toHexadecimal(digest(a, null));
    }

    /**
     *
     * @param s
     * @return
     */
    public static String toDigestedString(String s) {
        return StringUtility.toHexadecimal(digest(s, null));
    }

    /**
     *
     * @param b
     * @return
     */
    public static String toDigestedString(byte[] b) {
        return StringUtility.toHexadecimal(digest(b, null));
    }
    
    /**
     *
     * @param a
     * @param md
     * @return
     */
    public static long toDigestedLong(long a, MessageDigest md) {
        return digestFast(a, md);
    }

    /**
     *
     * @param a
     * @return
     */
    public static long toDigestedLong(long a) {
        return digestFast(a, null);
    }

    /**
     *
     * @param s
     * @return
     */
    public static long toDigestedLong(String s) {
        return DiscreteHash.toLong(digest(s, null));
    }

    /**
     *
     * @param b
     * @return
     */
    public static long toDigestedLong(byte[] b) {
        return DiscreteHash.toLong(digest(b, null));
    }

    /**
     *
     * @param s
     * @return
     */
    public static String toDigestedLongString(String s) {
        return StringUtility.toHexadecimal(toDigestedLong(s));
    }

    /**
     *
     * @param b
     * @return
     */
    public static String toDigestedLongString(byte[] b) {
        return StringUtility.toHexadecimal(toDigestedLong(b));
    }
}
