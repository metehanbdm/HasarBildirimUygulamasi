package com.example.rsyazilim.rs_ihbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        this.send_button = this.findViewById(R.id.info_btn);
        this.send_button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v)
    {
        SendToInformation();
    }

    public void SendToInformation() // Gönder butonu için..
    {
        Intent intent = getIntent();
        String info = ((TextView)findViewById(R.id.info_entry_editText)).getText().toString();
        String phone = ((TextView)findViewById(R.id.info_phone_editText)).getText().toString();
        intent.putExtra("INFO", info);
        intent.putExtra("PHONE", phone);

        setResult(RESULT_OK, intent);
        finish();

    }


}
