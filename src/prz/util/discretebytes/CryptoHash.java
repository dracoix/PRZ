
package PRZ.util.discretebytes;

/**
 *
 * @author David 'dracoix' Rathbun
 */
public class CryptoHash {

    /**
     *
     */
    public static final long MASK = 0xFFFFFL;

    /**
     *
     * @return
     */
    public static int salt() {
        return (int) (RandomUtility.unique()& MASK);
    }

    /**
     *
     * @return
     */
    public static long blind() {
        return flyHash(salt());
    }

    /**
     *
     * @param hidden
     * @return
     */
    public static long flyHash(long hidden) {
        return UniqueHash.toDigestedLong(hidden);
    }

    /**
     *
     * @param known
     * @param blind
     * @return
     */
    public static long challenge(long known, long blind) {
        return ordial(known, blind);
    }

    /**
     *
     * @param blind
     * @param challenge
     * @return
     */
    public static long antilocket(long blind, long challenge) {
        return ordial(blind, challenge);
    }

    /**
     *
     * @param blind
     * @param challenge
     * @return
     */
    public static long locket(long blind, long challenge) {
        return ordial(challenge, blind);
    }

    /**
     *
     * @param blind
     * @param challenge
     * @return
     */
    public static long respond(long blind, long challenge) {
        return antilocket(blind, challenge) ^ locket(blind, challenge);
    }

    /**
     *
     * @param blind
     * @param data
     * @return
     */
    public static long cloak(long blind, long data) {
        return blind ^ data;
    }

    /**
     *
     * @param blind
     * @param cloaked
     * @return
     */
    public static long decloak(long blind, long cloaked) {
        return blind ^ cloaked;
    }

    /**
     *
     * @param known
     * @param challenge
     * @return
     */
    public static long crack_raw(long known, long challenge) {
        for (long i = 0; i <= MASK; i++) {
            if (ordial(known, flyHash(i)) == challenge) {
                return flyHash(i);
            }
        }
        return 0;
    }

    /**
     *
     * @param known
     * @param challenge
     * @param cp
     * @return
     */
    public static long crack(long known, long challenge, CrackProgress cp) {
        cp.setChallenge(challenge);
        for (long i = 0; i <= MASK; i++) {
            cp.compile(known, i);
            if (cp.getCompiled() == challenge) {
                return cp.getAttempt();
            }

        }
        return 0;
    }

    /**
     *
     * @param first
     * @param second
     * @return
     */
    public static long ordial(long first, long second) {
        return UniqueHash.toDigestedLong(first ^ UniqueHash.toDigestedLong(second));
    }

    /**
     *
     */
    public static class CrackProgress {

        long index;
        long attempt;
        long compiled;
        long challenge;
        double progress;
        double x;
        double y;

        /**
         *
         * @return
         */
        public long getIndex() {
            return index;
        }

        /**
         *
         * @param known
         * @param index
         */
        public void compile(long known, long index) {
            this.index = index;
            this.attempt = flyHash(index);
            this.compiled = ordial(known, this.attempt);
            x = (double) index / (double) MASK;
            y = (4 / 3) - (4 / (3 * (1 + x) * (1 + x)));
            this.progress = y;
        }

        /**
         *
         * @return
         */
        public long getAttempt() {
            return attempt;
        }

        /**
         *
         * @return
         */
        public double getProgress() {
            return progress;
        }

        /**
         *
         * @return
         */
        public long getCompiled() {
            return compiled;
        }

        /**
         *
         */
        public CrackProgress() {
        }

        /**
         *
         * @param d
         */
        public void setProgress(double d) {
            this.progress = d;
        }

        /**
         *
         * @return
         */
        public long getChallenge() {
            return this.challenge;
        }

        private void setChallenge(long challenge) {
            this.challenge = challenge;
        }
    }
}
