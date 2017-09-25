package com.eeepay.cn.zzq.demo.updateapp;

import android.app.Application;
import android.util.Log;

import com.eeepay.cn.zzq.demo.updateapp.network.NetStateReceiver;
import com.vise.log.ViseLog;
import com.vise.log.inner.DefaultTree;

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
        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("ViseLog")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        ViseLog.plant(new DefaultTree());//添加打印日志信息到Logcat的树

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
