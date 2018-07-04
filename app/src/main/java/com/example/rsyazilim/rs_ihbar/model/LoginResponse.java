
package com.example.rsyazilim.rs_ihbar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("post")
    @Expose
    private Object post;
    @SerializedName("d")
    @Expose
    private D d;

    /**
     * No args constructor for use in serialization
     * 
     */
    public LoginResponse() {
    }

    /**
     * 
     * @param d
     * @param post
     */
    public LoginResponse(Object post, D d) {
        super();
        this.post = post;
        this.d = d;
    }

    public Object getPost() {
        return post;
    }

    public void setPost(Object post) {
        this.post = post;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }

}
