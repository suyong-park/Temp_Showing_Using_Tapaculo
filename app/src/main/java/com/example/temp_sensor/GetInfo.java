package com.example.temp_sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GetInfo implements Serializable{

    @SerializedName("device_info_update") // GSON Annotation. JSON으로 serialize될 때 매칭하는 이름을 명시적으로 지정함. 이 때 문자열 값은 실제 JSON 속성값이어야 함.
    @Expose // object 중 해당 값이 null 인 경우, 자동으로 해당 object는 무시하고 JSON을 만들게 됨.
    private String device_info_update;

    @SerializedName("device_version")
    @Expose
    private String device_version;

    @SerializedName("device_mac")
    @Expose
    private String device_mac;

    @SerializedName("device_name")
    @Expose
    private String device_name;

    @SerializedName("sensors")
    @Expose
    private Sensors[] sensors;

    @SerializedName("device_model")
    @Expose
    private String device_model;

    @SerializedName("device_interval")
    @Expose
    private String device_interval;

    @SerializedName("device_splrate")
    @Expose
    private String device_splrate;

    @SerializedName("device_lastupdate")
    @Expose
    private String device_lastupdate;

    @SerializedName("status")
    @Expose
    private String status;

    public String getDevice_info_update () {
        return device_info_update;
    }

    public void setDevice_info_update (String device_info_update) {
        this.device_info_update = device_info_update;
    }

    public String getDevice_version () {
        return device_version;
    }

    public void setDevice_version (String device_version) {
        this.device_version = device_version;
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

    public Sensors[] getSensors () {
        return sensors;
    }

    public void setSensors (Sensors[] sensors) {
        this.sensors = sensors;
    }

    public String getDevice_model () {
        return device_model;
    }

    public void setDevice_model (String device_model) {
        this.device_model = device_model;
    }

    public String getDevice_interval () {
        return device_interval;
    }

    public void setDevice_interval (String device_interval) {
        this.device_interval = device_interval;
    }

    public String getDevice_splrate () {
        return device_splrate;
    }

    public void setDevice_splrate (String device_splrate) {
        this.device_splrate = device_splrate;
    }

    public String getDevice_lastupdate () {
        return device_lastupdate;
    }

    public void setDevice_lastupdate (String device_lastupdate) {
        this.device_lastupdate = device_lastupdate;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[device_info_update = "+device_info_update+", device_version = "+device_version+", device_mac = "+device_mac+", device_name = "+device_name+", sensors = "+sensors+", device_model = "+device_model+", device_interval = "+device_interval+", device_splrate = "+device_splrate+", device_lastupdate = "+device_lastupdate+", status = "+status+"]";
    }
}