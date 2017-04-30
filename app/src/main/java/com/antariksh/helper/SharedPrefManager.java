package com.antariksh.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aditya Singh on 27-04-2017.
 */

public class SharedPrefManager {
    Context context;
    public SharedPrefManager(Context context)
    {
        this.context=context;
    }

    public void setLoggedIn(Boolean loggedIn)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences( Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(Constants.SHARED_USER_LOGGED_IN,loggedIn);
        editor.apply();
    }

    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences( Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.SHARED_USER_LOGGED_IN,false);
    }



}
