package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.random.IntegerGenerator;
import com.zamska.ptsim.random.UniformIntegerGenerator;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Tariff extends Entity {
    public static final String TYPE = "TARIFF";

    private Color color;
    private List<Stop> stopList;

    public Tariff() {
        super();

        color = getRandomColor();
        stopList = new LinkedList<>();
    }

    private Color getRandomColor() {
        IntegerGenerator integerGenerator = new UniformIntegerGenerator();

        return new Color(
                integerGenerator.nextInt(255),
                integerGenerator.nextInt(255),
                integerGenerator.nextInt(255)
        );
    }

    public List<Stop> getStopList() {
        return stopList;
    }

    public void setStopList(List<Stop> stopList) {
        this.stopList = stopList;
    }

    public Stop getFirstStop() {
        return stopList.get(0);
    }

    public Stop getLastStop() {
        return stopList.get(stopList.size() - 1);
    }

    public Stop getNextStop(Stop currentStop, Bus.BusDirection direction) {
        int offset = direction == Bus.BusDirection.FORWARD ? 1 : -1;
        int currentIndex = stopList.indexOf(currentStop);
        int newIndex = currentIndex + offset;

        if (newIndex < 0 || newIndex >= stopList.size()) {
            return null;
        }

        return stopList.get(newIndex);
    }

    public boolean hasNextStop(Stop currentStop, Bus.BusDirection direction) {
        return getNextStop(currentStop, direction) != null;
    }

    public boolean contains(Stop stop) {
        return stopList.contains(stop);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void update(long simulationTime) {

    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void draw(Graphics graphics) {

    }

    @Override
    public String toString() {
        return stopList.stream().map(s -> String.valueOf(s.getName())).collect(Collectors.joining("-"));
    }
}
