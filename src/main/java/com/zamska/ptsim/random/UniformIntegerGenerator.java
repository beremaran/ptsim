package com.zamska.ptsim.random;

public class UniformIntegerGenerator extends BaseGenerator implements IntegerGenerator {
    @Override
    public int nextInt() {
        return getRandom().nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return getRandom().nextInt(bound);
    }
}
