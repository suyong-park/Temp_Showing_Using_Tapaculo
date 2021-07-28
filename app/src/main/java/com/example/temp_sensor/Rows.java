package com.example.temp_sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rows implements Serializable {
    @SerializedName("device_mac")
    @Expose
    private String device_mac;

    @SerializedName("device_name")
    @Expose
    private String device_name;

    @SerializedName("device_model")
    @Expose
    private String device_model;

    @SerializedName("device_interval")
    @Expose
    private String device_interval;

    @SerializedName("device_version")
    @Expose
    private String device_version;

    @SerializedName("sensor_model")
    @Expose
    private String sensor_model;

    @SerializedName("sensor_mac")
    @Expose
    private String sensor_mac;

    @SerializedName("ch_no")
    @Expose
    private String ch_no;

    @SerializedName("ch_name")
    @Expose
    private String ch_name;

    @SerializedName("ch_unit")
    @Expose
    private String ch_unit;

    @SerializedName("VS_INTERVAL")
    @Expose
    private String VS_INTERVAL;

    @SerializedName("VS_SPLRATE")
    @Expose
    private String VS_SPLRATE;

    @SerializedName("device_splrate")
    @Expose
    private String device_splrate;

    @SerializedName("battery")
    @Expose
    private String battery;

    @SerializedName("lqi")
    @Expose
    private String lqi;

    @SerializedName("last_update")
    @Expose
    private String last_update;

    @SerializedName("ch_value")
    @Expose
    private String ch_value;

    @SerializedName("ch_timestamp")
    @Expose
    private String ch_timestamp;

    public String getDevice_version () {
        return device_version;
    }

    public void setDevice_version (String device_version) {
        this.device_version = device_version;
    }

    public String getSensor_mac () {
        return sensor_mac;
    }

    public void setSensor_mac (String sensor_mac) {
        this.sensor_mac = sensor_mac;
    }

    public String getDevice_model () {
        return device_model;
    }

    public void setDevice_model (String device_model) {
        this.device_model = device_model;
    }

    public String  getVS_SPLRATE () {
        return VS_SPLRATE;
    }

    public void setVS_SPLRATE (String  VS_SPLRATE) {
        this.VS_SPLRATE = VS_SPLRATE;
    }

    public String getLqi () {
        return lqi;
    }

    public void setLqi (String lqi) {
        this.lqi = lqi;
    }

    public String getDevice_interval () {
        return device_interval;
    }

    public void setDevice_interval (String device_interval) {
        this.device_interval = device_interval;
    }

    public String getSensor_model () {
        return sensor_model;
    }

    public void setSensor_model (String sensor_model) {
        this.sensor_model = sensor_model;
    }

    public String getVS_INTERVAL () {
        return VS_INTERVAL;
    }

    public void setVS_INTERVAL (String VS_INTERVAL) {
        this.VS_INTERVAL = VS_INTERVAL;
    }

    public String getCh_name () {
        return ch_name;
    }

    public void setCh_name (String ch_name) {
        this.ch_name = ch_name;
    }

    public String getDevice_splrate () {
        return device_splrate;
    }

    public void setDevice_splrate (String device_splrate) {
        this.device_splrate = device_splrate;
    }

    public String getCh_unit () {
        return ch_unit;
    }

    public void setCh_unit (String ch_unit) {
        this.ch_unit = ch_unit;
    }

    public String getBattery () {
        return battery;
    }

    public void setBattery (String battery) {
        this.battery = battery;
    }

    public String getDevice_mac () {
        return device_mac;
    }

    public void setDevice_mac (String device_mac) {
        this.device_mac = device_mac;
    }

    public String getDevice_name () {
        return device_name;
    }

    public void setDevice_name (String device_name) {
        this.device_name = device_name;
    }

    public String getCh_value () {
        return ch_value;
    }

    public void setCh_value (String ch_value) {
        this.ch_value = ch_value;
    }

    public String getLast_update () {
        return last_update;
    }

    public void setLast_update (String last_update) {
        this.last_update = last_update;
    }

    public String getCh_no () {
        return ch_no;
    }

    public void setCh_no (String ch_no) {
        this.ch_no = ch_no;
    }

    public String getCh_timestamp () {
        return ch_timestamp;
    }

    public void setCh_timestamp (String ch_timestamp) {
        this.ch_timestamp = ch_timestamp;
    }

    @Override
    public String toString() {
        return "[device_version = "+device_version+", sensor_mac = "+sensor_mac+", device_model = "+device_model+", VS_SPLRATE = "+VS_SPLRATE+", lqi = "+lqi+", device_interval = "+device_interval+", sensor_model = "+sensor_model+", VS_INTERVAL = "+VS_INTERVAL+", ch_name = "+ch_name+", device_splrate = "+device_splrate+", ch_unit = "+ch_unit+", battery = "+battery+", device_mac = "+device_mac+", device_name = "+device_name+", ch_value = "+ch_value+", last_update = "+last_update+", ch_no = "+ch_no+", ch_timestamp = "+ch_timestamp+"]";
    }
}
