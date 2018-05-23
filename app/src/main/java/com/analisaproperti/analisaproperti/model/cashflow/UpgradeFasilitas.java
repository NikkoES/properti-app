package com.analisaproperti.analisaproperti.model.cashflow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpgradeFasilitas implements Serializable{

    @SerializedName("id_cash_flow")
    String idCashFlow;
    @SerializedName("nama_fasilitas")
    String namaFasilitas;
    @SerializedName("kenaikan_harga")
    String kenaikanHarga;
    @SerializedName("jumlah_kamar")
    String jumlahKamar;

    public UpgradeFasilitas(String idCashFlow, String namaFasilitas, String kenaikanHarga, String jumlahKamar) {
        this.idCashFlow = idCashFlow;
        this.namaFasilitas = namaFasilitas;
        this.kenaikanHarga = kenaikanHarga;
        this.jumlahKamar = jumlahKamar;
    }

    public String getIdCashFlow() {
        return idCashFlow;
    }

    public String getNamaFasilitas() {
        return namaFasilitas;
    }

    public String getKenaikanHarga() {
        return kenaikanHarga;
    }

    public String getJumlahKamar() {
        return jumlahKamar;
    }
}
