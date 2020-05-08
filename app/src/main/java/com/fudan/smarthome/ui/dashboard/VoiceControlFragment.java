package com.fudan.smarthome.ui.dashboard;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fudan.smarthome.aip.ParticipleResult;
import com.fudan.smarthome.mqttService.HomeMQTTService;
import com.fudan.smarthome.voiceRecognition.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fudan.smarthome.R;

import java.util.HashMap;

public class VoiceControlFragment extends Fragment {
    private TextView txtResult;
    private Button speakBtn;
    private int status;
    private CommandViewModel dashboardViewModel;
    private MyRecognition myRecognition;
    private Handler handler;
    private static final String TAG = "SpeakActivity";
    private View root;
    private CommandViewModel commandViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        commandViewModel = CommandViewModel.getCommandViewModel();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "进入Handler");
                handleMsg(msg);
            }
        };
        initView();
        initRecognition();
        commandViewModel = CommandViewModel.getCommandViewModel();
        return root;
    }

    protected void initView() {

//        Activity activity = getActivity();
        View activity = root;
        txtResult = activity.findViewById(R.id.txtResult);
//        txtLog = activity.findViewById(R.id.txtLog);
        speakBtn = activity.findViewById(R.id.speakBtn);
        txtResult.setText("");
        speakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (status) {
                    case IStatus.STATUS_NONE: // 初始状态
                        start();
                        status = IStatus.STATUS_WAITING_READY;
                        updateBtnTextByStatus();
                        txtResult.setText("");
//                        txtLog.setText("");

                        break;
                    case IStatus.STATUS_WAITING_READY: // 调用本类的start方法后，即输入START事件后，等待引擎准备完毕。
                    case IStatus.STATUS_READY: // 引擎准备完毕。
                    case IStatus.STATUS_SPEAKING:
                    case IStatus.STATUS_RECOGNITION:
                        stop();
                        status = IStatus.STATUS_STOPPED; // 引擎识别中
                        updateBtnTextByStatus();
                        break;
                    case IStatus.STATUS_STOPPED: // 引擎识别中
                        cancel();
                        status = IStatus.STATUS_NONE; // 识别结束，回到初始状态
                        updateBtnTextByStatus();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        myRecognition.release();
        super.onDestroy();
    }

    private void initRecognition() {
        MessageRecogListener listener = new MessageRecogListener(handler);
        myRecognition = new MyRecognition(getContext(), listener);
        status = IStatus.STATUS_NONE;
    }

    private void start() {

        myRecognition.start(new HashMap<String, Object>());
    }

    private void stop() {
        myRecognition.stop();
    }

    private void cancel() {
        myRecognition.cancel();
    }

    private void updateBtnTextByStatus() {
        switch (status) {
            case IStatus.STATUS_NONE:
                speakBtn.setText("开始录音");
                speakBtn.setEnabled(true);
                break;
            case IStatus.STATUS_WAITING_READY:
            case IStatus.STATUS_READY:
            case IStatus.STATUS_SPEAKING:
            case IStatus.STATUS_RECOGNITION:
                speakBtn.setText("停止录音");
                speakBtn.setEnabled(true);
                break;

            case IStatus.STATUS_STOPPED:
                speakBtn.setText("取消识别");
                speakBtn.setEnabled(true);
                break;
            default:
                break;
        }
    }

    protected void handleMsg(Message msg) {
        if (txtResult != null && msg.obj != null && !(msg.obj instanceof ParticipleResult) && msg.obj != "" && msg.arg2 == 1) {
            txtResult.setText(msg.obj.toString());
        }
        Log.d(TAG, "进入view");
        if (msg.obj instanceof ParticipleResult) {
            Log.i(TAG, "get ParticipleResult");
            Log.i(TAG, "equip :" + ((ParticipleResult) msg.obj).getDevice());
            Log.i(TAG, "what:" + msg.what);
            // String[] order={result.getEquipment(),result.getAction(),result.getAction(),result.getMode()};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_segement, null);
            builder.setView(view);
            final EditText edit_equi = (EditText) view.findViewById(R.id.edit_equi);
            final EditText edit_act = (EditText) view.findViewById(R.id.edit_act);
            final EditText edit_value = (EditText) view.findViewById(R.id.edit_value);
            final EditText edit_mode = (EditText) view.findViewById(R.id.edit_mode);

            ParticipleResult participleResult = (ParticipleResult) msg.obj;
            edit_equi.setText(participleResult.getDevice());
            edit_act.setText(participleResult.getAction());
            edit_value.setText(participleResult.getValue());
            edit_mode.setText(participleResult.getMode());
            builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    commandViewModel.setCommand("1");
                    //edit之后确认的指令实体！！！！！！
//                    OrderEntity orderEntity=new OrderEntity(edit_equi.getText().toString(),edit_act.getText().toString(),edit_value.getText().toString(),edit_mode.getText().toString());
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //cancel 在设置了cancelListener时有区别，在这里没必要设置
                }
            });

            builder.setTitle("指令确认");
            builder.create().show();
        }
        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case IStatus.STATUS_FINISHED:
                if (msg.arg2 == 1) {
                    assert msg.obj != null;
                    txtResult.setText(msg.obj.toString());
                }
                status = msg.what;
                updateBtnTextByStatus();
                break;
            case IStatus.STATUS_NONE:
            case IStatus.STATUS_READY:
            case IStatus.STATUS_SPEAKING:
            case IStatus.STATUS_RECOGNITION:
                status = msg.what;
                Log.i(TAG, "status " + status);
                updateBtnTextByStatus();
                break;
            default:
                break;
        }
    }
}