package com.NikhilGupta.co_winner.login;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveTxnId {

    @Expose
    @SerializedName("txnId")
    String txnId;

    public SaveTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    /*private static SaveTxnId transactionId = new SaveTxnId();

    private SaveTxnId() {
    }

    public static SaveTxnId getInstance(){
        return transactionId;
    }

    public void addToRequestQueue(StringRequest postRequ̥est) {
        Log.d("Test", "Class SaveTxnId: >\n"+postRequest.toString()+" <");
    }*/
}
