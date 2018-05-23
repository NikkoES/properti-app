package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.nilaipasar.NilaiPasar;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseNilaiPasar {

    @SerializedName("status")
    String status;
    @SerializedName("data")
    List<NilaiPasar> data;

    public ResponseNilaiPasar(String status, List<NilaiPasar> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<NilaiPasar> getData() {
        return data;
    }

}
