package com.kd.appelements.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.kd.appelements.BR;
import com.kd.appelements.R;


public class PMFragment extends Fragment {
    ViewDataBinding inflate;
    int size;
    Typeface typeface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("TAG_", "PMFragment_onCreateView");
        inflate = DataBindingUtil.inflate(inflater, R.layout.pm_fragment, container, false);
        if(size>0){
            inflate.setVariable(BR.textSize,size);
        }
        if(typeface!=null){
            inflate.setVariable(BR.typeFace,typeface);
        }
        inflate.setVariable(BR.pm,"PM2.5: --");

        return inflate.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //pm_text.setText(MainActivity.pm_text);
        super.onHiddenChanged(hidden);
    }

    public void updateText(String pm_text) {
        inflate.setVariable(BR.pm,"PM2.5:"+pm_text);
    }
    public void setSizeAndType(int size,Typeface typeFace){
        if(size>0){
            this.size = size;
        }
        if(typeFace!=null){
            this.typeface = typeFace;
        }
    }
}
