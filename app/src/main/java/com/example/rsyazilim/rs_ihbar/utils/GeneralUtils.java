package com.example.rsyazilim.rs_ihbar.utils;

import android.support.design.widget.Snackbar;
import android.view.View;


public class GeneralUtils {

    public static void ShowSnackbar(View view , String message)
    {
        Snackbar.make(view, message , Snackbar.LENGTH_LONG).show();
    }
}
