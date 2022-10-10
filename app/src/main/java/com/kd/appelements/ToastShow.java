package com.kd.appelements;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastShow {
    public static  void show(Context ctx,String info){
        Toast toast = Toast.makeText(ctx, info, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.LEFT|Gravity.TOP,0,0);
        toast.show();
    }
}
