/*
 * The MIT License
 *
 * Copyright 2014 David Rathbun.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package PRZ.util.discretebytes;

/**
 *
 * @author David 'dracoix' Rathbun
 */
public final class RandomUtility {

    /**
     *
     */
    public static final long antiZeroUnique = UniqueHash.toDigestedLong(0);

    /**
     *
     */
    public static final long antiZeroRandom = UniqueHash.toDigestedLong(RandomHash.M32);
    
    /**
     *
     */
    public static final Slow Slow = new Slow();

    /**
     *
     */
    public static final Fast Fast = new Fast();

    /**
     *
     */
    public static final Basic Basic = new Basic();
    
    /**
     *
     * @return
     */
    public static long unique() {
        return UniqueHash.toDigestedLong(System.nanoTime());
    }

    /**
     *
     * @param seed
     * @return
     */
    public static long unique(long seed) {
        //Unique Zero seed should ALWAYS return Zero to indicate null
        return UniqueHash.toDigestedLong(seed) ^ antiZeroUnique;
    }

    /**
     *
     * @return
     */
    public static long flash() {
        return RandomHash.flash();
    }

    /**
     *
     * @param seed
     * @return
     */
    public static long flash(long seed) {
        //Random Zero seed should NEVER return Zero
        return RandomHash.seed(seed^antiZeroRandom);
    }

    /**
     *
     * @return
     */
    public static long snap() {
        return RandomHash.snap();
    }

    /**
     *
     * @param seed
     * @return
     */
    public static long snap(long seed) {
        //Random Zero seed should NEVER return Zero
        return RandomHash.encode(seed^antiZeroRandom);
    }

    /**
     *
     */
    public static class Basic {

        /**
         *
         * @return
         */
        public  long asLong() {
            return flash();
        }

        /**
         *
         * @param seed
         * @return
         */
        public  long asLong(long seed) {
            return flash(seed);
        }

        /**
         *
         * @return
         */
        public  byte asByte() {
            return DiscreteHash.toByte(asLong());
        }

        /**
         *
         * @param seed
         * @return
         */
        public  byte asByte(long seed) {
            return DiscreteHash.toByte(asLong(seed));
        }

        /**
         *
         * @return
         */
        public  short asShort() {
            return DiscreteHash.toShort(asLong());
        }

        /**
         *
         * @param seed
         * @return
         */
        public  short asShort(long seed) {
            return DiscreteHash.toShort(asLong(seed));
        }

        /**
         *
         * @return
         */
        public  int asInteger() {
            return DiscreteHash.toInteger(asLong());
        }

        /**
         *
         * @param seed
         * @return
         */
        public  int asInteger(long seed) {
            return DiscreteHash.toInteger(asLong(seed));
        }

        /**
         *
         * @return
         */
        public  float asFloat() {
            return DiscreteHash.toFloat(asLong());
        }

        /**
         *
         * @param seed
         * @return
         */
        public  float asFloat(long seed) {
            return DiscreteHash.toFloat(asLong(seed));
        }

        /**
         *
         * @return
         */
        public  double asDouble() {
            return DiscreteHash.toDouble(asLong());
        }

        /**
         *
         * @param seed
         * @return
         */
        public  double asDouble(long seed) {
            return DiscreteHash.toDouble(asLong(seed));
        }

        /**
         *
         * @return
         */
        public  boolean asBoolean() {
            return DiscreteHash.toBoolean(asLong());
        }

        /**
         *
         * @param seed
         * @return
         */
        public  boolean asBoolean(long seed) {
            return DiscreteHash.toBoolean(asLong(seed));
        }
    }

    /**
     *
     */
    public static class Slow extends Basic {

        /**
         *
         * @return
         */
        @Override
        public  long asLong() {
            return unique();
        }

        /**
         *
         * @param seed
         * @return
         */
        @Override
        public  long asLong(long seed) {
            return unique(seed);
        }
    }

    /**
     *
     */
    public static class Fast extends Basic {

        /**
         *
         * @return
         */
        @Override
        public  long asLong() {
            return snap();
        }

        /**
         *
         * @param seed
         * @return
         */
        @Override
        public  long asLong(long seed) {
            return snap(seed);
        }
    }

    /**
     *
     */
    public static abstract class BasicPump {

        private long seed = 0;
        private long drip = 0;

        /**
         *
         * @param seed
         */
        public BasicPump(long seed) {
            this.seed = seed;
        }

        /**
         *
         * @return
         */
        public long self() {
            return seed;
        }

        /**
         *
         */
        public void pump() {
            this.drip = Fast.asLong(this.seed ^ this.drip);
        }

        /**
         *
         * @return
         */
        public long drip() {
            return drip;
        }

        /**
         *
         * @return
         */
        public long spill() {
            pump();
            return drip();
        }

        /**
         *
         * @param a
         */
        public void fill(long a) {
            this.drip = Fast.asLong(this.seed ^ a);
        }
    }

    /**
     *
     */
    public static class SmallPump extends BasicPump {

        /**
         *
         * @param seed
         */
        public SmallPump(long seed) {
            super(seed);
            fill(Slow.asLong(seed));
        }
    }

    /**
     *
     */
    public static class LargePump extends BasicPump {

        private byte[] tank = new byte[32];

        /**
         *
         * @param seed
         */
        public LargePump(long seed) {
            super(seed);
            tank = UniqueHash.toDigested32Bytes(seed);
            fill(DiscreteHash.toLong(tank));
        }

        /**
         *
         */
        @Override
        public void pump() {
            tank = UniqueHash.toDigested32Bytes(tank);
            fill(DiscreteHash.toLong(tank));
        }

        /**
         *
         * @return
         */
        @Override
        public long spill() {
            pump();
            return drip();
        }
    }

    /**
     *
     */
    public static class Daemon extends SmallPump {

        /**
         *
         * @param seed
         */
        public Daemon(long seed) {
            super(seed);
        }

        /**
         *
         * @return
         */
        public short nextByte() {
            pump();
            return DiscreteHash.toByte(this.drip());
        }

        /**
         *
         * @return
         */
        public short nextShort() {
            pump();
            return DiscreteHash.toShort(this.drip());
        }

        /**
         *
         * @return
         */
        public int nextInteger() {
            pump();
            return DiscreteHash.toInteger(this.drip());
        }

        /**
         *
         * @return
         */
        public long nextLong() {
            pump();
            return this.drip();
        }

        /**
         *
         * @return
         */
        public double nextFloat() {
            pump();
            return DiscreteHash.toFloat(this.drip());
        }

        /**
         *
         * @return
         */
        public double nextDouble() {
            pump();
            return DiscreteHash.toDouble(this.drip());
        }

        /**
         *
         * @return
         */
        public boolean nextBoolean() {
            pump();
            return DiscreteHash.toBoolean(this.drip());
        }
    }
}
