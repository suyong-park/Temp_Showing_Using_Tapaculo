package com.dekist.radionodepanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetLst implements Serializable {
    @SerializedName("search") // GSON Annotation. JSON으로 serialize될 때 매칭하는 이름을 명시적으로 지정함. 이 때 문자열 값은 실제 JSON 속성값이어야 함.
    @Expose // object 중 해당 값이 null 인 경우, 자동으로 해당 object는 무시하고 JSON을 만들게 됨.
    private String search;

    @SerializedName("rows")
    @Expose
    private Rows[] rows;

    @SerializedName("status")
    @Expose
    private String status;

    public String getSearch () {
        return search;
    }

    public void setSearch (String search) {
        this.search = search;
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
        return "[search = "+search+", rows = "+rows+", status = "+status+"]";
    }
}
