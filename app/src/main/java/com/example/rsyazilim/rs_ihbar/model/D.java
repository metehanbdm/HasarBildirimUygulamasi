
package com.example.rsyazilim.rs_ihbar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class D {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;

    /**
     * No args constructor for use in serialization
     * 
     */
    public D() {
    }

    /**
     * 
     * @param msg
     * @param success
     */
    public D(String success, String msg) {
        super();
        this.success = success;
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
