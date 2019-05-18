package com.zamska.ptsim.statistics.collectors;

import com.zamska.ptsim.entities.Passenger;
import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.statistics.CollectorResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class PassengerWaitTimeCollector implements DataCollector {
    private Map<UUID, Long> passengersCreatedAt;
    private List<Long> passengersWaitedFor;

    public PassengerWaitTimeCollector() {
        passengersCreatedAt = new HashMap<>();
        passengersWaitedFor = new ArrayList<>();
    }

    @Override
    public void collect(Event event) {
        if (!event.getSourceEntity().getType().equals(Passenger.TYPE)) {
            return;
        }

        if (event.getType().equals(Passenger.EVENT_CREATE))
            passengersCreatedAt.put(event.getSourceEntity().getId(), event.getTimestamp());
        if (event.getType().equals(Passenger.EVENT_ON_BUS))
            passengersWaitedFor.add(event.getTimestamp() - passengersCreatedAt.get(event.getSourceEntity().getId()));
    }

    @Override
    public boolean hasMultipleResults() {
        return false;
    }

    @Override
    public List<CollectorResult> getResults() {
        throw new NotImplementedException();
    }

    @Override
    public CollectorResult getResult() {
        if (passengersWaitedFor.size() == 0) {
            return new CollectorResult("Avg Wait", 0);
        }

        return new CollectorResult(
                "Avg Wait",
                passengersWaitedFor.stream().mapToLong(pwf -> (long) pwf).sum() / (float) passengersWaitedFor.size()
        );
    }
}
