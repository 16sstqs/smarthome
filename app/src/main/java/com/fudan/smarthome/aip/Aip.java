package com.fudan.smarthome.aip;

import android.os.Message;
import android.util.Log;

import com.baidu.aip.nlp.AipNlp;
import com.fudan.smarthome.voiceRecognition.MessageRecogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Aip {
    //设置APPID/AK/SK
    private static final String APP_ID = "18588480";
    private static final String API_KEY = "CuQSAZ2QVeWcR1zuQ5X8yYpu";
    private static final String SECRET_KEY = "CHE4pURmmgDb9fv2PUClWnf4AUcAPXo6";
    private static final String TAG = "AIP";

    private JSONObject getJsonResult(String string) {
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        String text = string;
        JSONObject res = client.lexerCustom(text, null);
        try {
            System.out.println(res.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }


    public static void performWithAnswer(final String words, final MessageRecogListener callBackSegment) {

        if (null == callBackSegment)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Aip aip = new Aip();
                ParticipleResult result = aip.getResult(aip.getJsonResult(words));
                Log.i(TAG, "原始解析结果为：" + words);
                Log.d(TAG, "进入分词后");
                callBackSegment.callBackSegmentResult(result);
            }
        }).start();
    }

    public ParticipleResult getResult(JSONObject string) {
        if (!string.has("error_code")) {
            ParticipleResult participleResult = ParticipleResult.parseJson(string);
            if (participleResult.getMode() != null || participleResult.getDevice() != null) {
                return participleResult;
            }
        }
        return null;
    }
}
