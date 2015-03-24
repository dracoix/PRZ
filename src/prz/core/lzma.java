
package prz.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import prz.util.implode;

/**
 *
 * @author David 'Q' Rathbun
 */
public class lzma {

    /**
     *
     * @param bits
     * @param outStream
     */
    public static void compressBits(byte[] bits, ByteArrayOutputStream outStream) {
          SevenZip.Compression.LZMA.Encoder x;
        x = new SevenZip.Compression.LZMA.Encoder();
        try {
            x.Code(new ByteArrayInputStream(implode.BitsToBytes(bits)), outStream, 0, 0, null);
        } catch (IOException ex) {
            Logger.getLogger(lzma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @param bytes
     * @param outStream
     */
    public static void compressBytes(byte[] bytes, ByteArrayOutputStream outStream) {
         SevenZip.Compression.LZMA.Encoder x;
        x = new SevenZip.Compression.LZMA.Encoder();
        try {
            x.Code(new ByteArrayInputStream(bytes), outStream, 0, 0, null);
        } catch (IOException ex) {
            Logger.getLogger(lzma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
