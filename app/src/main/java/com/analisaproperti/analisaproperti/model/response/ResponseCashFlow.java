package com.analisaproperti.analisaproperti.model.response;

import com.analisaproperti.analisaproperti.model.cashflow.CashFlow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCashFlow {

    @SerializedName("status")
    String status;
    @SerializedName("data")
    List<CashFlow> data;

    public ResponseCashFlow(String status, List<CashFlow> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<CashFlow> getData() {
        return data;
    }

}
