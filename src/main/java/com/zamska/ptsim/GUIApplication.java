package com.zamska.ptsim;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.simulation.Simulation;
import com.zamska.ptsim.statistics.CollectorResult;
import com.zamska.ptsim.statistics.CollectorResultFormatter;
import com.zamska.ptsim.statistics.DataCollector;
import com.zamska.ptsim.statistics.TabularFormatter;
import com.zamska.ptsim.statistics.collectors.BusUtilizationCollector;
import com.zamska.ptsim.statistics.collectors.PassengerWaitTimeCollector;
import com.zamska.ptsim.statistics.collectors.TariffWaitTimeCollector;
import com.zamska.ptsim.utils.InvalidMapFileException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GUIApplication {
    private Simulation simulation;
    private JFrame simulationFrame;
    private JFrame statisticsFrame;
    private CollectorResultFormatter resultFormatter;

    public GUIApplication() {
        simulation = new Simulation();
        resultFormatter = new TabularFormatter();
    }

    public void run(String[] args) {
        if (args.length == 0) {
            System.err.println("Where is the configuration?");
            System.exit(1);
        }

        try {
            simulation.setup(args[0]);
        } catch (IOException | InvalidMapFileException e) {
            e.printStackTrace();
        }

        setupDataCollectors();
        startSimulationDisplay();
        startStatisticsDisplay();

        new Thread(simulation).run();
    }

    private void setupDataCollectors() {
        simulation.getStatistics().registerDataCollector(new TariffWaitTimeCollector());
        simulation.getStatistics().registerDataCollector(new BusUtilizationCollector());
    }

    private void startSimulationDisplay() {
        simulationFrame = new JFrame("PTSIM");
        simulationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        simulationFrame.setResizable(false);

        simulationFrame.getContentPane().add(simulation);
        simulationFrame.pack();
        simulationFrame.setVisible(true);
    }

    private void startStatisticsDisplay() {
        statisticsFrame = new JFrame("PTSIM Statistics");
        statisticsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statisticsFrame.setResizable(false);
        statisticsFrame.setLocationRelativeTo(simulationFrame);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(false);
        textArea.setPreferredSize(new Dimension(640, 640));

        statisticsFrame.add(textArea, BorderLayout.CENTER);

        simulation.getStatistics().addCollectorResultListener(results -> {
            if (resultFormatter != null)
                textArea.setText(resultFormatter.format(results));
        });

        statisticsFrame.pack();
        statisticsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUIApplication().run(args);
    }
}
