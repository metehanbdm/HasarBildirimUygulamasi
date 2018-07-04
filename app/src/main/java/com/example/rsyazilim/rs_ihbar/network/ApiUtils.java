package com.example.rsyazilim.rs_ihbar.network;


import com.example.rsyazilim.rs_ihbar.utils.Variables;



public class ApiUtils {

    public static LoginService loginSystem()
    {
        return RetrofitBuilder.getClient(Variables.RSSERVIS_BASE_URL).create(LoginService.class);
    }
}
