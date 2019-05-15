package com.zamska.ptsim.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSelector<T> implements Selector<T> {
    private Random random;

    public RandomSelector() {
        random = ThreadLocalRandom.current();
    }

    @Override
    public T select(Collection<T> collection) {
        int randomIndex = random.nextInt(collection.size());
        return (new LinkedList<>(collection)).get(randomIndex);
    }
}
