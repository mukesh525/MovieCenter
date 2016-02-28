package muk.materialdesign.Utils;

import android.app.Application;
import android.content.Context;

import muk.materialdesign.DataBase.DBMovies;

/**
 * Created by Mukesh on 4/14/2015.
 */
public class MyApplication extends Application {
    public static  MyApplication sInstance;
    private static DBMovies mDatabase;
    public static final String API_KEY_ROTTEN_TOMATOES = "54wzfswsa4qmjg8hjwa64d4c";
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }

    public static  MyApplication getInstance()
    {
        return  sInstance;
    }

    public static Context getAplicationContext()
    {
        return  sInstance.getApplicationContext();
    }
    public synchronized static DBMovies getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBMovies( getAplicationContext());
        }
        return mDatabase;
    }

}
