package com.fudan.smarthome.CommandProcessor;

import com.fudan.smarthome.Utils.TimeStringToMillisecondUtil;
import com.fudan.smarthome.aip.ParticipleResult;
import com.fudan.smarthome.ui.dashboard.CommandViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleCommandProcessor {
    private static CommandViewModel commandViewModel = CommandViewModel.getCommandViewModel();

    public static void sendSingleCommand(final ParticipleResult participleResult) {
        final String result = getResultJsonArrayString(participleResult);
        if (participleResult.getTime() != null) {
            new Thread(new Runnable() {

                public void run() {

                    try {
                        Thread.sleep(timeProcessor(participleResult));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    commandViewModel.setCommand(result);
                }

            }).start();
        } else {
            commandViewModel.setCommand(result);
        }
    }


    private static int deviceProcessor(ParticipleResult participleResult) {
        String device = participleResult.getDevice();
        if (device == null || "".equals(device)) {
            return -1;
        }
        if (device.contains("空调") || device.contains("扇")) {
            return 7;
        } else if (device.contains("灯")) {
            if (participleResult.getLocation() == null) {
                return 14;
            } else {
                return participleResult.getLocation().equals("卧室") ? 3 : 4;
            }
        } else if (device.contains("音乐")||device.contains("电视")) {
            return 8;
        } else {
            return -1;
        }
    }

    private static int actionProcessor(ParticipleResult participleResult) {
        if (participleResult.getAction().contains("开") || participleResult.getAction().contains("启动")) {
            return 1;
        } else if (participleResult.getAction().contains("关")) {
            return 0;
        } else {
            return -1;
        }
    }

    private static int timeProcessor(ParticipleResult participleResult) {
        return TimeStringToMillisecondUtil.getMillisecond(participleResult.getTime());
    }

    public static String getResultJsonArrayString(ParticipleResult participleResult) {
        JSONObject UIS = new JSONObject();
        try {
//            jsonObject.put("User", "01");
//            jsonObject.put("Time", "01");
            UIS.put("D", deviceProcessor(participleResult));
            UIS.put("A", actionProcessor(participleResult));

//            for (int i = 0; i < 2; i++) {

//                jsonArray.put(UIS);
//            }


//            jsonObject.put("C", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return UIS.toString();
    }
}

