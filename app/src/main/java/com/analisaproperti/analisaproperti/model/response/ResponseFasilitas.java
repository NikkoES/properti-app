package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cashflow.UpgradeFasilitas;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFasilitas {

    @SerializedName("status")
    String status;
    @SerializedName("fasilitas")
    List<UpgradeFasilitas> data;

    public ResponseFasilitas(String status, List<UpgradeFasilitas> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<UpgradeFasilitas> getData() {
        return data;
    }

}
