package com.mz.bf.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.code.Code;

public class CodeSharedPreferance {
    Context context;
    SharedPreferences mPrefs1;
    private static CodeSharedPreferance instance=null;


    public CodeSharedPreferance(Context context) {
        this.context = context;
    }

    public CodeSharedPreferance() {

    }

    public static  CodeSharedPreferance getInstance()
    {
        if (instance==null)
        {
            instance = new CodeSharedPreferance();
        }
        return instance;
    }

    public void Create_Update_UserData(Context context, Code userModel)
    {
        mPrefs1 = context.getSharedPreferences("code", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(userModel);
        SharedPreferences.Editor editor = mPrefs1.edit();
        editor.putString("code_data",userData);
        editor.apply();
        Create_Update_Session(context, "login");

    }

    public void Create_Update_Session(Context context, String session)
    {
        mPrefs1 = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs1.edit();
        editor.putString("state",session);
        editor.apply();
    }


    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", "logout");
        return session;
    }


    public Code Get_UserData(Context context){
        mPrefs1 = context.getSharedPreferences("code", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String userData = mPrefs1.getString("code_data", "");
        Code userModel=gson.fromJson(userData,Code.class);
        return userModel;
    }

    public void ClearData(Context context) {
        LoginModel userModel = null;
        mPrefs1 = context.getSharedPreferences("code", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(userModel);
        SharedPreferences.Editor editor = mPrefs1.edit();
        editor.putString("code_data", userData);
        editor.apply();
        Create_Update_Session(context,"login");
    }
}
