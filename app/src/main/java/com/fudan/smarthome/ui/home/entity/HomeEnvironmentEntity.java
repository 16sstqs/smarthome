package com.fudan.smarthome.ui.home.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeEnvironmentEntity {


    private String temp;
    private String hum;
    private String longitude;
    private String latitude;
    private boolean bedRoomLightSwitch;
    private boolean LivingRoomLightSwitch;
    private boolean airConditionSwitch;
    private final String TAG = "HomeEnvironmentEntity";

    private String jsonString;


    public HomeEnvironmentEntity(String jsonString) {
        this.jsonString = jsonString;
        try {
            Log.i(TAG, "original json string" + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.i(TAG, "original json object" + jsonObject.toString());

            setTemp(jsonObject.getString("temp"));
            setHum(jsonObject.getString("hum"));
//            setLongitude(jsonObject.getString("long"));
//            setLatitude(jsonObject.getString("lat"));
            Log.i(TAG, "temp" + getTemp());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HomeEnvironmentEntity() {
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public boolean isBedRoomLightSwitch() {
        return bedRoomLightSwitch;
    }

    public void setBedRoomLightSwitch(boolean bedRoomLightSwitch) {
        this.bedRoomLightSwitch = bedRoomLightSwitch;
    }

    public boolean isLivingRoomLightSwitch() {
        return LivingRoomLightSwitch;
    }

    public void setLivingRoomLightSwitch(boolean livingRoomLightSwitch) {
        LivingRoomLightSwitch = livingRoomLightSwitch;
    }

    public boolean isAirConditionSwitch() {
        return airConditionSwitch;
    }

    public void setAirConditionSwitch(boolean airConditionSwitch) {
        this.airConditionSwitch = airConditionSwitch;
    }
}
