package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.models.Vector2;
import com.zamska.ptsim.random.IntegerGenerator;
import com.zamska.ptsim.random.PoissonIntegerGenerator;
import com.zamska.ptsim.simulation.Context;
import com.zamska.ptsim.utils.RandomSelector;
import com.zamska.ptsim.utils.Selector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Stop extends Entity {
    public static final String TYPE = "STOP";

    private char name;

    private static Logger logger = LogManager.getLogger(Stop.class);

    private List<Passenger> passengerList;
    private long generateNextPassengerAt;
    private IntegerGenerator integerGenerator;

    public Stop(char name) {
        super();

        this.name = name;
        passengerList = new LinkedList<>();
    }

    @Override
    public void initialize(Context context) {
        super.initialize(context);

        passengerList = new LinkedList<>();
        integerGenerator = new PoissonIntegerGenerator(context.getConfiguration().getPassengerArrivalRate());
        generateNextPassengerAt = context.getTime().getCurrentTime() + integerGenerator.nextInt();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void update(long simulationTime) {
        if (simulationTime == generateNextPassengerAt) {
            generatePassenger();
            calculateNextPassengerTime(simulationTime);
        }
    }

    private void generatePassenger() {
        Passenger passenger = new Passenger();

        passenger.setDepartureStop(this);
        passenger.setTargetStop(getRandomStop());

        passengerList.add(passenger);
        getContext().addEntity(passenger);
    }

    private Stop getRandomStop() {
        Selector<Tariff> tariffSelector = new RandomSelector<>();
        List<Tariff> tariffs = getContext().getEntities(Tariff.TYPE).stream().map(t -> (Tariff) t).filter(t -> t.getStopList().contains(this)).collect(Collectors.toList());

        Tariff tariff = tariffSelector.select(tariffs);
        List<Stop> stops = tariff.getStopList();

        stops = stops.stream().filter(e -> !e.getId().equals(getId())).collect(Collectors.toList());

        Selector<Stop> selector = new RandomSelector<>();
        return selector.select(stops);
    }

    private void calculateNextPassengerTime(long simulationTime) {
        generateNextPassengerAt = simulationTime + integerGenerator.nextInt();
    }

    @Override
    public void draw(Graphics graphics) {
        Vector2<Integer> ratio = getContext().getDisplayRatio(graphics);
        Vector2<Integer> scaledPosition = new Vector2<>(
                getPosition().getX() * ratio.getX(),
                getPosition().getY() * ratio.getY()
        );

        graphics.setColor(Color.PINK);
        graphics.fillRect(scaledPosition.getX(), scaledPosition.getY(), ratio.getX(), ratio.getY());

        graphics.setColor(Color.BLUE);
        graphics.drawString(String.valueOf(getName()), scaledPosition.getX(), scaledPosition.getY());

        graphics.setColor(Color.BLACK);
        graphics.drawString(String.valueOf(getPassengerCount()), scaledPosition.getX(), scaledPosition.getY() + ratio.getY());
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case Passenger.EVENT_ON_BUS:
                Passenger passenger = (Passenger) event.getSourceEntity();
                passengerList.remove(passenger);
                break;
        }
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public int getPassengerCount() {
        return passengerList.size();
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public Vector2<Integer> getRoadPosition() {
        Map map = (Map) getContext().getEntities(Map.TYPE).get(0);
        return getNeighbors()
                .stream()
                .filter(p -> map.getMapData()[p.getY()][p.getX()] == map.getRoadBlock())
                .findFirst()
                .orElse(null);
    }

    private List<Vector2<Integer>> getNeighbors() {
        Vector2<Integer> position = getPosition();
        List<Vector2<Integer>> neighbors = new ArrayList<>();
        Map map = (Map) getContext().getEntities(Map.TYPE).get(0);

        Vector2<Integer> mapSize = map.getSize();
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (x < 0 || x >= mapSize.getX() || y < 0 || y >= mapSize.getY()) {
                    continue;
                }

                if (map.getMapData()[y][x] != map.getRoadBlock())
                    continue;

                if (x != getPosition().getX() && y != getPosition().getY()) {
                    continue;
                }

                neighbors.add(new Vector2<>(x, y));
            }
        }

        return neighbors;
    }

    @Override
    public String toString() {
        return String.format(
                "%s - %s",
                super.toString(),
                getName()
        );
    }
}
