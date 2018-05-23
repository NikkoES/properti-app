package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cashflow.Pengeluaran;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePengeluaran {

    @SerializedName("status")
    String status;
    @SerializedName("pengeluaran")
    List<Pengeluaran> data;

    public ResponsePengeluaran(String status, List<Pengeluaran> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<Pengeluaran> getData() {
        return data;
    }

}
