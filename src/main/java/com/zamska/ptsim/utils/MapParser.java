package com.zamska.ptsim.utils;

import com.zamska.ptsim.entities.Entity;
import com.zamska.ptsim.entities.Map;
import com.zamska.ptsim.entities.Stop;
import com.zamska.ptsim.entities.Tariff;
import com.zamska.ptsim.simulation.Context;

import java.io.*;
import java.util.*;

public class MapParser {
    private Context context;
    private List<Entity> entities;

    public MapParser(Context context) {
        this.context = context;
        entities = new LinkedList<>();
    }

    public void parse() throws IOException, InvalidMapFileException {
        Map map = new Map();
        entities.add(map);

        Queue<String> lines = readMapFile(getMapFile());

        String groundLine = lines.poll();
        String roadLine = lines.poll();
        if (groundLine == null || roadLine == null) {
            throw new InvalidMapFileException();
        }

        map.setGroundBlock(groundLine.charAt(0));
        map.setRoadBlock(roadLine.charAt(0));

        String stopNamesLine = lines.poll();
        if (stopNamesLine == null) {
            throw new InvalidMapFileException();
        }

        String[] stopNames = stopNamesLine.split("\\s");
        for (String stopName : stopNames) {
            Stop stop = new Stop(stopName.charAt(0));
            entities.add(stop);
        }

        String tariffCountLine = lines.poll();
        if (tariffCountLine == null) {
            throw new InvalidMapFileException();
        }

        int tariffCount = Integer.valueOf(tariffCountLine);
        for (int i = 0; i < tariffCount; i++) {
            String tariffLine = lines.poll();
            if (tariffLine == null) {
                throw new InvalidMapFileException();
            }

            Tariff tariff = new Tariff();
            entities.add(tariff);

            String[] tariffStops = tariffLine.split("\\s");

            for (String tariffStop : tariffStops) {
                tariff.getStopList().add(
                        getStopByName(tariffStop)
                );
            }
        }

        char[][] mapData = new char[lines.size()][];
        for (int i = 0; i < mapData.length; i++) {
            String line = lines.poll();
            if (line == null) {
                throw new InvalidMapFileException();
            }

            String[] tokens = line.split("\\s");
            mapData[i] = new char[tokens.length];

            for (int j = 0; j < tokens.length; j++) {
                mapData[i][j] = tokens[j].charAt(0);

                Stop stop = getStopByName(tokens[j]);
                if (stop != null) {
                    stop.getPosition().setX(j);
                    stop.getPosition().setY(i);
                }
            }
        }

        map.setMapData(mapData);
    }

    private File getMapFile() {
        String dataDirectoryPath = context.getConfiguration().getDataDirectory();
        String mapFilename = context.getConfiguration().getMap();
        return new File(new File(dataDirectoryPath, "maps"), String.format("%s.map", mapFilename));
    }

    private Queue<String> readMapFile(File mapFile) throws IOException {
        Queue<String> lines = new ArrayDeque<>();

        FileReader fr = new FileReader(mapFile);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("#.*$", "").replaceAll("\\s$", "");
            lines.add(line);
        }

        br.close();

        return lines;
    }

    private Stop getStopByName(String name) {
        return getStopByName(name.charAt(0));
    }

    private Stop getStopByName(char name) {
        return entities.stream()
                .filter(e -> e instanceof Stop)
                .map(e -> (Stop) e)
                .filter(e -> e.getName() == name)
                .findFirst()
                .orElse(null);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
