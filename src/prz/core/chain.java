/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import prz.util.BitOutputStream;
import prz.util.explode;
import prz.util.implode;

/**
 *
 * @author David 'Q' Rathbun
 */
public class chain {

    /**
     *
     * @param bytes
     * @param out
     */
    public static void chain(byte[] bytes, ByteArrayOutputStream out) {
        int[] count8 = new int[256];
        int[] count4 = new int[16];
        int[] count2 = new int[4];

        ByteArrayOutputStream baosStor = new ByteArrayOutputStream();
        ByteArrayOutputStream baos8 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos4 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

        for (byte b : bytes) {
            count8[Byte.toUnsignedInt(b)]++;
        }

        int d8c = 0;
        int d8i = 0;

        for (int i = 0; i < count8.length; i++) {
            if (count8[i] > d8c) {
                d8i = i;
                d8c = count8[i];
            }
        }

        if (count8[Byte.toUnsignedInt(bytes[0])] == d8c) {
            d8i = Byte.toUnsignedInt(bytes[0]);
        }

        for (byte b : bytes) {
            if (Byte.toUnsignedInt(b) == d8i) {
                baosStor.write(1);
            } else {
                baosStor.write(0);
                baos8.write(Byte.toUnsignedInt(b) ^ d8i);
            }
        }

        byte[] t4 = baos8.toByteArray();
        int d4c = 0;
        int d4i = 0;
        for (byte b : t4) {
            count4[Byte.toUnsignedInt(b) >>> 4]++;
            count4[Byte.toUnsignedInt(b) & 15]++;
        }

        for (int i = 0; i < count4.length; i++) {
            if (count4[i] > d4c) {
                d4i = i;
                d4c = count4[i];
            }
        }

        if (count4[Byte.toUnsignedInt(t4[0]) >>> 4] == d4c) {
            d4i = Byte.toUnsignedInt(t4[0]) >>> 4;
        }

        int tmp, th, tl;
        for (byte b : t4) {
            tmp = Byte.toUnsignedInt(b);
            th = (tmp >>> 4) & 15;
            tl = tmp & 15;
            if (th == d4i) {
                baosStor.write(1);
            } else {
                baosStor.write(0);
                baos4.write((th ^ d4i) & 15);
            }
            if (tl == d4i) {
                baosStor.write(1);
            } else {
                baosStor.write(0);
                baos4.write((tl ^ d4i) & 15);
            }
        }

        byte[] t2 = baos4.toByteArray();
        int d2c = 0;
        int d2i = 0;

        for (byte b : t2) {
            count2[Byte.toUnsignedInt(b) >>> 2]++;
            count2[Byte.toUnsignedInt(b) & 3]++;
        }

        for (int i = 0; i < count2.length; i++) {
            if (count2[i] > d2c) {
                d2i = i;
                d2c = count2[i];
            }
        }

        if (count2[Byte.toUnsignedInt(t2[0]) >>> 2] == d2c) {
            d2i = Byte.toUnsignedInt(t2[0]) >>> 2;
        }

        for (byte b : t2) {
            tmp = Byte.toUnsignedInt(b);
            th = (tmp >>> 2) & 3;
            tl = tmp & 3;
            if (th == d2i) {
                baosStor.write(1);
            } else {
                baosStor.write(0);
                baos2.write((d2i ^ th) & 3);
            }
            if (tl == d2i) {
                baosStor.write(1);
            } else {
                baosStor.write(0);
                baos2.write((d2i ^ tl) & 3);
            }
        }

        byte[] t1 = baos2.toByteArray();

        for (byte b : t1) {
            tmp = Byte.toUnsignedInt(b);
            if (tmp == d2i) {
                baosStor.write(1);
            } else {
                baosStor.write(0);
                baosStor.write((tmp) & 1);

                //baosStor.write((tmp) & 1);
            }
        }

        byte[] fin = baosStor.toByteArray();

//        Deflater d = new Deflater(9, true);
//        //d.setStrategy(Deflater.HUFFMAN_ONLY);
//        DeflaterOutputStream dfos;
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(out);

        //bos.writeByte(d2i, 2);
        //bos.writeByte(d4i, 4);
                 //bos.writeByte(d8i, 8);
        for (byte b : fin) {
            bos.write(b);
        }

        bos.finish();

    }

    /**
     *
     * @param bytes
     * @param out
     */
    public static void round_chain(byte[] bytes, ByteArrayOutputStream out) {

        ByteArrayOutputStream baoshuff = new ByteArrayOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream old = new ByteArrayOutputStream();

        //chain(baoshuff.toByteArray(), baos);
        int bestLength = bytes.length + bytes.length;
        baos.write(bytes, 0, bytes.length);

        byte[] tmp = bytes;
        //byte[] tmpr;
        int rounds = 0;
        while (tmp.length <= bestLength) {
            if (rounds > 254) {
                break;
            }
            rounds++;
            bestLength = tmp.length;
            old = baos;
            baos = new ByteArrayOutputStream();
            chain(tmp, baos);
            tmp = baos.toByteArray();
        }

        //tmp = old.toByteArray();
        //Deflater d = new Deflater(9, true);
        //d.setStrategy(Deflater.HUFFMAN_ONLY);
        //lzma.compressBytes(tmp, out);
//        DeflaterOutputStream dfos;
//        baos = new ByteArrayOutputStream();
//
//        dfos = new DeflaterOutputStream(baos, d);
        try {
//            dfos.write(tmp);
//            dfos.finish();

            //lzhuff.compressBytes(old.toByteArray(), baoshuff);
            out.write(rounds);
            old.writeTo(out);

        } catch (IOException ex) {
            Logger.getLogger(chain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
