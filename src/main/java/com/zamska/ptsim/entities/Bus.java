package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.models.Vector2;
import com.zamska.ptsim.simulation.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bus extends Entity {
    public static final String TYPE = "BUS";

    public static final String EVENT_ARRIVED_TO_STOP = "EVENT_ARRIVED_TO_STOP";

    private static Logger logger = LogManager.getLogger(Bus.class);

    private State state;
    private long waitTime;
    private Tariff tariff;
    private Stop currentStop;
    private BusDirection direction;

    private int capacity;

    private Vector2<Float> speed;
    private Vector2<Float> actualPosition;

    private List<Passenger> passengerList;

    public Bus(Tariff tariff) {
        super();

        this.tariff = tariff;
        this.state = State.ON_MOVE;
        direction = BusDirection.FORWARD;
    }

    @Override
    public void initialize(Context context) {
        super.initialize(context);

        capacity = context.getConfiguration().getBusCapacity();
        passengerList = new ArrayList<>();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void update(long simulationTime) {
        switch (state) {
            case ON_MOVE:
                if (isReached()) {
                    dispatchEvent(getEventBuilder(EVENT_ARRIVED_TO_STOP).withTargetEntity(currentStop).build());

                    if (!tariff.hasNextStop(currentStop, direction)) {
                        direction = direction == BusDirection.FORWARD ? BusDirection.BACKWARD : BusDirection.FORWARD;
                    }

                    currentStop = tariff.getNextStop(currentStop, direction);
                    state = State.WAITING_AT_STOP;
                    waitTime = simulationTime;
                    return;
                }

                speed = currentStop
                        .getRoadPosition()
                        .diffFloat(actualPosition)
                        .getUnitVector()
                        .multiply(
                                getContext().getConfiguration().getBusSpeed()
                        )
                        .multiply(-1);

                actualPosition = actualPosition.add(speed);
                setPosition(actualPosition.round());

                break;
            case WAITING_AT_STOP:
                if (simulationTime - waitTime >= getContext().getConfiguration().getBusWaitTime()) {
                    state = State.ON_MOVE;
                }
                break;
        }
    }

    public boolean takePassenger(Passenger passenger) {
        if (passengerList.size() >= capacity) {
            return false;
        }

        passengerList.add(passenger);
        return true;
    }

    public void leavePassenger(Passenger passenger) {
        if (!passengerList.contains(passenger)) {
            return;
        }

        passengerList.remove(passenger);
    }

    private boolean isReached() {
        return currentStop.getRoadPosition().equals(getPosition());
    }

    public List<Stop> getNextStops() {
        List<Stop> stops = new ArrayList<>(tariff.getStopList());

        if (direction == BusDirection.BACKWARD) {
            Collections.reverse(stops);
        }

        return stops.subList(stops.indexOf(currentStop), stops.size());
    }

    @Override
    public void draw(Graphics graphics) {
        Vector2<Integer> ratio = getContext().getDisplayRatio(graphics);
        Vector2<Integer> scaledPosition = new Vector2<>(
                getPosition().getX() * ratio.getX(),
                getPosition().getY() * ratio.getY()
        );

        graphics.setColor(tariff.getColor());
        graphics.fillRect(scaledPosition.getX(), scaledPosition.getY(), ratio.getX(), ratio.getY());

        graphics.setColor(Color.WHITE);
        graphics.drawString(
                String.valueOf(passengerList.size())
                , scaledPosition.getX(), scaledPosition.getY() + ratio.getY());
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case Passenger.EVENT_OFF_BUS:
                Passenger passenger = (Passenger) event.getSourceEntity();
                leavePassenger(passenger);
                break;
        }
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public BusDirection getDirection() {
        return direction;
    }

    public void setDirection(BusDirection direction) {
        this.direction = direction;
    }

    public Stop getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(Stop currentStop) {
        this.currentStop = currentStop;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPassengerCount() {
        return passengerList.size();
    }

    @Override
    public void setPosition(Vector2<Integer> position) {
        super.setPosition(position);

        if (actualPosition == null) {
            actualPosition = position.multiply(1);
        }
    }

    public enum BusDirection {
        FORWARD,
        BACKWARD
    }

    public enum State {
        ON_MOVE,
        WAITING_AT_STOP
    }
}
