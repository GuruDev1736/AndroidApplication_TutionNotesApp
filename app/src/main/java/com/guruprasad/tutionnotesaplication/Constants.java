package com.guruprasad.tutionnotesaplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.dmoral.toasty.Toasty;


public class Constants {
    public static ProgressDialog progress_dialog(Context context , String title , String message)
    {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public static Toasty success(Context context , String message)
    {
        Toasty.success(context, message, Toast.LENGTH_LONG, true).show();
        return null;
    }

    public static Toasty error(Context context , String message)
    {
        Toasty.error(context, message, Toast.LENGTH_LONG, true).show();
        return null;
    }

    public static Toasty info(Context context , String message)
    {
        Toasty.info(context, message, Toast.LENGTH_LONG, true).show();
        return null;
    }

    public static Toasty warning(Context context , String message)
    {
        Toasty.warning(context, message, Toast.LENGTH_LONG, true).show();
        return null;
    }

    public static Toasty normal(Context context , String message , Drawable icon)
    {
        Toasty.normal(context, message, Toast.LENGTH_LONG , icon).show();
        return null;
    }

//    public static MaterialAlertDialogBuilder dialog(Context context ,String title , String message)
//    {
//        new MaterialAlertDialogBuilder(context,R.style.RoundShapeTheme)
//                .setTitle(title)
//                .setIcon(R.drawable.logo_1)
//                .setMessage(message)
//                .setCancelable(false);
//        return null;
//    }



}
