package com.zamska.ptsim.statistics.collectors;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.statistics.CollectorResult;

import java.util.List;

public interface DataCollector {
    void collect(Event event);

    boolean hasMultipleResults();

    List<CollectorResult> getResults();

    CollectorResult getResult();
}
