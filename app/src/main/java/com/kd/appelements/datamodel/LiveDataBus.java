package com.kd.appelements.datamodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.kd.appelements.add.AlarmInfo;
import com.kd.appelements.add.Elements;
import com.kd.appelements.add.SiteInfo;


public class LiveDataBus {
    private MutableLiveData<Elements> elementsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<SiteInfo> siteInfoMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AlarmInfo> alarmInfoMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> weaInfoMutableLiveData = new MutableLiveData<>();
    private static LiveDataBus liveDataBus;
    public static LiveDataBus getInstance(){
        if(liveDataBus==null){
            liveDataBus = new LiveDataBus();
        }
        return liveDataBus;
    }
    public MutableLiveData<Elements> getElementsMutableLiveData() {

        return elementsMutableLiveData;
    }



    public MutableLiveData<SiteInfo> getSiteInfoMutableLiveData() {
        return siteInfoMutableLiveData;
    }



    public MutableLiveData<AlarmInfo> getAlarmInfoMutableLiveData() {
        return alarmInfoMutableLiveData;
    }



    public Elements getElements() {
        return elementsMutableLiveData.getValue();
    }

    public void setElements(Elements elements) {
        Log.i("TAG","发送：elements");
       // elementsMutableLiveData.setValue(elements);
        elementsMutableLiveData.postValue(elements);
    }

    public SiteInfo getSiteInfo() {
        return siteInfoMutableLiveData.getValue();
    }

    public void setSiteInfo(SiteInfo siteInfo) {
        siteInfoMutableLiveData.postValue(siteInfo);
    }

    public AlarmInfo getAlarmInfo() {
        return alarmInfoMutableLiveData.getValue();
    }

    public void setAlarmInfo(AlarmInfo alarmInfo) {
        this.alarmInfoMutableLiveData.postValue(alarmInfo);
    }

    public MutableLiveData<String> getWeaInfoMutableLiveData() {
        return weaInfoMutableLiveData;
    }
    public String getWeaInfo() {
        return weaInfoMutableLiveData.getValue();
    }
    public void setWeaInfo(String weaInfo) {
        this.weaInfoMutableLiveData.postValue(weaInfo) ;
    }
}
