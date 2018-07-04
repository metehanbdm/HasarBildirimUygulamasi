package com.example.rsyazilim.rs_ihbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rsyazilim.rs_ihbar.model.LoginResponse;
import com.example.rsyazilim.rs_ihbar.model.RegisterResponse;
import com.example.rsyazilim.rs_ihbar.network.ApiUtils;
import com.example.rsyazilim.rs_ihbar.network.LoginService;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String URL_REGISTER = "http://cheetatech.me/register_tutorial.php";

    private Button btnRegister;
    private EditText editText_namesurname , editText_username , editText_password , editText_passwordagain;

    LoginService loginService ;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.register_kayitTamamla_btn);
        editText_namesurname = findViewById(R.id.register_isim_edtText);
        editText_username = findViewById(R.id.register_kullaniciAdi_edtText);
        editText_password = findViewById(R.id.register_parola_edtText);
        editText_passwordagain = findViewById(R.id.register_parolaTekrar_edtText);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        loginService = ApiUtils.loginSystem();

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name = editText_namesurname.getText().toString().trim();
                String nickname = editText_username.getText().toString().trim();
                String password = editText_password.getText().toString().trim();
                String again_password = editText_passwordagain.getText().toString().trim();


                if (!name.isEmpty() && !nickname.isEmpty() && !password.isEmpty() && !again_password.isEmpty())
                {
                    if (password.equals(again_password))
                    {
                        registerUser(name, nickname, password);
//                    save2SharedPreferences(name,nickname,password,again_password);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void registerUser(final String name, final String nickname, final String password)
    {
        showDialog();
        Callback<RegisterResponse> listener = new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response)
            {
                RegisterResponse registerResponse = response.body();
                if (registerResponse !=null) //Todo dataya gore kontrol koyulacak
                {
                    //Toast mesaj yazilabilir
                    startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
                    hideDialog();
                    finish();
                }


            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t)
            {
                Toast.makeText(RegisterActivity.this , t.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };

        loginService.Register(name, nickname , password).enqueue(listener);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void save2SharedPreferences(final String name, final String nickname, final String password, final String again_password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("userName", name);
        editor.putString("userNickname", nickname);
        editor.putString("userPassword", password);
        editor.putString("userAgain_Password", again_password);


        editor.commit();
    }
}
