package com.fudan.smarthome.voiceRecognition;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fudan.smarthome.aip.Aip;
import com.fudan.smarthome.aip.ParticipleResult;

import org.json.JSONException;

import java.io.IOException;


public class MessageRecogListener implements IStatus,IRecogListener{
    private static final String TAG = "MessageRecogListener";
    private Handler handler;
  //分词并识别的处理结果
     private ParticipleResult segmentResult;

    private   int status;

    public MessageRecogListener(Handler handler){
        this.handler=handler;

    }

    private void sendSegmentMessage(ParticipleResult segmentResult,int what,boolean highlight){
        if(handler==null){
            return;
        }
        Message message=Message.obtain();
        message.what=what;
        message.arg1=status;
        if(highlight){
            message.arg2=1;
        }
        message.obj=segmentResult;
        handler.sendMessage(message);
    }

    private void sendMessage(String message,int what,boolean hightlight){
        if(handler==null){
            Log.i(TAG,message);
            return;
        }
        Message msg=Message.obtain();
        msg.what=what;
        msg.arg1=status;
        Log.i(TAG,"what "+what);
        Log.i(TAG,"status "+status);
        if(hightlight){
            msg.arg2=1;
        }
        msg.obj=message+"\n";
        Log.i(TAG,message);
        handler.sendMessage(msg);
    }

    private void sendStatusMessage(String message) {
        sendMessage(message, status,false);
    }


    @Override
    public void onAsrReady() {
        status=STATUS_READY;
        sendMessage("请说话…",status,true);
       // Log.i(TAG,"please_speaking");
            }

    @Override
    public void onAsrBegin() {
        status=STATUS_SPEAKING;
        sendMessage("识别开始…",status,true);
    }

    @Override
    public void onAsrEnd() {
        status=STATUS_RECOGNITION;
        sendMessage("说话结束…",status,true);
    }


    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {
        sendMessage("正在识别…"+results[0],status,true);
        Log.i(TAG,recogResult.getOrigalJson());
    }

    @Override
    public void onAsrFinalResult(final String[] results, RecogResult recogResult) {
        Log.i(TAG,recogResult.getOrigalJson());
        status=STATUS_FINISHED;
        sendMessage(results[0],status,true);
        //得到最终识别结果，再次调用分词
        //
        Aip.performWithAnswer(results[0],this);
        Log.i(TAG,"调用request");
        }
        /*
        中文分词
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //////why final?
//                    String cut=  RequestSegment.sendBody(results[0]);
//                    sendMessage("分词原始json"+cut);
//                    SegementResult result=  SegementResult.parseJson(cut);
//                    sendMessage("Action:"+result.getAction());
//                    sendMessage("Equipment:"+result.getEquipment());
//                    sendMessage("Mode:"+result.getMode());
//                    sendMessage("Value:"+result.getValue());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();




    @Override
    public void onAsrFinish(RecogResult recogResult) {
        status=STATUS_FINISHED;
        sendStatusMessage("识别一段话结束。");

    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String errorMessage, String descMessage, RecogResult recogResult) {
        status = STATUS_FINISHED;
        sendStatusMessage("识别出现错误。");
    }

    @Override
    public void onAsrLongFinish() {
        status = STATUS_FINISHED;
    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {
        Log.i(TAG, "音量百分比" + volumePercent + " ; 音量" + volume);
    }

    @Override
    public void onAsrAudio(byte[] data, int offset, int length) {
        if (offset != 0 || data.length != length) {
            byte[] actualData = new byte[length];
            System.arraycopy(data, 0, actualData, 0, length);
            data = actualData;
        }

        Log.i(TAG, "音频数据回调, length:" + data.length);
    }

    @Override
    public void onAsrExit() {
        status = STATUS_NONE;
        sendStatusMessage("识别引擎结束并空闲中");
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {

    }

    @Override
    public void onOfflineLoaded() {

    }

    @Override
    public void onOfflineUnLoaded() {

    }

    //回调接口，RequestSegment中调用，传回来分词结果
    public void callBackSegmentResult( ParticipleResult result) {
        Log.i(TAG,"进入callback");
        if(result==null)
            return;

        this.segmentResult=result;
        Log.i(TAG,segmentResult.getDevice()+"设备名");
        sendSegmentMessage(segmentResult,STATUS_SEGMENT_FINISHED,true);
    }

}
