package com.zamska.ptsim.statistics.collectors;

import com.zamska.ptsim.entities.Bus;
import com.zamska.ptsim.entities.Passenger;
import com.zamska.ptsim.entities.Tariff;
import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.statistics.CollectorResult;
import com.zamska.ptsim.statistics.DataCollector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class TariffWaitTimeCollector implements DataCollector {
    private Map<UUID, Long> passengersCreatedAt;
    private Map<Tariff, List<Long>> passengersWaitedFor;

    public TariffWaitTimeCollector() {
        passengersCreatedAt = new HashMap<>();
        passengersWaitedFor = new HashMap<>();
    }

    @Override
    public void collect(Event event) {
        if (!event.getSourceEntity().getType().equals(Passenger.TYPE)) {
            return;
        }

        if (event.getType().equals(Passenger.EVENT_CREATE))
            passengersCreatedAt.put(event.getSourceEntity().getId(), event.getTimestamp());
        if (event.getType().equals(Passenger.EVENT_ON_BUS)) {
            if (!event.getTargetEntity().getType().equals(Bus.TYPE))
                return;

            Bus bus = (Bus) event.getTargetEntity();
            Tariff tariff = bus.getTariff();

            if (!passengersWaitedFor.containsKey(tariff))
                passengersWaitedFor.put(tariff, new ArrayList<>());

            passengersWaitedFor.get(tariff).add(event.getTimestamp() - passengersCreatedAt.get(event.getSourceEntity().getId()));
        }
    }

    @Override
    public boolean hasMultipleResults() {
        return true;
    }

    @Override
    public List<CollectorResult> getResults() {
        List<CollectorResult> results = new ArrayList<>();

        passengersWaitedFor.forEach((tariff, longs) -> {
            if (!longs.isEmpty())
                results.add(
                        new CollectorResult(
                                tariff.toString() + " Waiting Time",
                                (float) longs.stream().mapToLong(l -> l).average().getAsDouble()
                        )
                );
        });

        return results;
    }

    @Override
    public CollectorResult getResult() {
        throw new NotImplementedException();
    }
}
