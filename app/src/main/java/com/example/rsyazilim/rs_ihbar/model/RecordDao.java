package com.example.rsyazilim.rs_ihbar.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

//db islemlerini burada annotaionlarla tanimliyosun hic kod yazmadan

@Dao
public interface RecordDao
{
    @Query("SELECT * FROM records")
    List<DamageRecordInfo> getAll();

    @Query("SELECT * FROM records where phone LIKE  :phone")
    DamageRecordInfo findByPhone(String phone);

    @Query("SELECT COUNT(*) from records")
    int countRecords();

    @Insert
    void insertAll(DamageRecordInfo... records);

    @Delete
    void delete(DamageRecordInfo record);
}
