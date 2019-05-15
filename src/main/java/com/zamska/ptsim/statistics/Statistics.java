package com.zamska.ptsim.statistics;

import com.zamska.ptsim.entities.Updatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Statistics implements Updatable {
    private List<DataCollector> dataCollectors;
    private List<DataCollectorListener> dataCollectorListeners;
    private List<CollectorResultListener> collectorResultListeners;

    public Statistics() {
        dataCollectors = new ArrayList<>();
        dataCollectorListeners = new ArrayList<>();
        collectorResultListeners = new ArrayList<>();
    }

    public void addDataCollectorListener(DataCollectorListener listener) {
        if (!dataCollectorListeners.contains(listener)) {
            dataCollectorListeners.add(listener);
        }
    }

    public void registerDataCollector(DataCollector collector) {
        if (!dataCollectors.contains(collector)) {
            dataCollectors.add(collector);
            dataCollectorListeners.forEach(dcl -> dcl.onRegisterDataCollector(collector));
        }
    }

    public List<DataCollector> getDataCollectors() {
        return dataCollectors;
    }

    public List<CollectorResultListener> getCollectorResultListeners() {
        return collectorResultListeners;
    }

    public void addCollectorResultListener(CollectorResultListener listener) {
        if (!collectorResultListeners.contains(listener))
            collectorResultListeners.add(listener);
    }

    @Override
    public void update(long simulationTime) {
        List<CollectorResult> collectorResults = new ArrayList<>();

        dataCollectors.forEach(dc -> {
            if (dc.hasMultipleResults()) {
                collectorResults.addAll(dc.getResults());
            } else {
                collectorResults.add(dc.getResult());
            }
        });

        collectorResults.sort(Comparator.comparing(CollectorResult::getValue));
        Collections.reverse(collectorResults);
        collectorResultListeners.forEach(crl -> crl.onCollectorResult(collectorResults));
    }
}
