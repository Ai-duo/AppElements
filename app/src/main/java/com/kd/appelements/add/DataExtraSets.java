package com.kd.appelements.add;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import androidx.databinding.BindingAdapter;


import com.kd.appelements.R;
import com.kd.appelements.activity.MainActivity;
import com.kd.appelements.fragments.ElementFragment;
import com.kd.appelements.fragments.PMFragment;

import java.util.Timer;
import java.util.TimerTask;

public class DataExtraSets {
    @BindingAdapter("setOrientation")
    public static void setOrientation(LinearLayout layout,int orientation){
        switch (orientation){
            case 0 :
                Log.i("TAG","HORIZONTAL");
                layout.setOrientation(LinearLayout.HORIZONTAL);
                break;
            case 1:
                Log.i("TAG","VERTICAL");
                layout.setOrientation(LinearLayout.VERTICAL);
                break;
        }
    }
    @BindingAdapter("setExtraTypeface")
    public static void setExtraTypeface(TextView textView,Typeface typeFace){
        Log.i("TAG","setExtraTypeface");
        if(textView!=null){
            textView.setTypeface(typeFace);
            textView.getPaint().setAntiAlias(false);
        }
    };
    @BindingAdapter("setExtraTextSize")
    public static void setExtraTextSize(TextView textView,int size){
        Log.i("TAG","setExtraTypeface");
        if(size>0){
           textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) size);
        }
    };
    @BindingAdapter("setMarqueeViewText")
    public static void setMarqueeViewText(MarqueeView view,String text){

        if(view!=null){
            view.getPaint().setTypeface(MainActivity.fontFace);
            view.getPaint().setAntiAlias(false);
            if(!SiteSets.getSiteTextSet().isShowWea){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
                view.setContent(text);
                Log.i("TAG","设置滚动文字成功");
            }

        }
    }
    private static int index = 0;

    @BindingAdapter({"context","timer","elementFragment","pmFragment"})
    public static void changeFragment(LinearLayout layout, final MainActivity cxt, Timer timer, final ElementFragment elementFragment, PMFragment pmFragment) {
        Log.i("TAG","changeFragment");
        if(elementFragment==null){
            return;
        }
        else if(pmFragment==null){
            FragmentManager manager  = cxt.getFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.content_fragment,elementFragment).commitAllowingStateLoss();
        }
        else if(timer != null) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (index % 2 == 0) {
                        FragmentManager manager  = cxt.getFragmentManager();
                        FragmentTransaction fragmentTransaction = manager.beginTransaction();
                       /* if(!elementFragment.isAdded()){
                            fragmentTransaction.add(R.id.content_fragment,elementFragment);
                        }else{
                            fragmentTransaction.hide(elementFragment).show(pmFragment);
                        }*/
                       fragmentTransaction.replace(R.id.content_fragment,elementFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    } else {
                        FragmentManager manager  = cxt.getFragmentManager();
                        FragmentTransaction fragmentTransaction = manager.beginTransaction();
                      /*  if(!pmFragment.isAdded()){
                            fragmentTransaction.add(R.id.content_fragment,pmFragment);
                        }else{
                            fragmentTransaction.hide(pmFragment).show(elementFragment);
                        }*/
                        fragmentTransaction.replace(R.id.content_fragment,elementFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    index++;
                }
            }, 0, 10 * 1000);
        }
    }
}
