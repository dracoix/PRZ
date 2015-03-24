
package prz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
/**
 *
 * @author David 'Q' Rathbun
 */
public class explode {
    
    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] BytesToBits(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length == 0) {
            return null;
        }
        return BytesToBits(new ByteArrayInputStream(bytes));
    }
    
    /**
     *
     * @param bytes
     * @return
     */
    public static byte[] BytesToBits(ByteArrayInputStream bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int tmp;
        while ((tmp = bytes.read()) != -1) {
            baos.write((tmp >> 7) & 1);
            baos.write((tmp >> 6) & 1);
            baos.write((tmp >> 5) & 1);
            baos.write((tmp >> 4) & 1);
            baos.write((tmp >> 3) & 1);
            baos.write((tmp >> 2) & 1);
            baos.write((tmp >> 1) & 1);
            baos.write(tmp & 1);
        }
        return baos.toByteArray();
    }
    
    /**
     *
     * @param sparse
     * @param fill
     * @return
     */
    public static byte[] SparseToBits(String sparse, int fill) {
        if (sparse == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int tmp;
        for (char c : sparse.toCharArray()) {
            if (c == '0') {
                baos.write(0);
                continue;
            }
            if (c == '1') {
                baos.write(1);
                continue;
            }
            baos.write(fill & 1);
        }
        return baos.toByteArray();
    }
}
