package com.analisaproperti.analisaproperti.model.cicilan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cicilan implements Serializable {
    @SerializedName("id_cicilan")
    String idCicilan;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("tanggal")
    String tanggal;
    @SerializedName("pinjaman_kpr")
    String pinjamanKpr;
    @SerializedName("bunga_per_tahun")
    String bungaPerTahun;
    @SerializedName("tenor_lama_pinjaman")
    String tenorLamaPinjaman;
    @SerializedName("tenor_bunga_fix")
    String tenorBungaFix;
    @SerializedName("sisa_pokok_pinjaman")
    String sisaPokokPinjaman;
    @SerializedName("bunga_floating_per_tahun")
    String bungaFloatingPerTahun;
    @SerializedName("cicilan")
    String cicilan;
    @SerializedName("cicilan_setelah_floating")
    String cicilanSetelahFloating;

    public Cicilan(String idCicilan, String keterangan, String tanggal, String pinjamanKpr, String bungaPerTahun, String tenorLamaPinjaman, String tenorBungaFix, String sisaPokokPinjaman, String bungaFloatingPerTahun, String cicilan, String cicilanSetelahFloating) {
        this.idCicilan = idCicilan;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.pinjamanKpr = pinjamanKpr;
        this.bungaPerTahun = bungaPerTahun;
        this.tenorLamaPinjaman = tenorLamaPinjaman;
        this.tenorBungaFix = tenorBungaFix;
        this.sisaPokokPinjaman = sisaPokokPinjaman;
        this.bungaFloatingPerTahun = bungaFloatingPerTahun;
        this.cicilan = cicilan;
        this.cicilanSetelahFloating = cicilanSetelahFloating;
    }

    public String getIdCicilan() {
        return idCicilan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getPinjamanKpr() {
        return pinjamanKpr;
    }

    public String getBungaPerTahun() {
        return bungaPerTahun;
    }

    public String getTenorLamaPinjaman() {
        return tenorLamaPinjaman;
    }

    public String getTenorBungaFix() {
        return tenorBungaFix;
    }

    public String getSisaPokokPinjaman() {
        return sisaPokokPinjaman;
    }

    public String getBungaFloatingPerTahun() {
        return bungaFloatingPerTahun;
    }

    public String getCicilan() {
        return cicilan;
    }

    public String getCicilanSetelahFloating() {
        return cicilanSetelahFloating;
    }
}