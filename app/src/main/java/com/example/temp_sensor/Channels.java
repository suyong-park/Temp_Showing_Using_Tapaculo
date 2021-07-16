package com.example.temp_sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Channels implements Serializable {
    @SerializedName("ch_value")
    @Expose
    private String ch_value;

    @SerializedName("ch_is_reg")
    @Expose
    private String ch_is_reg;

    @SerializedName("ch_timestamp")
    @Expose
    private String ch_timestamp;

    @SerializedName("ch_name")
    @Expose
    private String ch_name;

    @SerializedName("ch_unit")
    @Expose
    private String ch_unit;

    public String getCh_value () {
        return ch_value;
    }

    public void setCh_value (String ch_value) {
        this.ch_value = ch_value;
    }

    public String getCh_is_reg () {
        return ch_is_reg;
    }

    public void setCh_is_reg (String ch_is_reg) {
        this.ch_is_reg = ch_is_reg;
    }

    public String getCh_timestamp () {
        return ch_timestamp;
    }

    public void setCh_timestamp (String ch_timestamp) {
        this.ch_timestamp = ch_timestamp;
    }

    public String getCh_name () {
        return ch_name;
    }

    public void setCh_name (String ch_name) {
        this.ch_name = ch_name;
    }

    public String getCh_unit () {
        return ch_unit;
    }

    public void setCh_unit (String ch_unit) {
        this.ch_unit = ch_unit;
    }

    @Override
    public String toString() {
        return "ClassPojo [ch_value = "+ch_value+", ch_is_reg = "+ch_is_reg+", ch_timestamp = "+ch_timestamp+", ch_name = "+ch_name+", ch_unit = "+ch_unit+"]";
    }
}
