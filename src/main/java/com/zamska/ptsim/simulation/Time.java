package com.zamska.ptsim.simulation;

public class Time {
    private static Time instance;
    private long currentTime;

    private Time() {
        currentTime = 0;
    }

    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }

        return instance;
    }

    public synchronized long getCurrentTime() {
        return currentTime;
    }

    public synchronized void increment() {
        currentTime++;
    }
}
