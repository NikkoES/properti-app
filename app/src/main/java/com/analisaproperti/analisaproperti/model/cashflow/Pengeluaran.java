package com.analisaproperti.analisaproperti.model.cashflow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pengeluaran implements Serializable {

    @SerializedName("id_cash_flow")
    String idCashFlow;
    @SerializedName("pengeluaran")
    String keteranganPengeluaran;
    @SerializedName("jumlah_pengeluaran")
    String jumlahPengeluaran;

    public Pengeluaran(String idCashFlow, String keteranganPengeluaran, String jumlahPengeluaran) {
        this.idCashFlow = idCashFlow;
        this.keteranganPengeluaran = keteranganPengeluaran;
        this.jumlahPengeluaran = jumlahPengeluaran;
    }

    public String getIdCashFlow() {
        return idCashFlow;
    }

    public String getKeteranganPengeluaran() {
        return keteranganPengeluaran;
    }

    public String getJumlahPengeluaran() {
        return jumlahPengeluaran;
    }
}
