package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cashflow.Extras;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseExtras {

    @SerializedName("status")
    String status;
    @SerializedName("extras")
    List<Extras> data;

    public ResponseExtras(String status, List<Extras> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<Extras> getData() {
        return data;
    }

}
