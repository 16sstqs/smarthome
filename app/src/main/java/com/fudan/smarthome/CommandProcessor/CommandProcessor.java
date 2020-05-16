package com.fudan.smarthome.CommandProcessor;

import com.fudan.smarthome.aip.ParticipleResult;
import com.fudan.smarthome.ui.dashboard.CommandViewModel;

public class CommandProcessor {
    public static void commandProcessor(ParticipleResult participleResult) {
        if (participleResult.getMode() != null) {

        } else {
            SingleCommandProcessor.sendSingleCommand(participleResult);
        }
    }
}
