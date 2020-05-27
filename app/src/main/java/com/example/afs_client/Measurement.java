package com.example.afs_client;

import java.util.Arrays;
import java.util.Calendar;

public class Measurement {

    private long timestamp;
    private float speed;
    private float dynamicPressure;
    private float staticPressure;
    private float temperature;

    public Measurement() {
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.speed = 0.0f;
        this.dynamicPressure = 0.0f;
        this.staticPressure = 0.0f;
        this.temperature = 0.0f;
    }

    public Measurement(byte[] dataString) {
        String arr[] = new String(dataString).split("/");
        try {
            if (arr.length == 4){
                timestamp = Calendar.getInstance().getTimeInMillis();
                speed = Float.parseFloat(arr[0]);
                dynamicPressure = Float.parseFloat(arr[1]);
                staticPressure = Float.parseFloat(arr[2]);
                temperature = Float.parseFloat(arr[3]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDynamicPressure() {
        return dynamicPressure;
    }

    public float getStaticPressure() {
        return staticPressure;
    }

    public float getTemperature() {
        return temperature;
    }
}
