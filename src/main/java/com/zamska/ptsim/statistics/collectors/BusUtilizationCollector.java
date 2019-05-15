package com.zamska.ptsim.statistics.collectors;

import com.zamska.ptsim.entities.Bus;
import com.zamska.ptsim.entities.Tariff;
import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.statistics.CollectorResult;
import com.zamska.ptsim.statistics.DataCollector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class BusUtilizationCollector implements DataCollector {
    private Map<Tariff, List<Float>> busUtils;

    public BusUtilizationCollector() {
        busUtils = new HashMap<>();
    }

    @Override
    public void collect(Event event) {
        if (!event.getSourceEntity().getType().equals(Bus.TYPE))
            return;

        if (!event.getType().equals(Bus.EVENT_ARRIVED_TO_STOP))
            return;

        Bus bus = (Bus) event.getSourceEntity();
        Tariff tariff = bus.getTariff();

        int capacity = bus.getCapacity();
        int passengers = bus.getPassengerCount();

        if (!busUtils.containsKey(tariff)) {
            busUtils.put(tariff, new ArrayList<>());
        }

        busUtils.get(tariff).add(passengers / (float) capacity);
    }

    @Override
    public boolean hasMultipleResults() {
        return true;
    }

    @Override
    public List<CollectorResult> getResults() {
        if (busUtils.isEmpty()) {
            return new ArrayList<>();
        }

        List<CollectorResult> results = new ArrayList<>();

        busUtils.forEach((id, utils) -> {
            double average = utils.stream().mapToDouble(u -> u).sum() / utils.size();
            results.add(new CollectorResult(id.toString() + " Util", (float) average));
        });

        return results;
    }

    @Override
    public CollectorResult getResult() {
        throw new NotImplementedException();
    }
}
