package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cashflow.Kamar;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseKamar {

    @SerializedName("status")
    String status;
    @SerializedName("kamar")
    List<Kamar> data;

    public ResponseKamar(String status, List<Kamar> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<Kamar> getData() {
        return data;
    }

}
