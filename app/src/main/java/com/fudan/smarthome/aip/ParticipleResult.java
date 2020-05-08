package com.fudan.smarthome.aip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ParticipleResult {
    private String device;
    private String action;
    private String time;
    private String value;
    private String mode;
    private String[] items;

    public static ParticipleResult parseJson(JSONObject jsonObject) {
        ParticipleResult participleResult = new ParticipleResult();
        try {
            JSONArray jsonArray = jsonObject.optJSONArray("items");

            if (jsonArray != null) {
                String[] resultItem = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    resultItem[i] = object.optString("item");
                    if (object.optString("pos").equals("m")) {
                        participleResult.value = resultItem[i];
                        continue;
                    }
                    String ne = object.optString("ne");
                    switch (ne) {
                        case "DEVICE":
                            // result.equipment = object.optString("item");
                            participleResult.setDevice(resultItem[i]);
                            break;
                        case "ACTION":
                            // result.action = object.optString("item");
                            participleResult.setAction(resultItem[i]);
                            break;
                        case "MODE":
                            // result.mode = object.optString("item");
                            participleResult.setMode(resultItem[i]);
                            break;
                        case "TIME":
                            // result.mode = object.optString("item");
                            participleResult.setTime(resultItem[i]);
                            break;

                    }

                }
                participleResult.setItems(resultItem);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return participleResult;

    }

    private ParticipleResult() {
    }

    public String[] getItems() {
        return items;
    }

    private void setItems(String[] items) {
        this.items = items;
    }

    private void setDevice(String device) {
        this.device = device;
    }

    private void setAction(String action) {
        this.action = action;
    }

    private void setTime(String time) {
        this.time = time;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private void setMode(String mode) {
        this.mode = mode;
    }

    public String getDevice() {
        return device;
    }

    public String getAction() {
        return action;
    }

    public String getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }

    public String getMode() {
        return mode;
    }
}
