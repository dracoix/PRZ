/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz;

import PRZ.util.discretebytes.RandomUtility;
import static PRZ.util.discretebytes.RandomUtility.flash;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import static prz.core.core.compressDown;
import prz.core.dynamic;
import prz.core.rotary;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import prz.core.chain;
import prz.core.core;
import prz.core.lzma;
import prz.util.explode;
import prz.util.implode;

/**
 *
 * @author Expiscor
 */
public class PRZ {

    /**
     *
     * @param a
     */
    public static void dump(byte[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + "");
        }
        System.out.print("\n");
        System.out.flush();
    }

    /**
     *
     */
    public static void rotary_math() {
        int Dn;
        int Rn;
        double D_combo;
        int R_indices;
        double U_rotaries;
        for (int i = 1; i < 256; i += 1) {
            Dn = i;
            D_combo = (double) Math.pow(2, Dn);
            R_indices = (int) (Math.pow(2, Math.ceil(Math.log(Dn) / Math.log(2))) * 2);
            Rn = (int) Math.ceil(Math.log(R_indices) / Math.log(2));
            U_rotaries = (double) D_combo / R_indices;
            System.out.println("Dn: " + Dn + ", Rn:" + Rn + ", Dc: " + D_combo + ", Ri:" + R_indices + ", Ur:" + U_rotaries);
        }
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        long rand = flash();
        byte[] x = new byte[2048];
        for (int i = 0; i < x.length; i++) {
            rand = rand ^ flash(System.nanoTime() ^ rand);
            x[i] = (byte) (rand & 255);
            //System.out.print(x[i] + "");
        }
        if (!Arrays.equals(x, implode.BitsToBytes(explode.BytesToBits(x)))) {
            exit(0);
        }
        //rotary_math();
        //x = dynamic.prep(x, null);
        //x = implode.BitsToBytes(x);
        //ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        //rotary.fastRLE(x, baos);
        //System.out.println(x.length + "->" + baos.toByteArray().length);
        tests_full();
        calgary_full();
        canterbury_full();
//        fastRLE_test(x);
//        fastRRLE_test(x);
//        core_test(x);
//        dynamic_test(x);
//
//        deflate_test(x);
//        huffman_test(x);
//        bzip2_test(x);
//        lzma_test(x);
    }

    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void canterbury_full() throws FileNotFoundException, IOException {
        File f = new File("./bench/canterbury_last/");
        File[] files = f.listFiles();
        FileInputStream fis;
        BufferedInputStream bis;
        ByteArrayOutputStream baos;
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
            fis = new FileInputStream(files[i]);
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
            int r;
            byte[] buffer = new byte[16384];
            while ((r = bis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, r);
            }
            baos.flush();
            byte[] x = baos.toByteArray();
            chain_test(x);
            //fastRLE_test(x);
            //fastRRLE_test(x);
            core_test(x);
            dynamic_test(x);
            deflate_test(x);
            huffman_test(x);
            bzip2_test(x);
            lzma_test(x);
        }
    }
    
    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void tests_full() throws FileNotFoundException, IOException {
        File f = new File("./bench/tests/");
        File[] files = f.listFiles();
        FileInputStream fis;
        BufferedInputStream bis;
        ByteArrayOutputStream baos;
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
            fis = new FileInputStream(files[i]);
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
            int r;
            byte[] buffer = new byte[16384];
            while ((r = bis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, r);
            }
            baos.flush();
            byte[] x = baos.toByteArray();
            chain_test(x);
            //fastRLE_test(x);
            //fastRRLE_test(x);
            core_test(x);
            dynamic_test(x);
            deflate_test(x);
            huffman_test(x);
            bzip2_test(x);
            lzma_test(x);
        }
    }

    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void calgary_full() throws FileNotFoundException, IOException {
        File f = new File("./bench/calgary/");
        File[] files = f.listFiles();
        FileInputStream fis;
        BufferedInputStream bis;
        ByteArrayOutputStream baos;
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
            fis = new FileInputStream(files[i]);
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
            int r;
            byte[] buffer = new byte[16384];
            while ((r = bis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, r);
            }
            baos.flush();
            byte[] x = baos.toByteArray();
            //fastRLE_test(x);
            chain_test(x);
            //fastRRLE_test(x);
            core_test(x);
            dynamic_test(x);
            deflate_test(x);
            huffman_test(x);
            bzip2_test(x);
            lzma_test(x);
        }
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] huffman_test(byte[] bytes) {
        Deflater d = new Deflater(9, true);
        d.setStrategy(Deflater.HUFFMAN_ONLY);
        DeflaterOutputStream dfos;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            dfos = new DeflaterOutputStream(baos, d);
            dfos.write(bytes);
            dfos.finish();
            byte[] y = baos.toByteArray();
            System.out.println("HUFF-> BitLength:"
                    + (bytes.length * 8)
                    + "| BestLength: "
                    + (y.length * 8)
                    + "| Ratio: "
                    + ((double) y.length / (bytes.length)));
            return y;
        } catch (IOException ex) {
            Logger.getLogger(PRZ.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] deflate_test(byte[] bytes) {
        Deflater d = new Deflater(9, true);
        DeflaterOutputStream dfos;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            dfos = new DeflaterOutputStream(baos, d);
            dfos.write(bytes);
            dfos.finish();
            byte[] y = baos.toByteArray();
            System.out.println("LZHF-> BitLength:"
                    + (bytes.length * 8)
                    + "| BestLength: "
                    + (y.length * 8)
                    + "| Ratio: "
                    + ((double) y.length / (bytes.length)));
            return y;
        } catch (IOException ex) {
            Logger.getLogger(PRZ.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] bzip2_test(byte[] bytes) {
        BZip2CompressorOutputStream bzos;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bzos = new BZip2CompressorOutputStream(baos);
            bzos.write(bytes);
            bzos.finish();
            byte[] y = baos.toByteArray();
            System.out.println("BZP2-> BitLength:"
                    + (bytes.length * 8)
                    + "| BestLength: "
                    + (y.length * 8)
                    + "| Ratio: "
                    + ((double) y.length / (bytes.length)));
            return y;
        } catch (IOException ex) {
            Logger.getLogger(PRZ.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] fastRRLE_test(byte[] bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] y = null;
        byte[] x = explode.BytesToBits(bytes);
        byte[] original = Arrays.copyOf(x, x.length);
        x = implode.BitsToBytes(x);
        boolean fallout = false;
        int bestLen = Integer.MAX_VALUE;
        int bestIndex = 0;
        byte[] bestData = null;
        int rIndex = 0;
        long snap = System.currentTimeMillis();
        do {
            if (rIndex != 0) {
                baos = new ByteArrayOutputStream();
                rotary.derive(x, baos);
                x = baos.toByteArray();
            }
            baos = new ByteArrayOutputStream();
            rotary.fastRLE(x, baos);
            y = explode.BytesToBits(baos.toByteArray());
            if (y.length < bestLen) {
                bestIndex = rIndex;
                bestLen = y.length;
                bestData = Arrays.copyOf(y, y.length);
                System.out.println("FRRL-> BitLength:"
                        + original.length
                        + "| BestLength: "
                        + bestLen
                        + "| @RotaryIndex: " + bestIndex
                        + "| Ratio: "
                        + ((double) y.length / (bytes.length * 8)));
                snap = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - snap > 3000) {
                break;
            }
            rIndex++;
            if (rIndex >= (x.length)) {
                fallout = Arrays.equals(original, x);
            }
        } while (!fallout);
        return bestData;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] fastRLE_test(byte[] bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] y;
        rotary.fastRLE(bytes, baos);
        y = explode.BytesToBits(baos.toByteArray());
        System.out.println("FRLE-> BitLength:"
                + (bytes.length * 8)
                + "| BestLength: "
                + (y.length)
                + "| Ratio: "
                + ((double) y.length / (bytes.length * 8)));
        return y;
    }
    
    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] chain_test(byte[] bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] y;
        chain.round_chain(bytes, baos);
        y = explode.BytesToBits(baos.toByteArray());
        System.out.println("CHRZ-> BitLength:"
                + (bytes.length * 8)
                + "| BestLength: "
                + (y.length)
                + "| Ratio: "
                + ((double) y.length / (bytes.length * 8)));
        return y;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] core_test(byte[] bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bits = explode.BytesToBits(bytes);
        core.compressUp(bits, baos);
        byte[] y = baos.toByteArray();
        System.out.println("CPRZ-> BitLength:"
                + (bytes.length * 8)
                + "| BestLength: "
                + (y.length)
                + "| Ratio: "
                + ((double) y.length / (bytes.length * 8)));
        return y;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] lzma_test(byte[] bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        lzma.compressBytes(bytes, baos);
        byte[] y = baos.toByteArray();
        System.out.println("LZMA-> BitLength:"
                + (bytes.length * 8)
                + "| BestLength: "
                + (y.length * 8)
                + "| Ratio: "
                + ((double) y.length / (bytes.length)));
        return y;
    }

    /**
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] dynamic_test(byte[] bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] y = null;
        byte[] x = Arrays.copyOf(bytes, bytes.length);
        byte[] original =explode.BytesToBits(bytes);
        boolean fallout = false;
        int bestLen = Integer.MAX_VALUE;
        int bestIndex = 0;
        byte[] bestData = null;
        int rIndex = 0;
        long snap = System.currentTimeMillis();
        do {
            if (rIndex != 0) {
               
                baos = new ByteArrayOutputStream();
                rotary.integrate(x, baos);
                x = baos.toByteArray();
                //x = explode.BytesToBits(x);
                //
            }
            baos = new ByteArrayOutputStream();
            y =  explode.BytesToBits(x);
            dynamic.compressUp(y, baos);
            y = baos.toByteArray();
            if (y.length <= bestLen) {
                bestIndex = rIndex;
                bestLen = y.length;
                bestData = Arrays.copyOf(y, y.length);
                System.out.println("DPRZ-> BitLength:"
                        + original.length
                        + "| BestLength: "
                        + bestLen
                        + "| @RotaryIndex: " + bestIndex
                        + "| Ratio: "
                        + ((double) y.length / (bytes.length * 8)));
                snap = System.currentTimeMillis();
            }
            //if (rIndex >= Math.ceil(log_2(x.length)*16)) {
            //break;
            //}
            if (System.currentTimeMillis() - snap > 2000) {
                break;
            }
            rIndex++;
            if (rIndex >= (x.length)) {
                fallout = Arrays.equals(original, x);
            }
        } while (!fallout);
        return bestData;
    }

    /**
     *
     * @param x
     * @return
     */
    public static double log_2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
