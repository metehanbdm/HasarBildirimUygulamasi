package com.example.rsyazilim.rs_ihbar.utils;

import android.content.Context;

import com.example.rsyazilim.rs_ihbar.AppDatabase;
import com.example.rsyazilim.rs_ihbar.model.DamageRecordInfo;

import java.util.Calendar;

public class DatabaseUtils {

    public static void Add(Context context , DamageRecordInfo damageRecordInfo)
    {
            DamageRecordInfo record = new DamageRecordInfo();
            record.setInformation(damageRecordInfo.getInformation());
            record.setLat(damageRecordInfo.getLat());
            record.setLng(damageRecordInfo.getLng());
            record.setPhone(damageRecordInfo.getPhone());
            record.setImage1(damageRecordInfo.getImage1());
            record.setImage2(damageRecordInfo.getImage2());
            record.setImage3(damageRecordInfo.getImage3());
            record.setImage4(damageRecordInfo.getImage4());

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String dateStr = df.format("dd MMM yy", Calendar.getInstance().getTime()).toString();
            record.setDate(dateStr);

            AppDatabase.getAppDatabase(context).recordDao().insertAll(record); // database ekleme


        }
}
