package com.zamska.ptsim.statistics;

public class CollectorResult {
    private String key;
    private float value;

    public CollectorResult(String key, float value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
