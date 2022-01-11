package com.example.patchment20;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;

public class MyDatabase {
    private static SharedPreferences sharedPreferences;

    public static void setString(Context context, String key, String data){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putString(key, data);
        spe.apply();
    }
    public static String getString(Context context, String key){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static void setSetString(Context context, String key, ArrayList<String> data){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        HashSet<String> hashset = new HashSet<>(data);
        spe.putStringSet(key, hashset);
        spe.apply();
    }
    public static ArrayList<String> getSetString(Context context, String key){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        HashSet<String> hashset = (HashSet<String>) sharedPreferences.getStringSet(key, null);
        return new ArrayList<String>(hashset);
    }
}
