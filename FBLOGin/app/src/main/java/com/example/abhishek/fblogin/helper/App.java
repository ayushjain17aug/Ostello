package com.example.abhishek.fblogin.helper;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by Abhishek on 02-01-2017.
 */
public class App {

    public static void showProgressBar(ProgressBar progressBar) {
        if (progressBar.getVisibility() != View.VISIBLE)
            progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        if (progressBar.getVisibility() != View.GONE
                || progressBar.getVisibility() != View.INVISIBLE)
            progressBar.setVisibility(View.GONE);
    }
}
