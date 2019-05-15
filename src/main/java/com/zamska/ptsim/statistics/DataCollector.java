package com.zamska.ptsim.statistics;

import com.zamska.ptsim.event.Event;

import java.util.List;

public interface DataCollector {
    void collect(Event event);

    boolean hasMultipleResults();

    List<CollectorResult> getResults();

    CollectorResult getResult();
}
