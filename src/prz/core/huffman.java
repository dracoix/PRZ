/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 *
 * @author David 'Q' Rathbun
 */
public class huffman {

    /**
     *
     * @param bytes
     * @param outStream
     */
    public static void compressBytes(byte[] bytes, ByteArrayOutputStream outStream) {
        Deflater d = new Deflater(9, true);
        d.setStrategy(Deflater.HUFFMAN_ONLY);
        DeflaterOutputStream dfos;

        dfos = new DeflaterOutputStream(outStream, d);
        try {
            dfos.write(bytes);
            dfos.finish();
        } catch (IOException ex) {
            Logger.getLogger(huffman.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
