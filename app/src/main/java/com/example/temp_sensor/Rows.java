package com.example.temp_sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rows {

    @SerializedName("sensor_id")
    @Expose
    private String sensor_id;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("last_update")
    @Expose
    private String last_update;

    @SerializedName("battery")
    @Expose
    private String battery;

    @SerializedName("lqi")
    @Expose
    private String lqi;

    @SerializedName("sensor_model")
    @Expose
    private String sensor_model;

    public String getSensor_id () {
        return sensor_id;
    }

    public void setSensor_id (String sensor_id) {
        this.sensor_id = sensor_id;
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

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ClassPojo [sensor_id = "+sensor_id+", lqi = "+lqi+", last_update = "+last_update+", sensor_model = "+sensor_model+", battery = "+battery+", value = "+value+", timestamp = "+timestamp+"]";
    }
}

