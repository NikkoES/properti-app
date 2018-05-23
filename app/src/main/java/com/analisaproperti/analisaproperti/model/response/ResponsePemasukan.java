package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cashflow.Pemasukan;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePemasukan {

    @SerializedName("status")
    String status;
    @SerializedName("pemasukan")
    List<Pemasukan> data;

    public ResponsePemasukan(String status, List<Pemasukan> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<Pemasukan> getData() {
        return data;
    }

}
