package com.zamska.ptsim.event;

import com.zamska.ptsim.simulation.Time;
import com.zamska.ptsim.entities.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    private UUID id;
    private String type;
    private Bundle bundle;
    private long timestamp;
    private Entity targetEntity;
    private Entity sourceEntity;

    public Event(String type) {
        this.type = type;
        bundle = new Bundle();
        id = UUID.randomUUID();
        timestamp = Time.getInstance().getCurrentTime();
    }

    public Event(String type, Entity sourceEntity) {
        this(type);
        this.sourceEntity = sourceEntity;
    }

    public Event(String type, Entity sourceEntity, Entity targetEntity) {
        this(type, sourceEntity);
        this.targetEntity = targetEntity;
    }

    public UUID getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Entity getSourceEntity() {
        return sourceEntity;
    }

    public void setSourceEntity(Entity sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Event)) {
            return false;
        }
        Event otherEvent = (Event) obj;
        return otherEvent.getId().equals(getId());
    }

    @Override
    public String toString() {
        String sourceEntityString = sourceEntity == null ? "?" : getSourceEntity().toString();
        String targetEntityString = targetEntity == null ? "" : getTargetEntity().toString();

        return String.format(
                "%s - %d - %s - %s",
                getType(), getTimestamp(), sourceEntityString, targetEntityString
        );
    }

    public static class Bundle {
        private Map<String, Object> data;

        public Bundle() {
            data = new HashMap<>();
        }

        public void put(String key, Object value) {
            data.put(key, value);
        }

        public Object get(String key) {
            return data.getOrDefault(key, null);
        }

        public String getString(String key) {
            return (String) get(key);
        }

        public int getInt(String key) {
            return (int) get(key);
        }

        public float getFloat(String key) {
            return (float) get(key);
        }

        public float getDouble(String key) {
            return (float) get(key);
        }

        public Entity getEntity(String key) {
            return (Entity) get(key);
        }
    }

    public static class Builder {
        private Event event;

        public Builder(String type) {
            event = new Event(type);
        }

        public Builder withSourceEntity(Entity sourceEntity) {
            event.setSourceEntity(sourceEntity);
            return this;
        }

        public Builder withTargetEntity(Entity targetEntity) {
            event.setTargetEntity(targetEntity);
            return this;
        }

        public Builder withBundle(Bundle bundle) {
            event.setBundle(bundle);
            return this;
        }

        public Builder withData(String key, Object value) {
            event.getBundle().put(key, value);
            return this;
        }

        public Event build() {
            return event;
        }
    }
}
