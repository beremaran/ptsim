package com.zamska.ptsim.simulation;

import com.zamska.ptsim.entities.BusManager;
import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.event.EventSubscriber;
import com.zamska.ptsim.gui.Drawable;
import com.zamska.ptsim.statistics.Statistics;
import com.zamska.ptsim.utils.InvalidMapFileException;
import com.zamska.ptsim.utils.MapParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Simulation extends JPanel implements Drawable, Runnable, EventSubscriber {
    public static final String EVENT_START = "EVENT_START";
    public static final String EVENT_PAUSE = "EVENT_PAUSE";
    public static final String EVENT_STOP = "EVENT_STOP";

    private static Logger logger = LogManager.getLogger(Simulation.class);

    private Context context;
    private boolean isConfigured;
    private SimulationState state;
    private Statistics statistics;

    public Simulation() {
        isConfigured = false;
        state = SimulationState.STOPPED;
        statistics = new Statistics();
    }

    public void setup(String configurationPath) throws IOException, InvalidMapFileException {
        logger.info(String.format("Loading configuration from %s", configurationPath));
        context = new Context(Configuration.parse(configurationPath));
        logger.info("Configuration is loaded.");

        parseMap();
        createManagers();
        subscribeToEvents();

        statistics.addDataCollectorListener(context.getEventDispatcher());

        isConfigured = true;
    }

    private void parseMap() throws IOException, InvalidMapFileException {
        MapParser mapParser = new MapParser(context);
        mapParser.parse();
        context.addEntities(mapParser.getEntities());
    }

    private void createManagers() {
        context.addEntity(new BusManager());
    }

    private void subscribeToEvents() {
        context.getEventDispatcher().subscribe(EVENT_START, this);
        context.getEventDispatcher().subscribe(EVENT_PAUSE, this);
        context.getEventDispatcher().subscribe(EVENT_STOP, this);
    }

    public void tick() throws NotConfiguredException {
        if (!isConfigured) {
            throw new NotConfiguredException();
        }

        if (!shouldTick()) {
            return;
        }

        logger.info(String.format("T = %d", context.getTime().getCurrentTime()));

        processEvents();
        updateEntities();
        updateStatistics(context.getTime().getCurrentTime());
        incrementTime();
    }

    private boolean shouldTick() {
        return state == SimulationState.RUNNING;
    }

    private void processEvents() {
        context.getEventDispatcher().update(
                context.getTime().getCurrentTime()
        );

        repaint();
    }

    private void incrementTime() {
        context.getTime().increment();
    }

    private void updateEntities() {
        context.getEntities()
                .forEach(entity -> entity.update(context.getTime().getCurrentTime()));

        repaint();
    }

    private void updateStatistics(long simulationTime) {
        statistics.update(simulationTime);
    }

    @Override
    public void draw(Graphics graphics) {
        if (!isConfigured) {
            return;
        }

        context.getEntities()
                .forEach(entity -> entity.draw(graphics));
    }

    @Override
    public void run() {
        logger.info("Simulation is starting ...");
        state = SimulationState.RUNNING;

        while (context.getTime().getCurrentTime() <= context.getConfiguration().getMaxTicks()) {
            try {
                tick();
                repaint();
            } catch (NotConfiguredException e) {
                e.printStackTrace();
                return;
            }

            delay();
        }

        logger.info("Maximum tick count is reached.");
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case EVENT_START:
                logger.info("Simulation is started.");
                state = SimulationState.RUNNING;
                break;
            case EVENT_PAUSE:
                logger.info("Simulation is paused.");
                state = SimulationState.PAUSED;
                break;
            case EVENT_STOP:
                logger.info("Simulation is stopped.");
                state = SimulationState.STOPPED;
                break;
        }
    }

    private void delay() {
        if (context.getConfiguration().getTickDelay() > 0) {
            try {
                Thread.sleep(context.getConfiguration().getTickDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
