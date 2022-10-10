package com.kd.appelements.tools;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUploadTool {
    private static OkHttpClient client;
    private static OkHttpUploadTool uploadTool;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static String cardId;

    public static OkHttpUploadTool getInstance() {
        if (uploadTool == null) {
            uploadTool = new OkHttpUploadTool();
        }
        if (client == null) {
            client = new OkHttpClient();
        }
        return uploadTool;
    }

    public static void uploadString(String siteNum,String info) {
        if (TextUtils.isEmpty(info)) {
            return;
        }
        if (TextUtils.isEmpty(cardId)) {
            cardId = getCardId();
        }
        String date = simpleDateFormat.format(new Date());
        Log.i("TAG", date + ";cardId:" + cardId);

        String sendInfo = "{\"site\":\""+siteNum+"\",\"id\":\"" + cardId + "\",\"date\":\"" + date + "\",\"" + info + "\"}";
        Log.i("TAG", "sendInfo:"+sendInfo);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), sendInfo);
        Request request = new Request.Builder().url("http://112.16.49.28:8081/qxdata/").post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("TAG", "上传失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", "上传成功");
                String sss = response.body().string();
                Log.i("TAG", "ss:" + sss);
            }
        });
    }

    public static String GetCardId(byte[] cBuf) {
        byte[] cMyKey = new byte[]{97, 119, 38, 3, 46, 112, 36, 93, 58, 100, 103, 62, 115, 112, 114, 51, 43, 61, 2, 101, 119};
        if (cBuf.length < 40) {
            return "";
        } else {
            for (int i = 0; i < 20; ++i) {
                cBuf[i] = (byte) (cBuf[i * 2] - cMyKey[i] - i - (cBuf[i * 2 + 1] - 3));
            }
            String strtemp = new String(cBuf);
            if (strtemp.length() >= 13) {
                strtemp = strtemp.substring(0, 13);
            }

            return strtemp;
        }
    }


    public static byte[] getByte(String src) {
        File file = new File(src);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read;
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
            out.close();
            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    final static String CID_PATH = "/data/joey/signed/card.id";

    public static String getCardId() {
        byte[] content = getByte(CID_PATH);
        String value = null;
        try {
            value = GetCardId(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
