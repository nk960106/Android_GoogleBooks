package com.example.googlebooks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Dictionary;

public class Utility {
    public static  final String KEY_PARAMS="KEY";
    private static  final String TAG="BooksQuery";
    //顯示LOG
    public  static  void printLog(String message){
        if(message == null || message.isEmpty()){
            return;
        }
        if(message.length()>2000){
            Log.d(TAG,"sb.length = " + message.length());
            int chunkCount = message.length() / 2000;
            for (int i =0; i <= chunkCount; i++){
                int max = 2000*(i+1);
                if(max >= message.length()){
                    Log.d(TAG,"chunk " + i + "of" + chunkCount + ":" + message.substring(2000*i));
                }
                else{
                    Log.d(TAG,"chunk " + i + "of" + chunkCount + ":" + message.substring(2000*i,max));
                }
            }
        }
        else{
            Log.d(TAG,message);
        }
    }

    public static  String getJsonString(JSONObject object,String Key){
        try{
            if(object.has(Key)){
                return object.getString(Key);
            }
            else{
                return  "";
            }
        }catch (Exception ex){
            return  "";
        }
    }
    //作者 另外處理 裡面是陣列
    public static String getAuthorArrayToString(JSONObject object,String key){
        try{
            JSONArray jsonArray = object.getJSONArray(key);
            String rebuild="";
            StringBuilder sb = new StringBuilder();
            for (int i =0 ; i<jsonArray.length(); i++){
                if(rebuild.length() == 0){
                    rebuild = jsonArray.getString(i);
                }
                else{
                    rebuild = sb.append(rebuild).append(",").append(jsonArray.get(i)).toString();
                }
            }
            return rebuild;
        }
        catch (Exception ex){
            return  "";
        }
    }
    //將圖片URL 另外處理 多一層JSON
    public static String getUrlToString(JSONObject object,String key){
        try{
            JSONObject jsonObject = object.getJSONObject(key);
            String smallThumbnail = jsonObject.getString("smallThumbnail");
           return smallThumbnail;
        }
        catch (Exception ex){
            return  "";
        }
    }
}
