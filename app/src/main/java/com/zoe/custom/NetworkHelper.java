package com.zoe.custom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by scorp on 2016/12/29.
 */
public class NetworkHelper {

    private static NetworkHelper mNetHelper;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetInfo;
    private Context mCon;

    public NetworkHelper(Context mCon) {
        mConnectivityManager = (ConnectivityManager) mCon
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        this.mCon = mCon;
    }

    public static NetworkHelper getInstance(Context mCon) {
        if (mNetHelper == null) {
            mNetHelper = new NetworkHelper(mCon);
        }
        return mNetHelper;
    }

    /** Get the network type. */
    public String getNetWorkType() {
        mNetInfo = mConnectivityManager.getActiveNetworkInfo();
        String typeName = null;
        if (mNetInfo != null && mNetInfo.isAvailable()) {
            typeName = mNetInfo.getTypeName();
        }
        return typeName;
    }

    /** Indicates whether network connectivity is possible. */
    public boolean isNetSuccess() {
        ConnectivityManager cwjManager = (ConnectivityManager) mCon
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}
