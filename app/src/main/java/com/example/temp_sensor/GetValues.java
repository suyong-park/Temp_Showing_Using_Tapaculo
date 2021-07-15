package com.example.temp_sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetValues {

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("rows")
    @Expose
    private Rows[] rows;

    @SerializedName("status")
    @Expose
    private String status;

    public String getCount () {
        return count;
    }

    public void setCount (String count) {
        this.count = count;
    }

    public Rows[] getRows () {
        return rows;
    }

    public void setRows (Rows[] rows) {
        this.rows = rows;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [count = "+count+", rows = "+rows+", status = "+status+"]";
    }
}