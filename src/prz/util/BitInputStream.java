
package prz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David 'Q' Rathbun
 */
public class BitInputStream {

    private final ByteArrayInputStream backStream;

    /**
     *
     * @param bais
     */
    public BitInputStream(ByteArrayInputStream bais) {
        backStream = bais;
        nextByte = backStream.read();
        consume();
    }
    private int bitPos = -1;
    private int nextByte = 0;
    private int currentByte = 0;
    private int builtByte = 0;
    private long bitsRead = 0;

    private void consume() {
        bitPos--;
        if (bitPos < 0) {
            currentByte = nextByte;
            nextByte = backStream.read();
            builtByte = 0;
            if (currentByte < 0) {
                currentBit = -1;
                nextBit = -1;
                return;
            }
            bitPos = 7;
        }
        currentBit = (currentByte >>> bitPos) & 1;
        if (bitPos != 0) {
            nextBit = (currentByte >>> bitPos - 1) & 1;
        } else {
            if (nextByte != -1) {
                nextBit = (nextByte >>> 7) & 1;
            } else {
                nextBit = -1;
            }
        }
    }
    int currentBit;
    int nextBit;

    /**
     *
     * @return
     */
    public int readBit() {
        return read();
    }

    /**
     *
     * @return
     */
    public int peek() {
        return nextBit;
    }

    /**
     *
     * @return
     */
    public int read() {
        int p = currentBit;
        consume();
        return p;
    }
}
