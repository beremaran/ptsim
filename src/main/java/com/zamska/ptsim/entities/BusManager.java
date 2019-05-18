package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.simulation.Context;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BusManager extends Entity {
    public static final String TYPE = "BUS_MANAGER";

    private Map<Tariff, Long> lastDeployedAt;

    @Override
    public void initialize(Context context) {
        super.initialize(context);

        lastDeployedAt = new HashMap<>();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void update(long simulationTime) {
        for (Tariff tariff : getTariffs()) {
            if (simulationTime == 0 || simulationTime - lastDeployedAt.getOrDefault(tariff, 0L) >= tariff.getBusDepartureInterval()) {
                deployBus(tariff, simulationTime);
            }
        }
    }

    private void deployBus(Tariff tariff, long simulationTime) {
        Stop firstStop = tariff.getFirstStop();
        Stop lastStop = tariff.getLastStop();
        List<Bus> tariffBuses = getBusesWithTariff(tariff);

        if (tariffBuses.size() / 2 < tariff.getNumberOfBuses()) {
            for (int i = 0; i < 1; i++) {
                Bus bus = new Bus(tariff);
                bus.setCurrentStop(firstStop);
                bus.setDirection(Bus.BusDirection.FORWARD);
                bus.setPosition(firstStop.getRoadPosition());
                getContext().addEntity(bus);
            }

            for (int i = 0; i < 1; i++) {
                Bus bus = new Bus(tariff);
                bus.setCurrentStop(lastStop);
                bus.setDirection(Bus.BusDirection.BACKWARD);
                bus.setPosition(lastStop.getRoadPosition());
                getContext().addEntity(bus);
            }
        }

        lastDeployedAt.put(tariff, simulationTime);
    }

    private List<Tariff> getTariffs() {
        return getContext().getEntities(Tariff.TYPE).stream().map(e -> (Tariff) e).collect(Collectors.toList());
    }

    private List<Bus> getBusesWithTariff(Tariff tariff) {
        return getContext().getEntities(Bus.TYPE).stream().map(e -> (Bus) e).filter(e -> e.getTariff().equals(tariff)).collect(Collectors.toList());
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void draw(Graphics graphics) {

    }
}
