package com.example.temp_sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Sensors implements Serializable {
    @SerializedName("sensor_mac")
    @Expose
    private String sensor_mac;

    @SerializedName("channels")
    @Expose
    private Channels[] channels;

    @SerializedName("lqi")
    @Expose
    private String lqi;

    @SerializedName("last_update")
    @Expose
    private String last_update;

    @SerializedName("sensor_model")
    @Expose
    private String sensor_model;

    @SerializedName("battery")
    @Expose
    private String battery;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getSensor_mac () {
        return sensor_mac;
    }

    public void setSensor_mac (String sensor_mac) {
        this.sensor_mac = sensor_mac;
    }

    public Channels[] getChannels () {
        return channels;
    }

    public void setChannels (Channels[] channels) {
        this.channels = channels;
    }

    public String getLqi () {
        return lqi;
    }

    public void setLqi (String lqi) {
        this.lqi = lqi;
    }

    public String getLast_update () {
        return last_update;
    }

    public void setLast_update (String last_update) {
        this.last_update = last_update;
    }

    public String getSensor_model () {
        return sensor_model;
    }

    public void setSensor_model (String sensor_model) {
        this.sensor_model = sensor_model;
    }

    public String getBattery () {
        return battery;
    }

    public void setBattery (String battery) {
        this.battery = battery;
    }

    public String getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ClassPojo [sensor_mac = "+sensor_mac+", channels = "+channels+", lqi = "+lqi+", last_update = "+last_update+", sensor_model = "+sensor_model+", battery = "+battery+", timestamp = "+timestamp+"]";
    }
}

