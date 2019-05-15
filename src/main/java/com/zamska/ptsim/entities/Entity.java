package com.zamska.ptsim.entities;

import com.zamska.ptsim.event.Event;
import com.zamska.ptsim.event.EventSubscriber;
import com.zamska.ptsim.gui.Drawable;
import com.zamska.ptsim.models.Vector2;
import com.zamska.ptsim.simulation.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public abstract class Entity implements Drawable, Updatable, Initializable, EventSubscriber {
    public static final String EVENT_CREATE = "EVENT_CREATE";
    public static final String EVENT_DESTROY = "EVENT_DESTROY";

    private static Logger logger = LogManager.getLogger(Entity.class);

    private UUID id;
    private Context context;
    private Vector2<Integer> position;

    public Entity() {
        id = UUID.randomUUID();
        position = new Vector2<>(0, 0);
    }

    public UUID getId() {
        return id;
    }

    protected Context getContext() {
        return context;
    }

    public Vector2<Integer> getPosition() {
        return position;
    }

    public void setPosition(Vector2<Integer> position) {
        this.position = position;
    }

    public abstract String getType();

    @Override
    public void initialize(Context context) {
        this.context = context;

        dispatchEvent(getEventBuilder(EVENT_CREATE).build());
        context.getEventDispatcher().subscribe(this, this);
    }

    protected Event.Builder getEventBuilder(String eventType) {
        return new Event.Builder(eventType)
                .withSourceEntity(this);
    }

    protected void dispatchEvent(Event event) {
        context.getEventDispatcher().dispatch(event);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entity)) {
            return false;
        }

        Entity otherEntity = (Entity) obj;
        return otherEntity.getId().equals(getId());
    }

    @Override
    public String toString() {
        return String.format(
                "[%s] - %s",
                getId().toString(),
                getType()
        );
    }
}
