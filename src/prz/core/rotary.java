
package prz.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import prz.util.BitInputStream;
import prz.util.BitOutputStream;
import prz.util.explode;
import prz.util.implode;

/**
 *
 * @author David 'Q' Rathbun
 */
public class rotary {

    /**
     *
     * @param data
     * @param out
     */
    public static void derive(byte[] data, ByteArrayOutputStream out) {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(data));
        BitOutputStream bos = new BitOutputStream(out);
        int c, last;
        int r;
        r = bis.read();
        r = r ^ 1;
        last = r;
        //System.out.print(r + "");
        bos.write(r);
        c = bis.read();
        while (c != -1) {
            r = 1 ^ (last ^ c);
            last = c;
            //System.out.print(r + "");
            bos.write(r);
            c = bis.read();
        }
        bos.finish();
        //System.out.print("\n");
    }
    
    /**
     *
     * @param data
     * @param out
     */
    public static void integrate(byte[] data, ByteArrayOutputStream out) {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(data));
        BitOutputStream bos = new BitOutputStream(out);
        int c, last;
        int r;
        r = bis.read();
        //r = r ^ 1;
        last = r;
        //System.out.print(r + "");
        bos.write(r);
        c = bis.read();
        while (c != -1) {
            r = (c == 1) ? r : (r ^ 1);
            last = c;
            //System.out.print(r + "");
            bos.write(r);
            c = bis.read();
        }
        bos.finish();
        //System.out.print("\n");
    }

    /**
     *
     * @param data
     * @param out
     */
    public static void classic(byte[] data, ByteArrayOutputStream out) {
        byte[] x = explode.BytesToBits(data);
        BitOutputStream bos = new BitOutputStream(out);
        int tmp, flip, c;
        tmp = x[0];
        bos.write(tmp);
        for (int i = 1; i < x.length; i++) {
            c = x[i];
            flip = 1 ^ (tmp ^ c);
            bos.write(flip);
            tmp = c;
        }
        bos.finish();
    }
    
    /**
     *
     * @param data
     * @param out
     */
    public static  void LeftEdge(byte[] data,  ByteArrayOutputStream out)
    {
        
        byte[] x = explode.BytesToBits(data);
        BitOutputStream bos = new BitOutputStream(out);
        int tmp, flip, c;
        tmp = x[x.length-1];
        bos.write(tmp);
        for (int i = x.length-2; i >=0; i--) {
            c = x[i];
            flip = (tmp ^ c) & 1;
            tmp = c;
            bos.write(flip);
        }
        bos.finish();
    }

    /**
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] rbwt64(byte[] bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        derive(bytes, baos);
        return bwt64(baos.toByteArray());
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] bwt64(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bis = new BitOutputStream(baos);
        ByteArrayOutputStream header = new ByteArrayOutputStream();
        BitOutputStream his = new BitOutputStream(header);
        Long[] table = new Long[16];
        while (bb.remaining() >= 8) {
            long x = bb.getLong();
            long k = x;
            table[0] = x;
            for (int i = 1; i < 16; i++) {
                x = Long.rotateLeft(x, 4);
                table[i] = x;
            }
            Arrays.sort(table, Long::compareUnsigned);
            long o = table[0] & 15;
            for (int i = 1; i < 16; i++) {
                o = o << 4;
                o |= table[i] & 15;
            }
            int s = Arrays.binarySearch(table, k);
            his.writeByte(s, 4);
            bis.writeLong(o);
        }
        while (bb.remaining() > 0) {
            bis.writeByte(bb.get(), 8);
        }
        bis.finish();
        his.finish();
        try {
            baos.writeTo(header);
        } catch (IOException ex) {
            Logger.getLogger(rotary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return header.toByteArray();
    }

    /**
     *
     * @param bytes
     * @param out
     */
    public static void fastRLE(byte[] bytes, ByteArrayOutputStream out) {
        byte[] lastRun = Arrays.copyOf(bytes, bytes.length);
        byte[] bestRun = Arrays.copyOf(bytes, bytes.length);
        int i = 0;
        int iBest = 0;
        boolean lastChance = false;
        ByteArrayOutputStream run;
        long lastLen;
        long rotLen;
        long bestLen;
        byte[] rots;
        while (true) {
            ByteArrayOutputStream rot = new ByteArrayOutputStream();
            lastLen = lastRun.length;
            rotary.derive(lastRun, rot);
            rots = rot.toByteArray();
            lastRun = rots;
            rotLen = lastRun.length;
            
            //lastRun = bwt64(lastRun);
            //System.out.println(bytes.length + "->" + lastLen + "->" + rotLen);
            run = new ByteArrayOutputStream();
            FastRLE_Plus(lastRun, run);
            lastRun = run.toByteArray();
            if (lastRun.length > bytes.length * 2) {
                break;
            }
            if (lastRun.length < bestRun.length) {
                bestLen = lastRun.length;
                bestRun = lastRun;
                lastChance = false;
                iBest = i + 1;
            }else{
                lastRun = rots;
            }
            i++;
            if (i == 255) {
                //System.out.println("Max reached.");
                break;
            }
        }
        out.write(iBest>>> 8);
        out.write(iBest & 255);
        out.write(bestRun, 0, bestRun.length);
    }

    /**
     *
     * @param bytes
     * @param out
     */
    public static void FastRLE_Plus(byte[] bytes, ByteArrayOutputStream out) {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(bytes));
        BitOutputStream bos = new BitOutputStream(out);
        int middleBit;
        int peakBit;
        int lastBitRead = bis.read();
        int testR = 1;
        //System.out.print("\n");
        while (lastBitRead >= 0) {
            middleBit = bis.read();
            testR++;
            if ((middleBit == lastBitRead) && (middleBit != -1)) {
                peakBit = bis.peek();
                if ((middleBit == peakBit) && (peakBit != -1)) {
                    bos.write(middleBit);
                    middleBit = bis.read();
                    //System.out.print(middleBit + "");
                    testR++;
                    bos.write(middleBit);
                    //System.out.print(middleBit + "");
                    lastBitRead = bis.read();
                    testR++;
                    continue;
                }
            }
            //System.out.print("|");
            bos.write(lastBitRead);
            //System.out.print(lastBitRead + "");
            bos.write(lastBitRead ^ 1);
            //System.out.print((lastBitRead ^ 1) + "");
            lastBitRead = middleBit;
        }
        //System.out.print("\n");
        bos.finish();
    }

    /**
     *
     * @param data
     * @param out
     * @return
     */
    public static byte[] inflate(byte[] data, ByteArrayOutputStream out) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        derive(data, tmp);
        byte[] t = tmp.toByteArray();
        for (int i = 0; i < t.length; i++) {
            //out.write(1);
            out.write(t[i] & 1);
        }
        return t;
    }
}
