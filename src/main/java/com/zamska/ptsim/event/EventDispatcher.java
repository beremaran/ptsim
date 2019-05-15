package com.zamska.ptsim.event;

import com.zamska.ptsim.entities.Updatable;
import com.zamska.ptsim.entities.Entity;
import com.zamska.ptsim.statistics.DataCollector;
import com.zamska.ptsim.statistics.DataCollectorListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher implements Updatable, DataCollectorListener {
    private static EventDispatcher instance;

    private static Logger logger = LogManager.getLogger(EventDispatcher.class);

    private List<Event> eventQueue;
    private List<DataCollector> dataCollectors;

    private Map<String, List<EventSubscriber>> eventSubscribers;
    private Map<UUID, List<EventSubscriber>> targetSubscribers;

    private EventDispatcher() {
        eventSubscribers = new HashMap<>();
        targetSubscribers = new HashMap<>();
        eventQueue = new CopyOnWriteArrayList<>();
        dataCollectors = new CopyOnWriteArrayList<>();
    }

    public static synchronized EventDispatcher getInstance() {
        if (instance == null) {
            instance = new EventDispatcher();
        }

        return instance;
    }

    public void subscribe(String eventType, EventSubscriber eventSubscriber) {
        if (!eventSubscribers.containsKey(eventType)) {
            eventSubscribers.put(eventType, new LinkedList<>());
        }

        eventSubscribers
                .get(eventType)
                .add(eventSubscriber);
    }

    public void subscribe(Entity entity, EventSubscriber eventSubscriber) {
        if (!targetSubscribers.containsKey(entity.getId())) {
            targetSubscribers.put(entity.getId(), new LinkedList<>());
        }

        targetSubscribers
                .get(entity.getId())
                .add(eventSubscriber);
    }

    public void dispatch(Event event) {
        logger.info(String.format("EVENT: %s", event.toString()));
        eventQueue.add(event);

        dataCollectors.forEach(dc -> dc.collect(event));
    }

    public void registerCollector(DataCollector dataCollector) {
        if (!dataCollectors.contains(dataCollector))
            dataCollectors.add(dataCollector);
    }

    @Override
    public void update(long simulationTime) {
        Queue<Event> events = new ArrayDeque<>(eventQueue);
        eventQueue.clear();

        while (events.size() > 0) {
            Event e = events.poll();
            String eventType = e.getType();

            if (eventSubscribers.containsKey(eventType)) {
                for (EventSubscriber eventSubscriber : eventSubscribers.get(eventType)) {
                    eventSubscriber.onEvent(e);
                }
            }

            if (e.getTargetEntity() != null) {
                UUID targetUUID = e.getTargetEntity().getId();
                if (targetSubscribers.containsKey(targetUUID)) {
                    for (EventSubscriber eventSubscriber : targetSubscribers.get(targetUUID)) {
                        eventSubscriber.onEvent(e);
                    }
                }
            }
        }
    }

    @Override
    public void onRegisterDataCollector(DataCollector collector) {
        registerCollector(collector);
    }
}
