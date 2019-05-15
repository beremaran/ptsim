package com.zamska.ptsim.random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PoissonIntegerGenerator extends BaseGenerator implements IntegerGenerator {
    private double mean;

    public PoissonIntegerGenerator(double mean) {
        this.mean = mean;
    }

    @Override
    public int nextInt() {
        double L = Math.exp(-mean);

        int k = 0;
        double p = 1.0;

        do {
            p = p * getRandom().nextDouble();
            k++;
        } while (p > L);

        return k - 1;
    }

    @Override
    public int nextInt(int bound) {
        throw new NotImplementedException();
    }
}
