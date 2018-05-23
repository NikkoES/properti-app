package com.analisaproperti.analisaproperti.model.cashflow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CashFlow implements Serializable {

    @SerializedName("id_cash_flow")
    String idCashFlow;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("tanggal")
    String tanggal;
    @SerializedName("idUser")
    String idUser;

    public CashFlow(String idCashFlow, String keterangan, String tanggal, String idUser) {
        this.idCashFlow = idCashFlow;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.idUser = idUser;
    }

    public String getIdCashFlow() {
        return idCashFlow;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getIdUser() {
        return idUser;
    }
}
