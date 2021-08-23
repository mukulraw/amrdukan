package com.mrtecks.amrdukan.cartPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class cartBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("items")
    @Expose
    private String items;
    @SerializedName("delcharges")
    @Expose
    private String delcharges;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("packing")
    @Expose
    private String packing;
    @SerializedName("auto_cancel")
    @Expose
    private String auto_cancel;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getDelcharges() {
        return delcharges;
    }

    public void setDelcharges(String delcharges) {
        this.delcharges = delcharges;
    }

    public String getPacking() {
        return packing;
    }

    public String getTax() {
        return tax;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getAuto_cancel() {
        return auto_cancel;
    }

    public void setAuto_cancel(String auto_cancel) {
        this.auto_cancel = auto_cancel;
    }
}
