package com.example.rsyazilim.rs_ihbar.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "records")
public class DamageRecordInfo
{
    @Ignore
    private boolean locationChecked ;
    @Ignore
    private boolean imageChecked ;
    @Ignore
    private boolean informationChecked;



    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "information")
    public String information;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lng")
    private double lng;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "image1")
    public String image1;
    @ColumnInfo(name = "image2")
    public String image2;
    @ColumnInfo(name = "image3")
    public String image3;
    @ColumnInfo(name = "image4")
    public String image4;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLocationChecked() {
        return locationChecked;
    }

    public void setLocationChecked(boolean locationChecked) {
        this.locationChecked = locationChecked;
    }

    public boolean isImageChecked() {
        return imageChecked;
    }

    public void setImageChecked(boolean imageChecked) {
        this.imageChecked = imageChecked;
    }

    public boolean isInformationChecked() {
        return informationChecked;
    }

    public void setInformationChecked(boolean informationChecked) {
        this.informationChecked = informationChecked;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    @Override
    public String toString() {
        return uid+" "+phone+" "+information;
    }
}
