package com.zamska.ptsim.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BaseGenerator {
    private Random random;

    public BaseGenerator() {
        random = ThreadLocalRandom.current();
    }

    protected Random getRandom() {
        return random;
    }
}
