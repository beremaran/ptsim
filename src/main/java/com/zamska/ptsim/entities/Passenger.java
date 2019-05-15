package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.event.EventDispatcher;
import com.zamska.ptsim.simulation.Context;

import java.awt.*;
import java.util.List;

public class Passenger extends Entity {
    public static final String TYPE = "PASSENGER";

    public static final String EVENT_ON_BUS = "EVENT_ON_BUS";
    public static final String EVENT_OFF_BUS = "EVENT_OFF_BUS";
    public static final String EVENT_ARRIVED = "EVENT_ARRIVED";

    private Stop departureStop;
    private Stop targetStop;
    private Bus currentBus;
    private State state;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void initialize(Context context) {
        super.initialize(context);

        state = State.WAITING;
        if (departureStop != null)
            EventDispatcher.getInstance()
                    .subscribe(departureStop, this);
        if (targetStop != null)
            EventDispatcher.getInstance()
                    .subscribe(targetStop, this);
    }

    @Override
    public void update(long simulationTime) {
        switch (state) {
            case WAITING:
                setPosition(departureStop.getPosition());
                break;
            case ON_BUS:
                if (currentBus != null)
                    setPosition(currentBus.getPosition());
                break;
        }
    }

    @Override
    public void draw(Graphics graphics) {

    }

    @Override
    public void onEvent(Event event) {
        if (Bus.EVENT_ARRIVED_TO_STOP.equals(event.getType())) {
            Bus bus = (Bus) event.getSourceEntity();

            if (event.getTargetEntity() == null) {
                return;
            }

            if (state == State.WAITING) {
                List<Stop> nextStops = bus.getNextStops();
                boolean isDepartureStop = event.getTargetEntity().getId().equals(departureStop.getId());

                if (isDepartureStop && nextStops.contains(targetStop)) {
                    if (bus.takePassenger(this)) {
                        currentBus = bus;
                        state = State.ON_BUS;

                        dispatchEvent(getEventBuilder(EVENT_ON_BUS).withTargetEntity(bus).build());
                        dispatchEvent(getEventBuilder(EVENT_ON_BUS).withTargetEntity(departureStop).build());
                    }
                }
            } else if (state == State.ON_BUS) {
                if (event.getTargetEntity().getId().equals(targetStop.getId())) {
                    currentBus.leavePassenger(this);
                    currentBus = null;
                    state = State.ARRIVED;
                    dispatchEvent(getEventBuilder(EVENT_OFF_BUS).withTargetEntity(bus).build());
                    dispatchEvent(getEventBuilder(EVENT_ARRIVED).withTargetEntity(bus).build());
                }
            }
        }
    }

    public Stop getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(Stop departureStop) {
        this.departureStop = departureStop;
    }

    public Stop getTargetStop() {
        return targetStop;
    }

    public void setTargetStop(Stop targetStop) {
        this.targetStop = targetStop;
    }

    public enum State {
        WAITING,
        ON_BUS,
        ARRIVED,
    }
}
