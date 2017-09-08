package com.eeepay.cn.zzq.demo.updateapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * 描述：监听apk安装替换卸载广播
 * 作者：zhuangzeqin
 * 时间: 2017/9/7-10:12
 * 邮箱：zzq@eeepay.cn
 */
public class AppInstallReceiver extends BroadcastReceiver {
    private final String TAG  = AppInstallReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
//        PackageManager manager = context.getPackageManager();
        final String DEFAULTSAVEPATH  = Utils.getCachePath(context)+ File.separator;//默认的保存路径;
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"安装成功"+packageName);
            //安装完成之后；删除下载文件； 和清空db里的数据
            Utils.deleteFile(new File(DEFAULTSAVEPATH));
            FileDownloader.getImpl().clearAllTaskData();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"卸载成功"+packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"替换成功"+packageName);
        }
    }
}
