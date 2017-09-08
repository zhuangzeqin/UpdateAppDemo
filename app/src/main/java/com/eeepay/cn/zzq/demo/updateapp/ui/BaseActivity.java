package com.eeepay.cn.zzq.demo.updateapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eeepay.cn.zzq.demo.updateapp.network.NetChangeObserver;
import com.eeepay.cn.zzq.demo.updateapp.network.NetStateReceiver;
import com.eeepay.cn.zzq.demo.updateapp.utils.NetUtils;

/**
 * 描述：抽象一个基类
 * 作者：zhuangzeqin
 * 时间: 2017/9/5-14:42
 * 邮箱：zzq@eeepay.cn
 */
public abstract class BaseActivity extends Activity {
    protected Activity mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        //开启广播去监听 网络 改变事件
        NetStateReceiver.addObserver(mNetChangeObserver);

        initWidget();
    }

    /**
     * 网络观察者
     */
   protected final NetChangeObserver mNetChangeObserver = new NetChangeObserver() {
       @Override
       public void onNetConnected(NetUtils.NetType type) {
           onNetworkConnected(type);
       }

       @Override
       public void onNetDisConnect() {
           onNetworkDisConnected();
       }
   };

    @Override
    protected void onDestroy() {
        if (mNetChangeObserver!=null)
            NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        super.onDestroy();
    }

    /**
     *
     * 布局id
     */
    protected abstract int getLayoutId();
    /**
     * 初始化控件
     */
    protected abstract void initWidget();
    /**
     * 网络连接状态
     *
     * @param type 网络状态
     */
    protected abstract void onNetworkConnected(NetUtils.NetType type);

    /**
     * 网络断开的时候调用
     */
    protected abstract void onNetworkDisConnected();
}
