package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cicilan.Cicilan;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCicilan {

    @SerializedName("status")
    String status;
    @SerializedName("data")
    List<Cicilan> data;

    public ResponseCicilan(String status, List<Cicilan> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<Cicilan> getData() {
        return data;
    }

}
