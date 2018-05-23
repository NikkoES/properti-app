package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.nilaipasar.PropertiNilaiPasar;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePropertiNilaiPasar {

    @SerializedName("status")
    String status;
    @SerializedName("value_properti")
    List<PropertiNilaiPasar> data;

    public ResponsePropertiNilaiPasar(String status, List<PropertiNilaiPasar> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<PropertiNilaiPasar> getData() {
        return data;
    }
}
