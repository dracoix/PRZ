package PRZ.util.discretebytes;

/**
 *
 * @author David 'Q' Rathbun
 */
public class BinaryUtility {

    /**
     *
     * @param b
     * @param pos
     * @return
     */
    public static byte setBit(byte b, int pos) {
        return (byte) (b | (1 << pos));
    }

    /**
     *
     * @param b
     * @param pos
     * @return
     */
    public static byte unsetBit(byte b, int pos) {
        return (byte) (b & ~(1 << pos));
    }

    /**
     *
     * @param b
     * @param pos
     * @return
     */
    public static boolean getBit(byte b, int pos) {
        return (b & (1 << pos)) > 0;
    }

    /**
     *
     */
    public static class DataCoder {

        /**
         *
         */
        public static final double EPSILON_PRECISION = 65536.0;

        /**
         *
         */
        public static final double EPSILON_COMPARE = 1.0 / EPSILON_PRECISION;

        /**
         *
         * @param a
         * @return
         */
        public static long toDecimal(double a) {
            return (long) (a * EPSILON_PRECISION);
        }

        /**
         *
         * @param a
         * @return
         */
        public static double fromDecimal(long a) {
            return ((double) a / EPSILON_PRECISION);
        }

        /**
         *
         * @param a
         * @return
         */
        public static int toDecimal32(double a) {
            return (int) (a * EPSILON_PRECISION);
        }

        /**
         *
         * @param a
         * @return
         */
        public static short toDecimal16(double a) {
            return (short) (a * EPSILON_PRECISION);
        }

        /**
         *
         * @param a
         * @return
         */
        public static byte toDecimal8(double a) {
            return (byte) (a * EPSILON_PRECISION);
        }

        /**
         *
         * @param a
         * @return
         */
        public static double correct(double a) {
            if (!Double.isFinite(a)) {
                return 0;
            }
            return a;
        }

        /**
         *
         * @param a
         * @param b
         * @return
         */
        public static short packBytes(byte a, byte b) {
            return (short) ((Byte.toUnsignedInt(a) << 8) | (Byte.toUnsignedInt(b)));
        }

        /**
         *
         * @param a
         * @param b
         * @return
         */
        public static int packShorts(short a, short b) {
            return ((Short.toUnsignedInt(a) << 16) | (Short.toUnsignedInt(b)));
        }

        /**
         *
         * @param a
         * @param b
         * @return
         */
        public static long packIntegers(int a, int b) {
            return ((Integer.toUnsignedLong(a) << 32) | (Integer.toUnsignedLong(b)));
        }
        
        /**
         *
         * @param a
         * @param b
         * @return
         */
        public static long packDoubles(double a, double b)
        {
            return packIntegers((int) toDecimal(a), (int) toDecimal(b));
        }

        /**
         *
         * @param a
         * @return
         */
        public static byte loByte(short a) {
            return (byte) (a & 255);
        }

        /**
         *
         * @param a
         * @return
         */
        public static byte hiByte(short a) {
            return (byte) (a >>> 8);
        }

        /**
         *
         * @param a
         * @return
         */
        public static short hiShort(int a) {
            return (short) (a >>> 16);
        }

        /**
         *
         * @param a
         * @return
         */
        public static short loShort(int a) {
            return (short) (a & 0xFFFF);
        }

        /**
         *
         * @param a
         * @return
         */
        public static int hiInteger(long a) {
            return (int) (a >>> 32);
        }

        /**
         *
         * @param a
         * @return
         */
        public static int loInteger(long a) {
            return (int) (a & 0xFFFF_FFFFL);
        }

        /**
         *
         * @param a
         * @return
         */
        public static long mask(byte a) {
            return ((long) a) & 0xFF;
        }

        /**
         *
         * @param a
         * @return
         */
        public static long mask(short a) {
            return ((short) a) & 0xFFFF;
        }

        /**
         *
         * @param a
         * @return
         */
        public static long mask(int a) {
            return ((int) a) & 0xFFFF_FFFF;
        }

        /**
         *
         * @param a
         * @param b
         * @return
         */
        public static boolean better(double a, double b) {
            return (Math.abs(b - a) > (EPSILON_COMPARE));
        }

    }

}
