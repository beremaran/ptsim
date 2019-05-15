package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.models.Vector2;

import java.awt.*;

public class Map extends Entity {
    public static final String TYPE = "MAP";

    private char roadBlock;
    private char groundBlock;
    private char[][] mapData;

    public char getRoadBlock() {
        return roadBlock;
    }

    public void setRoadBlock(char roadBlock) {
        this.roadBlock = roadBlock;
    }

    public char getGroundBlock() {
        return groundBlock;
    }

    public void setGroundBlock(char groundBlock) {
        this.groundBlock = groundBlock;
    }

    public char[][] getMapData() {
        return mapData;
    }

    public void setMapData(char[][] mapData) {
        this.mapData = mapData;
    }

    public Vector2<Integer> getSize() {
        return new Vector2<>(mapData[0].length, mapData.length);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void update(long simulationTime) {

    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void draw(Graphics graphics) {
        Vector2<Integer> ratio = getContext().getDisplayRatio(graphics);

        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                Color color = mapData[y][x] == roadBlock ? Color.GRAY : Color.BLACK;

                graphics.setColor(color);
                graphics.fillRect(x * ratio.getX(), y * ratio.getY(), ratio.getX(), ratio.getY());
            }
        }
    }
}
