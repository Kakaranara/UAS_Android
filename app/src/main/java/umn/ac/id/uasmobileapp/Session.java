package umn.ac.id.uasmobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setKey(String key){
        prefs.edit().putString("key",key).commit();
    }

    public String getKey(){
        String key = prefs.getString("key","");
        return key;
    }

}
