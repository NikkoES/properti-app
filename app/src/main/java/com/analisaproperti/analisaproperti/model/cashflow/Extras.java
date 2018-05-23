package com.analisaproperti.analisaproperti.model.cashflow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Extras implements Serializable {

    @SerializedName("id_cash_flow")
    String idCashFlow;
    @SerializedName("occupancy_rate")
    String occupancyRate;
    @SerializedName("total_penghasilan")
    String totalPenghasilan;
    @SerializedName("total_pemasukan")
    String totalPemasukan;
    @SerializedName("total_pengeluaran")
    String totalPengeluaran;
    @SerializedName("net_operating_income")
    String netOperatingIncome;
    @SerializedName("net_operating_income_future")
    String netOperatingIncomeFuture;

    public Extras(String idCashFlow, String occupancyRate, String totalPenghasilan, String totalPemasukan, String totalPengeluaran, String netOperatingIncome, String netOperatingIncomeFuture) {
        this.idCashFlow = idCashFlow;
        this.occupancyRate = occupancyRate;
        this.totalPenghasilan = totalPenghasilan;
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.netOperatingIncome = netOperatingIncome;
        this.netOperatingIncomeFuture = netOperatingIncomeFuture;
    }

    public String getIdCashFlow() {
        return idCashFlow;
    }

    public String getOccupancyRate() {
        return occupancyRate;
    }

    public String getTotalPenghasilan() {
        return totalPenghasilan;
    }

    public String getTotalPemasukan() {
        return totalPemasukan;
    }

    public String getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public String getNetOperatingIncome() {
        return netOperatingIncome;
    }

    public String getNetOperatingIncomeFuture() {
        return netOperatingIncomeFuture;
    }
}
