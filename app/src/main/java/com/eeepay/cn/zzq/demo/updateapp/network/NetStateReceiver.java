package com.eeepay.cn.zzq.demo.updateapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.eeepay.cn.zzq.demo.updateapp.utils.NetUtils;

import java.util.ArrayList;

/**
 * 描述：使用广播去监听网络
 * 作者：zhuangzeqin
 * 时间: 2017/9/5-14:15
 * 邮箱：zzq@eeepay.cn
 */
public class NetStateReceiver extends BroadcastReceiver {
    private final static String TAG = NetStateReceiver.class.getSimpleName();
    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.eeepay.cn.api.netstatus.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;//"android.net.conn.CONNECTIVITY_CHANGE";

    private static boolean isNetAvailable = false;
    private static NetUtils.NetType mNetType;
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();
    private static BroadcastReceiver mBroadcastReceiver;

    /**
     * 获取 BroadcastReceiver 实例
     * @return
     */
    private static BroadcastReceiver getReceiverInstance() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        mBroadcastReceiver = NetStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) || intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION)) {
            boolean networkAvailable = NetUtils.isNetworkConnected(context);
            if (!networkAvailable) {
                Log.e(TAG, "<--- network disconnected --->");
                isNetAvailable = networkAvailable;
            } else {
                Log.e(TAG, "<--- network connected --->");
                isNetAvailable = networkAvailable;
                mNetType = NetUtils.getAPNType(context);
            }
            notifyObserver();//发出通知
        }
    }

    /**
     * 注册
     *
     * @param context
     */
    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiverInstance(), filter);
    }
    /**
     * 反注册
     *
     * @param context
     */
    public static void unRegisterNetworkStateReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            try {
                context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {

            }
        }
    }

    /**
     * 检查网络状态
     *
     * @param context
     */
    public static void checkNetworkState(Context context) {
        Intent intent = new Intent();
        intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        context.sendBroadcast(intent);
    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    public static NetUtils.NetType getAPNType() {
        return mNetType;
    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }

    /**
     * 添加网络监听
     *
     * @param observer
     */
    public static void addObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    /**
     * 移除网络监听
     *
     * @param observer
     */
    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }

}
