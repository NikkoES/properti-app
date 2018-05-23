package com.analisaproperti.analisaproperti.model.cashflow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kamar implements Serializable {

    @SerializedName("id_cash_flow")
    String idCashFlow;
    @SerializedName("tipe_kamar")
    String tipeKamar;
    @SerializedName("jumlah_kamar")
    String jumlahKamar;
    @SerializedName("harga_kamar")
    String hargaKamar;

    public Kamar(String idCashFlow, String tipeKamar, String jumlahKamar, String hargaKamar) {
        this.idCashFlow = idCashFlow;
        this.tipeKamar = tipeKamar;
        this.jumlahKamar = jumlahKamar;
        this.hargaKamar = hargaKamar;
    }

    public String getIdCashFlow() {
        return idCashFlow;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public String getJumlahKamar() {
        return jumlahKamar;
    }

    public String getHargaKamar() {
        return hargaKamar;
    }
}
