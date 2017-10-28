package com.emz.pathfinder.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.emz.pathfinder.R;

public class Ui {
    private static ProgressDialog progressDialog;

    public static void createProgressDialog(Context context, int theme, String string) {
        progressDialog = new ProgressDialog(context, theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(string);
        progressDialog.show();
    }

    public static void createProgressDialog(Context context, String string) {
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(string);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static void createSnackbar(View view, String string){
        Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();
    }
}
