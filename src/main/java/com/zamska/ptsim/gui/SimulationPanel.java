package com.zamska.ptsim.gui;

import com.zamska.ptsim.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel {
    private Simulation simulation;

    public SimulationPanel(Simulation simulation) {
        super(new FlowLayout(), true);
        this.simulation = simulation;
    }

    public void run() {
        new Thread(simulation).start();
    }
}
