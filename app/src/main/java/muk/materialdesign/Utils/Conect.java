package muk.materialdesign.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import muk.materialdesign.Model.Movie;

/**
 * Created by Mukesh on 4/23/2015.
 */
public class Conect {

    public static Boolean isInternateOn(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}
