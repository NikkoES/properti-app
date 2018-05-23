package com.analisaproperti.analisaproperti.model.cashflow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pemasukan implements Serializable {

    @SerializedName("id_cash_flow")
    String idCashFlow;
    @SerializedName("pemasukan")
    String keteranganPemasukan;
    @SerializedName("jumlah_pemasukan")
    String jumlahPemasukan;

    public Pemasukan(String idCashFlow, String keteranganPemasukan, String jumlahPemasukan) {
        this.idCashFlow = idCashFlow;
        this.keteranganPemasukan = keteranganPemasukan;
        this.jumlahPemasukan = jumlahPemasukan;
    }

    public String getIdCashFlow() {
        return idCashFlow;
    }

    public String getKeteranganPemasukan() {
        return keteranganPemasukan;
    }

    public String getJumlahPemasukan() {
        return jumlahPemasukan;
    }
}
