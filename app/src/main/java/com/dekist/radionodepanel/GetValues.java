package com.dekist.radionodepanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetValues implements Serializable {
    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("rows")
    @Expose
    private Rows_Values[] rows_values;

    @SerializedName("status")
    @Expose
    private String status;

    public String getCount () {
        return count;
    }

    public void setCount (String count) {
        this.count = count;
    }

    public Rows_Values[] getRows () {
        return rows_values;
    }

    public void setRows (Rows_Values[] rows_values) {
        this.rows_values = rows_values;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[count = "+count+", rows = "+rows_values+", status = "+status+"]";
    }
}
