package com.zamska.ptsim;

import com.zamska.ptsim.simulation.Simulation;
import com.zamska.ptsim.utils.InvalidMapFileException;

import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();

        try {
            simulation.setup("/home/beremaran/dev/ptsim/data/config.json");
        } catch (IOException | InvalidMapFileException e) {
            e.printStackTrace();
        }
        
        simulation.run();
    }
}
