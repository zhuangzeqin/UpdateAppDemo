package com.eeepay.cn.zzq.demo.updateapp.network;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.eeepay.cn.zzq.demo.updateapp.R;
import com.eeepay.cn.zzq.demo.updateapp.utils.NetUtils;
import com.eeepay.cn.zzq.demo.updateapp.utils.Utils;
import com.eeepay.cn.zzq.demo.updateapp.view.CustomDialog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.net.UnknownHostException;

/**
 * 描述：通知栏显示下载进度条
 * 作者：zhuangzeqin
 * 时间: 2017/9/8-9:28
 * 邮箱：zzq@eeepay.cn
 */
public class ShowProgressFileDownload extends FileDownloadListener {

    private final String TAG = ShowProgressFileDownload.class.getSimpleName();

    private static final int NOTIFICATIONID = 103;//通知栏id

    private final int MININTERVALUPDATESPEED = 400;//最小时间间隔更新下载速度

    private final int CALLBACKPROGRESSTIMES = 300;//回调进度时间

    private Activity mContext;//上下文对象

    private String mDownloadUrl;//下载文件路径

    private String mTargetFilePath;//apk 文件路径

    private boolean isWifiRequired = false;//是否wifi 请求下载

    private NotificationCompat.Builder builderProgress;

    private NotificationManager notificationManager;

    private CustomDialog dialog;

    private ShowProgressFileDownload(Activity activity)
    {
        this.mContext = activity;
        //显示进度条通知
        builderProgress = new NotificationCompat.Builder(mContext);
        builderProgress.setContentTitle("下载中");
        builderProgress.setSmallIcon(android.R.drawable.stat_sys_download);
        builderProgress.setTicker("进度条通知");
        builderProgress.setProgress(100, 0, false);
        Notification notification = builderProgress.build();
        notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        //发送一个通知
        notificationManager.notify(NOTIFICATIONID, notification);

        //开启广播去监听 网络 改变事件
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        NetStateReceiver.addObserver(mNetChangeObserver);
    }

    /**
     * 设置下载的url
     *
     * @param url
     * @return
     */
    public ShowProgressFileDownload downloadUrl(String url) {
        this.mDownloadUrl = url;
        return this;
    }

    /**
     * 设置apk 文件路径
     * @param targetFilePath
     * @return
     */
    public ShowProgressFileDownload targetFilePath(String targetFilePath)
    {
        this.mTargetFilePath = targetFilePath;
        return this;
    }

    /**
     * 是否wifi 请求下载
     * @param isWifiRequired
     * @return
     */
    public ShowProgressFileDownload isWifiRequired(boolean isWifiRequired)
    {
        this.isWifiRequired = isWifiRequired;
        return this;
    }


    public static ShowProgressFileDownload with(Activity activity)
    {
        return new ShowProgressFileDownload(activity);
    }

    /**
     * 开始下载
     */
    public void startDownload()
    {
        FileDownloader.getImpl().
                create(mDownloadUrl).
                setPath(mTargetFilePath).
                setAutoRetryTimes(3).
                setWifiRequired(isWifiRequired).//是否在wifi下载
                setCallbackProgressTimes(CALLBACKPROGRESSTIMES).
                setMinIntervalUpdateSpeed(MININTERVALUPDATESPEED).setListener(this).start();
    }


    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        //等待，已经进入下载队列	数据库中的soFarBytes与totalBytes
        Log.d(TAG, task.getFilename() + "等待，已经进入下载队列");
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        int progress = (int) ((float) soFarBytes / totalBytes * 100);
        //更新进度条
        builderProgress.setProgress(100, progress, false);
        //再次通知
        notificationManager.notify(NOTIFICATIONID, builderProgress.build());
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        Log.d(TAG, task.getFilename() + "下载完成");
        //进度条退出
        notificationManager.cancel(NOTIFICATIONID);
        showTipDialog(mContext);
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        Log.d(TAG, task.getFilename() + "下载暂停Speed:" + task.getSpeed());
        //下载暂停
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        //下载出现错误 java.net.UnknownHostException
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            Toast.makeText(mContext, "网络已经断开", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void warn(BaseDownloadTask task) {
        //  在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
       // Log.d(TAG, task.getFilename() + "已经下载字节:" + task.getSoFarBytes());
    }

    private void showTipDialog(final Activity mContext) {
        if (dialog!=null)//不知道为什么；网络监听会回调2次？
            dialog.cancel();
        dialog = new CustomDialog(mContext);
        dialog.setTitles("温馨提示").setMessage("新的版本已经下载好，是否需要更新？");
        dialog.setPositiveButton(mContext.getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转下载安装的界面
                Utils.installAPK(mContext, mTargetFilePath);
            }
        });
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.setNegativeButton(mContext.getString(R.string.cancel), null);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    /**
     * 网络观察者
     */
    protected final NetChangeObserver mNetChangeObserver = new NetChangeObserver() {
        @Override
        public void onNetConnected(NetUtils.NetType type) {//网络连接
            if (type == NetUtils.NetType.WIFI) {

            } else if (type == NetUtils.NetType.TYPE_MOBILE) {

            }
            if (dialog!=null)//不知道为什么；网络监听会回调2次？
                dialog.cancel();

        }

        @Override
        public void onNetDisConnect() {//网络已经断开
            if (dialog!=null)//不知道为什么；网络监听会回调2次？
                dialog.cancel();
        }
    };


}
