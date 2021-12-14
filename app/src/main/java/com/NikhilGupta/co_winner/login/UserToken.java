package com.NikhilGupta.co_winner.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserToken {

    @Expose
    @SerializedName("token")
    String token;

    public UserToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
