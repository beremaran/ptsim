package com.zamska.ptsim.utils;

import java.util.Collection;

public interface Selector<T> {
    T select(Collection<T> collection);
}
