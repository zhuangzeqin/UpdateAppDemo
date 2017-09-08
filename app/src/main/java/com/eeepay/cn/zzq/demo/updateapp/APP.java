package com.eeepay.cn.zzq.demo.updateapp;

import android.app.Application;

import com.eeepay.cn.zzq.demo.updateapp.network.NetStateReceiver;

/**
 * 描述：app 全局的Application
 * 作者：zhuangzeqin
 * 时间: 2017/9/5-14:11
 * 邮箱：zzq@eeepay.cn
 */
public class APP extends Application {
    private static APP instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
    }

    public static APP getInstance()
    {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //低内存的时候回收掉
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
