package com.example.rsyazilim.rs_ihbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


import com.example.rsyazilim.rs_ihbar.model.DamageRecordInfo;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button send_button;
    private Context context;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        context = this;

        RecyclerView recycler_view = (RecyclerView) findViewById(R.id.history_listview);

        AppDatabase database = AppDatabase.getAppDatabase(context);

        List<DamageRecordInfo> items = database.recordDao().getAll();//TODO uithreadden cikart

        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(new RecyclerViewAdapter(context , items));

        this.send_button = this.findViewById(R.id.history_btn);
        this.send_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(HistoryActivity.this , MainActivity.class));
        finish();
    }

    public void DatabaseOku()
    {

        AppDatabase database = AppDatabase.getAppDatabase(this);
        List<DamageRecordInfo> list = database.recordDao().getAll(); //dbdeki tum datayi oku

    }

}
