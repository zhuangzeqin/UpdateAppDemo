package com.eeepay.cn.zzq.demo.updateapp.network;

import com.eeepay.cn.zzq.demo.updateapp.utils.NetUtils;

/**
 * 描述：网络改变观察者，观察网络改变后回调的方法
 * 作者：zhuangzeqin
 * 时间: 2017/9/5-14:13
 * 邮箱：zzq@eeepay.cn
 */
public interface NetChangeObserver {
    /**
     * 网络连接回调 type为网络类型
     */
    void onNetConnected(NetUtils.NetType type);

    /**
     * 没有网络
     */
    void onNetDisConnect();
}
