package com.zamska.ptsim.simulation;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Configuration {
    private String map;
    private long maxTicks;
    private long tickDelay;
    private int busCapacity;
    private int numberOfBuses;
    private int busDepartureInterval;
    private int busWaitTime;
    private float busSpeed;
    private float passengerArrivalRate;
    private String dataDirectory;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public long getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(long maxTicks) {
        this.maxTicks = maxTicks;
    }

    public long getTickDelay() {
        return tickDelay;
    }

    public void setTickDelay(long tickDelay) {
        this.tickDelay = tickDelay;
    }

    public int getBusCapacity() {
        return busCapacity;
    }

    public void setBusCapacity(int busCapacity) {
        this.busCapacity = busCapacity;
    }

    public int getNumberOfBuses() {
        return numberOfBuses;
    }

    public void setNumberOfBuses(int numberOfBuses) {
        this.numberOfBuses = numberOfBuses;
    }

    public int getBusDepartureInterval() {
        return busDepartureInterval;
    }

    public void setBusDepartureInterval(int busDepartureInterval) {
        this.busDepartureInterval = busDepartureInterval;
    }

    public float getPassengerArrivalRate() {
        return passengerArrivalRate;
    }

    public void setPassengerArrivalRate(float passengerArrivalRate) {
        this.passengerArrivalRate = passengerArrivalRate;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public int getBusWaitTime() {
        return busWaitTime;
    }

    public void setBusWaitTime(int busWaitTime) {
        this.busWaitTime = busWaitTime;
    }

    public float getBusSpeed() {
        return busSpeed;
    }

    public void setBusSpeed(float busSpeed) {
        this.busSpeed = busSpeed;
    }

    public static Configuration parse(String filePath) {
        return new Gson()
                .fromJson(
                        readConfigurationFile(filePath),
                        Configuration.class
                );
    }

    protected static String readConfigurationFile(String filePath) {
        StringBuilder sb = new StringBuilder();

        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
        } catch (IOException e) {
            return null;
        }

        return sb.toString();
    }
}
