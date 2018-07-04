package org.akapps.tools.generators.random;

import org.apache.commons.lang3.Validate;

import java.util.Random;

/**
 * A random test that conforms to a given ratio : "num" chances out of "range" that the test succeeds.
 *
 * @author Antoine Kapps
 */
public class RatioRandom {

    private final Random random = new Random();
    private final int ratio;
    private final int range;

    public RatioRandom(int ratio, int range) {
        Validate.isTrue(range > 1, "Range must be greater than one or it is useless - found %s", range);
        Validate.exclusiveBetween(0, range, ratio);
        this.ratio = ratio;
        this.range = range;
    }

    /**
     * "Rolls the dice" and verify whether the test succeeds.
     * The global number of success should obey the configured ratio.
     *
     * @return {@code true} if the test is considered a success
     */
    public boolean test() {
        return random.nextInt(range) < ratio;
    }

    void setSeed(long seed) {
        this.random.setSeed(seed);
    }
}
