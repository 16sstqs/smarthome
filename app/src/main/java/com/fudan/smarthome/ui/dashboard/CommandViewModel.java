package com.fudan.smarthome.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import androidx.lifecycle.ViewModel;

public class CommandViewModel extends ViewModel {
    private static volatile CommandViewModel commandViewModel;
    private final String TAG = "CommandViewModel";
    private Handler handler;
    private Bundle bundle = new Bundle();

    private CommandViewModel() {
    }

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