package com.eeepay.cn.zzq.demo.updateapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 描述：网络工具类--网络状态类型
 * 作者：zhuangzeqin
 * 时间: 2017/9/5-14:06
 * 邮箱：zzq@eeepay.cn
 */
public class NetUtils {
    public static enum NetType {
        WIFI,//wifi
        TYPE_MOBILE,//手机网络
        //        CMNET,//手机网络
//        CMWAP,
        NONE// 无网络
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    public static NetType getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE;//网络不可用
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {//
            return NetType.TYPE_MOBILE;
//            if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
//                return NetType.CMNET;
//            } else {
//                return NetType.CMWAP;
//            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;//wifi 网络
        }
        return NetType.NONE;
    }
}
