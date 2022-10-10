package com.kd.appelements.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.kd.appelements.BR;
import com.kd.appelements.R;
import com.kd.appelements.add.SiteSets;

//import com.xixun.add.ElementActivity;
public class ElementFragment extends Fragment {
    ViewDataBinding inflate;
    int size;
    Typeface typeface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("TAG_", "ElementFragment_onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG_", "ElementFragment_onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("TAG_", "ElementFragment_onCreateView");
        inflate = DataBindingUtil.inflate(inflater, R.layout.element_fragment, container, false);
        if (size > 0) {
            inflate.setVariable(BR.textSize, size);
        }
        if (typeface != null) {
            inflate.setVariable(BR.typeFace, typeface);
        }
        inflate.setVariable(BR.orientation, SiteSets.getSiteTextSet().orientation);
        inflate.setVariable(BR.rightText, SiteSets.getSiteTextSet().FourRightText);
        inflate.setVariable(BR.leftText, SiteSets.getSiteTextSet().FourLeftText);
        return inflate.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("TAG_", "ElementFragment_onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("TAG_", "ElementFragment_onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG_", "ElementFragment_onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TAG_", "ElementFragment_onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("TAG_", "ElementFragment_onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("TAG_", "ElementFragment_onDestroyView");
    }

    @Override
    public void onDestroy() {
        Log.i("TAG_", "ElementFragment_onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("TAG_", "ElementFragment_onDetach");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void updateText(String leftText, String rightText) {
        Log.i("TAG_", "updateText:" + leftText + ";" + rightText);
        if (inflate==null)return;
        if (!TextUtils.isEmpty(leftText)) {
            inflate.setVariable(BR.leftText, leftText);
            // inflate.setVariable(BR.rightText,"风速:/m/s\\n风向:/\\n气压:/hPa");
        }
        if (!TextUtils.isEmpty(rightText)) {
            //  inflate.setVariable(BR.leftText,"温度:/℃\n湿度:/%\n雨量:/mm");
            inflate.setVariable(BR.rightText, rightText);
        }
    }

    public void setSizeAndType(int size, Typeface typeFace) {
        if (size > 0) {
            this.size = size;
        }
        if (typeFace != null) {
            this.typeface = typeFace;
        }
    }

    public void setDefultText(String right, String lift) {

    }

}
