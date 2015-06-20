package com.uma.tfg.appartment.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    public static String getStringFromJSONObject(String key, JSONObject jsonObject){
        if (jsonObject.has(key)){
            try {
                return jsonObject.getString(key);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static int getIntFromJSONObject(String key, JSONObject jsonObject){
        if (jsonObject.has(key)){
            try {
                return jsonObject.getInt(key);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return 0;
    }

    public static double getDoubleFromJSONObject(String key, JSONObject jsonObject) {
        if (jsonObject.has(key)){
            try{
                return Double.parseDouble(jsonObject.getString(key));
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return 0.0;
    }

    public static JSONArray getJSONArrayFromJSONObject(String key, JSONObject jsonObject){
        if (jsonObject.has(key)){
            try {
                return jsonObject.getJSONArray(key);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject getJSONObjectFromJSONObject(String key, JSONObject jsonObject){
        if (jsonObject.has(key)){
            try{
                return jsonObject.getJSONObject(key);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray, int position){
        try{
            return jsonArray.getJSONObject(position);
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static String getStringFromJSONArray(JSONArray jsonArray, int position){
        try{
            return jsonArray.getString(position);
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
