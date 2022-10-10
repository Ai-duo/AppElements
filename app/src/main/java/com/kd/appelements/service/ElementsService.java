package com.kd.appelements.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.kd.appelements.ToastShow;
import com.kd.appelements.add.Elements;
import com.kd.appelements.datamodel.LiveDataBus;
import com.kd.appelements.tools.UrlList;
import com.xixun.joey.aidlset.CardService;
import com.xixun.joey.uart.BytesData;
import com.xixun.joey.uart.IUartListener;
import com.xixun.joey.uart.IUartService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ElementsService extends Service {
    public IUartService uart;
    volatile StringBuffer builder = new StringBuffer();
    boolean start = false;
     volatile  boolean dmgd = false, weaf = false, timing = false, bright = false;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            uart = IUartService.Stub.asInterface(iBinder);
            Log.i("TAG_uart", "================ onServiceConnected ====================");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("TAG_uart", "================== onServiceDisconnected ====================");
            uart = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG_Service", "服务开启");
        weaTimer();
        bindCardSystemUartAidl();
        startGetUart();
      // final String in = "DMGD F6144 2022-10-09 10:26 111111110000110000000000000000000000000000000 0000000008 10 0 318 0 353 0 0 0 286";
       // getFourElements(in);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("TAG_uart", "正在获取uart======================");
                } while (null == uart);
                try {
                    uart.write(port,new byte[]{0x44,0x4d,0x47,0x44,0x0d,0x0a});
                   // uart.write(port,"DMGD\r\n".getBytes());
                    try {
                        Thread.sleep(10*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getFourElements(builder.toString());
                    dmgd = false;
                    weaf = false;
                    timing = false;
                    bright = false;
                    start = false;
                    builder.delete(0, builder.length());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2*60*1000);
    }

    Thread thread;
    private boolean openPm = false;

    private void startGetUart() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("TAG_uart", "正在获取uart======================");
                } while (null == uart);
                try {
                    //监听/dev/ttyMT2，获取数据/dev/s3c2410_serial3
                    uart.read(port, new IUartListener.Stub() {
                        @Override
                        public void onReceive(BytesData data) throws RemoteException {
                            Log.i("TAG_uart", "========获取到串口数据===========");

                                for (byte a : data.getData()) {
                                    String s1 = "0x" + Integer.toHexString(a & 0xFF) + " ";
                                    char ss = (char) a;
                                    Log.i("TAG_uart", "ss:" + ss + ";s1:" + s1);
                                    if (ss == 'D') {
                                        dmgd = true;
                                        start = true;
                                        builder.append(ss);
                                    } else if (ss == 'W') {
                                        weaf = true;
                                        start = true;
                                        builder.append(ss);
                                    } else if (ss == 'O') {
                                        timing = true;
                                        start = true;
                                        builder.append(ss);
                                    } else if (ss == 'B') {
                                        bright = true;
                                        start = true;
                                        builder.append(ss);
                                    } else if (start) {
                                        start = true;
                                        builder.append(ss);
                                    }
                                    Log.i("TAG_uart123", builder.toString());
                                    if (builder.length() == 1) {
                                        //||!builder.toString().equals("FE")
                                        if (!builder.toString().equals("D") ) {
                                            builder.delete(0, builder.length());
                                            dmgd = false;
                                            weaf = false;
                                            timing = false;
                                            bright = false;
                                            start = false;
                                        }
                                    }
                                    if (builder.length() == 2) {
                                        //||!builder.toString().equals("FE")
                                        if (!builder.toString().equals("DM") ) {
                                            builder.delete(0, builder.length());
                                            dmgd = false;
                                            weaf = false;
                                            timing = false;
                                            bright = false;
                                            start = false;
                                        }
                                    }
                                    if (builder.length() == 4) {
                                        //||!builder.toString().equals("FE")
                                        if (!builder.toString().equals("DMGD") ) {
                                            builder.delete(0, builder.length());
                                            dmgd = false;
                                            weaf = false;
                                            timing = false;
                                            bright = false;
                                            start = false;
                                        }
                                    }
                                }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    Timer weaTimer;

    private void weaTimer() {
        weaTimer = new Timer();
        weaTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                String result = null;
                Request request = new Request.Builder()
                        .url(UrlList.dayurl)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        result = response.body().string();
                        JSONObject json = new JSONObject(result);
                        String Info = json.optString("DATE");
                        String dayinfo = json.optString("wea_txt1");
                        // site_name = json.optString("wea_logo");
                        if (!TextUtils.isEmpty(Info)) {
                            EventBus.getDefault().post(dayinfo);
                            LiveDataBus.getInstance().setWeaInfo(dayinfo);
                            try {
                                Thread.sleep(60 * 60 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.i("TAG", "获取天气数据失败");
                    }
                } catch (Exception e) {
                    // Log.i("TAG", "网络异常");
                }

            }
        }, 0, 3000);
    }

    String s2 = "";
    ArrayList<Byte> byteArrayList = new ArrayList<>();
    static int ii = 0;
    boolean PmFlag = false;

    public void bindCardSystemUartAidl() {
        Intent intent = new Intent("xixun.intent.action.UART_SERVICE");
        intent.setPackage("com.xixun.joey.cardsystem");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    ArrayList<Integer> index = new ArrayList<Integer>();

    public int getCharCount(String chars) {
        if (TextUtils.isEmpty(chars)) {
            return 0;
        }
        index.clear();
        char[] chars1 = chars.toCharArray();
        int count = 0;
        for (int j = 0; j < chars1.length; j++) {
            if (chars1[j] == '1') {
                count++;
            }
            if (j == 0 || j == 1 || j == 6 || j == 12 ) {
                index.add(count - 1);
            }
        }
        return count;
    }

    //风向
    String fx = "--";//1
    //风速
    String fs = "--";//2
    //降水
    String js = "--";//13
    //温度
    String wd = "--";
    //日最大温度
    String max_wd = "--";
    //日最小温度
    String min_wd = "--";
    //湿度
    String sd = "--";//21
    //日最小湿度
    String min_sd = "--";
    //气压
    String qy = "--";//26
    //能见度
    String njd = "--";//26
    static String WEA;
    String port = "/dev/ttysWK2";//,/dev/ttysWK2   /dev/ttyMT3
    //判断是否重启,每十分钟判断一次sendCount与currentCount的值，如果两者相等就重起；
    int sendCount = 0, currentCount = -1;

    public void getFourElements(String info) {
        //builder.delete(0, builder.length());
        //开始接受PM2.5数据
        Log.i("TAG", "getElements:");
        if (info.startsWith("DMGD")) {

            String[] infoss = info.split(" ");
            for (int i = 0; i < infoss.length; i++) {
                Log.i("TAG_uart", i + ":" + infoss[i]);
            }
            //日期
            String date = infoss[2];
            Log.i("TAG", "日期:" + date);
            //时间
            String time = infoss[3];
            Log.i("TAG", "时间:" + time);
            int count = getCharCount(infoss[4]);
            int qc = 5;
            if (infoss[5].length() >= 4) {
                qc = 6;
            }
            try {
                if (infoss[4].charAt(0) == '1') {
                    String fx1 = infoss[qc + index.get(0)];
                    Log.i("TAG", "风向:" + fx1);
                    if (isNum(fx1)) {
                        float f = Float.valueOf(fx1);
                        if ((f >= 0 && f < 12.25) || (f > 348.76 && f <= 360)) {
                            fx = "北";
                        } else if (f > 12.26 && f < 33.75) {//22.5
                            fx = "北偏东北";
                        } else if (f > 33.76 && f < 56.25) {
                            fx = "东北";
                        } else if (f > 56.25 && f < 78.75) {
                            fx = "东偏东北";
                        } else if (f > 78.75 && f < 101.25) {
                            fx = "东";
                        } else if (f > 101.25 && f < 123.75) {
                            fx = "东偏东南";
                        } else if (f > 123.76 && f < 146.25) {
                            fx = "东南";
                        } else if (f > 146.26 && f < 168.75) {
                            fx = "南偏东南";
                        } else if (f > 168.75 && f < 191.25) {
                            fx = "南";
                        } else if (f > 191.25 && f < 213.75) {
                            fx = "南偏西南";
                        } else if (f > 213.75 && f < 236.25) {
                            fx = "西南";
                        } else if (f > 236.25 && f < 258.75) {
                            fx = "西偏西南";
                        } else if (f > 258.75 && f < 281.25) {
                            fx = "西";
                        } else if (f > 281.25 && f < 303.75) {
                            fx = "西偏西北";
                        } else if (f > 303.75 && f < 326.25) {
                            fx = "西北";
                        } else if (f > 326.25 && f < 348.75) {
                            fx = "北偏西北";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析风向时出错");
            }
            try {
                if (infoss[4].charAt(1) == '1') {
                    String fs1 = infoss[qc + index.get(1)];
                    Log.i("TAG", "风速:" + fs1);
                    if (!isNum(fs1)) {
                        //fs = "";
                    } else {
                        fs = Float.parseFloat(infoss[qc + index.get(1)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析风速时出错");
            }

            try {
                if (infoss[4].charAt(7) == '1') {
                    String js1 = infoss[qc + index.get(2)];
                    Log.i("TAG", "降水:" + js1);
                    if (!isNum(js1)) {
                        // js = "";
                    } else {
                        js = Float.parseFloat(infoss[qc + index.get(2)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析雨量时出错");
            }
            String wd1 = "";
            try {

                if (infoss[4].charAt(12) == '1') {
                    wd1 = effectiveLen(infoss[qc + index.get(3)]);
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {

                            wd1 = wd1.substring(1);
                            int l = getLen(wd1);
                            wd1 = wd1.substring(0,l);
                            if (isNum(wd1)) {
                                wd = "-" + Float.parseFloat(wd1) / 10;
                            }

                    } else {
                        if(wd1.length()>3){
                            wd1 = wd1.substring(0,4);
                        }
                        int l = getLen(wd1);
                        wd1 = wd1.substring(0,l);
                        if (isNum(wd1)) {
                            wd = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                    Log.i("TAG", "温度:" + wd);
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析温度时出错");
                wd = wd1;
            }

            LiveDataBus.getInstance().setElements(new Elements("语溪小学气象站", date, time, wd, max_wd, min_wd, sd, min_sd, fx, fs, js, qy, njd));
            sendCount++;
        }

    }
    public String  effectiveLen(String s){

        return s.replaceAll("D","").replaceAll("M","")
                .replaceAll("G","").replaceAll(" ","")
                .replaceAll("\n","");
    }
    public int getLen(String len){
        int l = 0;
        for(int i=0;i<len.length();i++){
            if(!isNum(len.charAt(i)+"")){
                return i;
            }else{
                l = i;
            }
        }
        return l;
    }

    public void getElements(String info) {
        //builder.delete(0, builder.length());
        //开始接受PM2.5数据
        Log.i("TAG", "getElements:");
        if (openPm) {
            PmFlag = true;
            try {
                uart.write(port, new byte[]{0x15, 0x03, 0X00, 0X64, 0X00, 0x08, 0x06, (byte) 0xc7});
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(info)) {
            return;
        }
        if (info.startsWith("DMGD") && (info.endsWith("F") || info.endsWith("T"))) {

            String[] infoss = info.split(" ");
            for (int i = 0; i < infoss.length; i++) {
                Log.i("TAG_uart", i + ":" + infoss[i]);
            }
            //日期
            String date = infoss[2];
            Log.i("TAG", "日期:" + date);
            //时间
            String time = infoss[3];
            Log.i("TAG", "时间:" + time);
            int count = getCharCount(infoss[4]);
            int qc = 5;
            if (infoss[5].length() >= 4) {
                qc = 6;
            }
            try {
                if (infoss[4].charAt(0) == '1') {
                    String fx1 = infoss[qc + index.get(0)];
                    Log.i("TAG", "风向:" + fx1);
                    if (isNum(fx1)) {
                        float f = Float.valueOf(fx1);
                        if ((f >= 0 && f < 12.25) || (f > 348.76 && f <= 360)) {
                            fx = "北";
                        } else if (f > 12.26 && f < 33.75) {//22.5
                            fx = "北偏东北";
                        } else if (f > 33.76 && f < 56.25) {
                            fx = "东北";
                        } else if (f > 56.25 && f < 78.75) {
                            fx = "东偏东北";
                        } else if (f > 78.75 && f < 101.25) {
                            fx = "东";
                        } else if (f > 101.25 && f < 123.75) {
                            fx = "东偏东南";
                        } else if (f > 123.76 && f < 146.25) {
                            fx = "东南";
                        } else if (f > 146.26 && f < 168.75) {
                            fx = "南偏东南";
                        } else if (f > 168.75 && f < 191.25) {
                            fx = "南";
                        } else if (f > 191.25 && f < 213.75) {
                            fx = "南偏西南";
                        } else if (f > 213.75 && f < 236.25) {
                            fx = "西南";
                        } else if (f > 236.25 && f < 258.75) {
                            fx = "西偏西南";
                        } else if (f > 258.75 && f < 281.25) {
                            fx = "西";
                        } else if (f > 281.25 && f < 303.75) {
                            fx = "西偏西北";
                        } else if (f > 303.75 && f < 326.25) {
                            fx = "西北";
                        } else if (f > 326.25 && f < 348.75) {
                            fx = "北偏西北";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析风向时出错");
            }
            try {
                if (infoss[4].charAt(1) == '1') {
                    String fs1 = infoss[qc + index.get(1)];
                    Log.i("TAG", "风速:" + fs1);
                    if (!isNum(fs1)) {
                        //fs = "";
                    } else {
                        fs = Float.parseFloat(infoss[qc + index.get(1)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析风速时出错");
            }

            try {
                if (infoss[4].charAt(12) == '1') {
                    String js1 = infoss[qc + index.get(2)];
                    Log.i("TAG", "降水:" + js1);
                    if (!isNum(js1)) {
                        // js = "";
                    } else {
                        js = Float.parseFloat(infoss[qc + index.get(2)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析雨量时出错");
            }

            try {
                if (infoss[4].charAt(14) == '1') {
                    String wd1 = infoss[qc + index.get(3)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                wd = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + index.get(3)]) && Float.parseFloat(infoss[qc + index.get(3)]) < 580) {
                            wd = Float.parseFloat(infoss[qc + index.get(3)]) / 10 + "";
                        }
                    }
                    Log.i("TAG", "温度:" + wd);
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析温度时出错");
            }
            try {
                if (infoss[4].charAt(15) == '1') {
                    String max_wd1 = infoss[qc + index.get(4)];
                    if (max_wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (max_wd1.charAt(0) == '-') {
                        if (max_wd1.length() <= 3) {
                            max_wd1 = max_wd1.substring(1, max_wd1.length());
                            if (isNum(max_wd1) && Float.parseFloat(max_wd1) < 580) {
                                max_wd = "-" + Float.parseFloat(max_wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + index.get(4)]) && Float.parseFloat(infoss[qc + index.get(4)]) < 580) {
                            max_wd = Float.parseFloat(infoss[qc + index.get(4)]) / 10 + "";
                        }
                    }
                    Log.i("TAG", "日最高温度:" + max_wd);
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析温度时出错");
            }
            try {
                if (infoss[4].charAt(17) == '1') {
                    String min_wd1 = infoss[qc + index.get(5)];
                    if (min_wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (min_wd1.charAt(0) == '-') {
                        if (min_wd1.length() <= 3) {
                            min_wd1 = min_wd1.substring(1, min_wd1.length());
                            if (isNum(min_wd1) && Float.parseFloat(min_wd1) < 580) {
                                min_wd = "-" + Float.parseFloat(min_wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + index.get(5)]) && Float.parseFloat(infoss[qc + index.get(5)]) < 580) {
                            min_wd = Float.parseFloat(infoss[qc + index.get(5)]) / 10 + "";
                        }
                    }
                    Log.i("TAG", "日最低温度:" + min_wd);
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析温度时出错");
            }
            try {
                if (infoss[4].charAt(20) == '1') {
                    String sd1 = infoss[qc + index.get(6)];
                    Log.i("TAG", "湿度:" + sd1);
                    if (!isNum(sd1)) {
                        //sd = "";
                    } else {
                        sd = sd1;
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析湿度时出错");
            }
            try {
                if (infoss[4].charAt(21) == '1') {
                    String min_sd1 = infoss[qc + index.get(7)];
                    Log.i("TAG", "湿度:" + min_sd1);
                    if (!isNum(min_sd1)) {
                        //sd = "";
                    } else {
                        min_sd = min_sd1;
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析湿度时出错");
            }
            try {
                if (infoss[4].charAt(25) == '1') {
                    String qy1 = infoss[qc + index.get(8)];
                    Log.i("TAG", "气压:" + qy1);
                    if (!isNum(qy1)) {
                        //qy = "";
                    } else {
                        qy = Float.parseFloat(infoss[qc + index.get(8)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i("TAG_", "解析气压时出错");
            }
            try {
                if (infoss[4].charAt(55) == '1') {
                    //能见度
                    String njd1 = "";
                    njd1 = infoss[qc + index.get(9)];
                    Log.i("TAG", "能见度:" + njd1);
                    if (!isNum(njd1)) {
                        //njd = "12345";
                    } else {
                        njd = njd1;
                    }

                }
            } catch (Exception e) {
                Log.i("TAG_", "解析能见度时出错");
            }
            LiveDataBus.getInstance().setElements(new Elements("语溪小学气象站", date, time, wd, max_wd, min_wd, sd, min_sd, fx, fs, js, qy, njd));
            sendCount++;
        }

    }
    private Timer timerListener;
    public boolean isNum(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) str);
        boolean result = matcher.matches();
        return result;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unbindService(conn);
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        super.onDestroy();
    }

    public String stringToGbk(String string) throws Exception {
        byte[] bytes = new byte[string.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            byte high = Byte.parseByte(string.substring(i * 2, i * 2 + 1), 16);
            byte low = Byte.parseByte(string.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high << 4 | low);
        }
        String result = new String(bytes, "gbk");
        return result;
    }

    private void restartApp() {
        Log.i("TAG_Service", "重启app");
       /* Intent activity = new Intent(getApplicationContext(), MainActivity.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activity);*/
        Intent i = getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(getApplicationContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplicationContext().startActivity(i);
        Process.killProcess(Process.myPid());
        // android.os.Process.killProcess(Process.myPid());
    }


}