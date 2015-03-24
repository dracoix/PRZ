
package prz.core;

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
public class lzhuff {

    /**
     *
     * @param bytes
     * @param outStream
     */
    public static void compressBytes(byte[] bytes, ByteArrayOutputStream outStream) {
        Deflater d = new Deflater(9, true);
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
