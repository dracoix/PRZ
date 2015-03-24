
package prz.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David 'Q' Rathbun
 */
public class BitOutputStream {

    private final ByteArrayOutputStream backStream;

    /**
     *
     * @param baos
     */
    public BitOutputStream(ByteArrayOutputStream baos) {
        backStream = baos;
    }
    private long bitsWritten = 0;
    private int bitPosition = 8;
    private int currentByte = 0;
    private boolean isFlush;

    /**
     *
     * @return
     */
    public ByteArrayOutputStream getBackStream() {
        return backStream;
    }

    /**
     *
     * @return
     */
    public long getBitsWritten() {
        return bitsWritten;
    }

    /**
     *
     * @return
     */
    public int getBitPosition() {
        return bitPosition;
    }

    /**
     *
     * @return
     */
    public int getCurrentByte() {
        return currentByte;
    }

    /**
     *
     * @param b
     * @param bitLength
     */
    public void writeByte(int b, int bitLength) {

        for (int i = (bitLength - 1); i > -1; i--) {
            write((int) ((b >>> i) & 1));
        }
    }

    /**
     *
     * @param a
     * @param bitLength
     */
    public void writeByte(byte a, int bitLength) {
        writeByte(Byte.toUnsignedInt(a), bitLength);
    }

    /**
     *
     * @param b
     */
    public void writeLong(long b) {
        for (int i = 63; i > -1; i--) {
            write((int) ((b >>> i) & 1));
        }
    }

    /**
     *
     * @param b
     */
    public void write(int b) {
        isFlush = false;
        bitPosition--;
        currentByte |= ((b & 1) << bitPosition);
        if (bitPosition <= 0) {
            backStream.write(currentByte);
            isFlush = true;
            currentByte = 0;
            bitPosition = 8;
        }
        bitsWritten++;
    }

    /**
     *
     */
    public void flush() {
        if (isFlush) {
            return;
        }
        backStream.write(currentByte);
        currentByte = 0;
        isFlush = true;
    }

    /**
     *
     */
    public void finish() {
        this.flush();
        try {
            backStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(BitOutputStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
