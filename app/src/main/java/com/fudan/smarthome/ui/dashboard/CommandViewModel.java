package com.fudan.smarthome.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.fudan.smarthome.R;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommandViewModel extends ViewModel {

    //    private static volatile MutableLiveData<String> command;
    private static volatile CommandViewModel commandViewModel;
    private final String TAG = "CommandViewModel";
    private Handler handler;
    private Bundle bundle = new Bundle();

    private CommandViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is dashboard fragment");


    }

//    public LiveData<String> getCommand() {
//        return command;
//    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }


    public void setCommand(String string) {
        Message message = handler.obtainMessage();
        bundle.clear();
        bundle.putString("command", string);
        message.setData(bundle);
        message.what = 1;
        handler.sendMessage(message);
//        command.setValue(string);
    }

    public static CommandViewModel getCommandViewModel() {

        if (commandViewModel == null) {
            synchronized (CommandViewModel.class) {
                if (commandViewModel == null) {
                    commandViewModel = new CommandViewModel();
                }
            }
        }
        return commandViewModel;
    }


}