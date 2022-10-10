package com.kd.appelements.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.kd.appelements.BR;
import com.kd.appelements.R;
import com.kd.appelements.add.Elements;
import com.kd.appelements.add.SiteSets;
import com.kd.appelements.databinding.ActivitySixT256x128Binding;
import com.kd.appelements.datamodel.LiveDataBus;
import com.kd.appelements.fragments.ElementFragment;
import com.kd.appelements.service.ElementsService;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    ActivitySixT256x128Binding binding;
    ElementFragment elementFragment;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestPermissions();
        initDataBinding();
        //initView();
        inintFragments();
        initSizeAndTypeFace();

        setAllObserve();
        startService();
    }

    private void initSizeAndTypeFace() {
        if(elementFragment!=null){
            elementFragment.setSizeAndType(SiteSets.getSiteTextSet().midSize,fontFace);
        }

        binding.setTypeFace(fontFace);
        binding.setVariable(BR.textSize,SiteSets.getSiteTextSet().topSize);

    }

    private void initDataBinding() {
        timer = new Timer();
        fontFace = Typeface.createFromAsset(getAssets(), "fonts/simsun.ttc");//HYQiHei-25JF.ttf//simfang.ttf//PixelMplus10-Regular
        binding = DataBindingUtil.setContentView(this, SiteSets.getSiteTextSet().layoutId);
        binding.setTypeFace(fontFace);
    }

    public static Typeface fontFace;

    private void inintFragments() {
       elementFragment = new ElementFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_fragment, elementFragment).commit();
    }




    @Override
    protected void onDestroy() {
        stopService();
        if(timer!=null){
            timer.cancel();
        }
        super.onDestroy();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED}, 1);
        }
    }

    private void startService() {
        Intent intent = new Intent(this, ElementsService.class);
        startService(intent);
    }
    private void stopService(){
        Intent intent = new Intent(this, ElementsService.class);
        stopService(intent);
    }
    private void setAllObserve() {
        LiveDataBus.getInstance().getElementsMutableLiveData().observe(this, new Observer<Elements>() {
            @Override
            public void onChanged(Elements elements) {
                //处理六要素
                Log.i("MainActivity", "处理六要素------------");
                Log.i("MainActivity", "时间："+elements.getYear());
                binding.setElements(elements);
                binding.setLeftText(SiteSets.getSiteTextSet().getLeftText(elements));
                binding.setRightText(SiteSets.getSiteTextSet().getRightText(elements));
            }
        });
    }
}