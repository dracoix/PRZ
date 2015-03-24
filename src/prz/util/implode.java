
package prz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author David 'Q' Rathbun
 */
public class implode {

    /**
     *
     * @param bits
     * @return
     */
    public static byte[] BitsToBytes(byte[] bits) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int tmp = 0;
        int p = 0;
        tmp =  (bits[0] << 7);
        for (int i = 1; i < bits.length; i++) {
            p = (i % 8);
            if (p == 0) {
                baos.write(tmp);
                tmp =0;
            }
            tmp |=  ((bits[i] & 1) << (7-p));
        }
        if (p != 0) {
            baos.write(tmp);
        }
        return baos.toByteArray();
    }

    /**
     *
     * @param bits
     * @return
     */
    public static byte[] BitsToBytes(ByteArrayInputStream bits) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int tmp = 0;
        int p = 0;
        int bit;
        int i = 0;
        
        while ((bit = bits.read()) != -1) {
            p = (i % 8);
            if (p == 0 && i> 0) {
                baos.write(tmp);
                tmp = 0;
            }
            tmp |= ((bit & 1) << (7 - p));
            i++;
        }
        if (p != 0) {
            baos.write(tmp);
        }
        return baos.toByteArray();
    }
}
