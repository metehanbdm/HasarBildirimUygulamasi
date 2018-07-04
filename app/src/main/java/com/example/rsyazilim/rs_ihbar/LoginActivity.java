package com.example.rsyazilim.rs_ihbar;

import android.R.layout;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rsyazilim.rs_ihbar.model.LoginResponse;
import com.example.rsyazilim.rs_ihbar.network.ApiUtils;
import com.example.rsyazilim.rs_ihbar.network.LoginService;


import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {


    private Button btnLogin, btnLinkToRegister;
    private EditText editText_kullaniciadi , editText_sifre;
    private ProgressDialog pDialog;

    private LoginService loginService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.login_girisyap_btn);
        //btnLinkToRegister = (Button) findViewById(R.id.login_kayitol_btn);
        editText_kullaniciadi = findViewById(R.id.login_kullaniciAdi_editText);
        editText_sifre = findViewById(R.id.login_parola_editText);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading");
        pDialog.setCancelable(false);

        loginService = ApiUtils.loginSystem();


        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String kullaniciAdi = editText_kullaniciadi.getText().toString().trim();
                String parola = editText_sifre.getText().toString().trim();


                if (!kullaniciAdi.isEmpty() && !parola.isEmpty()) {

                    LoginUser(kullaniciAdi, parola);
                } else {

                    Toast.makeText(getApplicationContext(), "E-Mail Adresi veya Şifre Hatalı!!", Toast.LENGTH_LONG).show();
                }
            }

        });


 //       btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

 //           public void onClick(View view) {
 //               Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
 //               startActivity(i);
 //               finish();
  //          }
  //      });


//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//        String nameFromPref = preferences.getString("userName", "N/A");
//        String emailFromPref = preferences.getString("userEmail", "N/A");
//        String passwordFromPref = preferences.getString("userPassword", "N/A");


//        if(!nameFromPref.equals("N/A") && !emailFromPref.equals("N/A") && !passwordFromPref.equals("N/A")){
//
//            LoginUser(emailFromPref, passwordFromPref);
//        }
    }

    private void LoginUser(final String kullaniciAdi, final String parola)
    {
        showDialog();
        Callback<LoginResponse> listener = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response)
            {
                LoginResponse loginResponse = response.body();
                if (loginResponse.getD().getSuccess().equalsIgnoreCase("false"))
                {
                    Toast.makeText(LoginActivity.this, "Giriş Başarılı.", Toast.LENGTH_SHORT).show();
                    ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "Loading. Please wait...", true);
                    startActivity(new Intent(LoginActivity.this , MainActivity.class));
                    //hideDialog();
                    finish();
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t)
            {
                Toast.makeText(LoginActivity.this , t.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };

        loginService.Login(kullaniciAdi , parola).enqueue(listener);
        }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}