package com.emz.pathfinder.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.emz.pathfinder.R;

public class Ui {
    public static void createSnackbar(View view, CharSequence charSequence){
        Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG).show();
    }
}
