package com.zamska.ptsim.simulation;

import com.zamska.ptsim.entities.Entity;
import com.zamska.ptsim.entities.Map;
import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.event.EventDispatcher;
import com.zamska.ptsim.event.EventSubscriber;
import com.zamska.ptsim.models.Vector2;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Context implements EventSubscriber {
    private Time time;
    private List<Entity> entities;
    private Configuration configuration;
    private EventDispatcher eventDispatcher;

    public Context(Configuration configuration) {
        time = Time.getInstance();
        entities = new CopyOnWriteArrayList<>();
        this.configuration = configuration;
        eventDispatcher = EventDispatcher.getInstance();

        eventDispatcher.subscribe(Entity.EVENT_CREATE, this);
        eventDispatcher.subscribe(Entity.EVENT_DESTROY, this);
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case Entity.EVENT_CREATE:
                if (!entities.contains(event.getSourceEntity()))
                    entities.add(event.getSourceEntity());
                break;
            case Entity.EVENT_DESTROY:
                entities.remove(event.getSourceEntity());
                break;
        }
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntities(String entityType) {
        return entities.stream().filter(e -> e.getType().equals(entityType)).collect(Collectors.toList());
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        if (entities.contains(entity))
            return;

        entity.initialize(this);
        entities.add(entity);
    }

    public void addEntities(Collection<Entity> entities) {
        entities.forEach(this::addEntity);
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Vector2<Integer> getDisplayRatio(Graphics graphics) {
        Map map = (Map) getEntities(Map.TYPE).get(0);
        Rectangle clipBounds = graphics.getClipBounds();

        return new Vector2<>(
                clipBounds.width / map.getMapData().length,
                clipBounds.height / map.getMapData()[0].length
        );
    }
}
