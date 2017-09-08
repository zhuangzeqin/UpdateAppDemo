package com.eeepay.cn.zzq.demo.updateapp.network;

import android.app.Activity;

import com.eeepay.cn.zzq.demo.updateapp.utils.Utils;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * 描述：下载文件管理类
 * 作者：zhuangzeqin
 * 时间: 2017/9/6-16:08
 * 邮箱：zzq@eeepay.cn
 */
public class DownloadManager {
    private final String TAG = DownloadManager.class.getSimpleName();

    private String mDownloadUrl;//下载文件路径

    private String mSavePath;//保存路径

    private Activity mContext;//上下文对象

    private int mDownloadId = 0;//下载id

    private boolean isForce = false; //是否强制更新 默认不强制升级

    private String mTargetFilePath;//apk 文件路径

    private boolean isWifiRequired = false;//是否wifi 请求下载

    private DownloadManager(Activity context) {
        this.mContext = context;
        this.mSavePath = Utils.getCachePath(context) + File.separator;//默认的保存路径
        FileDownloader.getImpl().setup(context);
    }


    public static DownloadManager with(Activity context) {
        return new DownloadManager(context);
    }

    /**
     * 设置下载的url
     *
     * @param url
     * @return
     */
    public DownloadManager downloadUrl(String url) {
        this.mDownloadUrl = url;
        return this;
    }

    /**
     * 是否强制升级
     *
     * @param isForce true 为强制升级； false 不强制升级
     * @return
     */
    public DownloadManager isForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }

    /**
     * 设置是否wifi请求下载
     *
     * @param isWifiRequired
     * @return
     */
    public DownloadManager isWifiRequired(boolean isWifiRequired) {
        this.isWifiRequired = isWifiRequired;
        return this;
    }

    /**
     * 暂停downloadId的任务
     */
    private void paused() {
        FileDownloader.getImpl().pause(mDownloadId);
    }

    /**
     * 强制清理ID为downloadId的任务在filedownloader中的数据
     */
    private void delete(String targetFilePath) {
        FileDownloader.getImpl().clear(mDownloadId, targetFilePath);
    }

    /**
     * 开始下载
     * 启动单任务下载
     */
    public int startDownload() {
        //开始下载
        final String fileName = mDownloadUrl.substring(mDownloadUrl.lastIndexOf("/") + 1);
        final File file = new File(mSavePath, fileName);
        mTargetFilePath = file.getPath();//apk 文件路径
        if (isForce)
            //开始下载文件
            ShowDialogFileDownload.with(mContext, isForce).isWifiRequired(isWifiRequired).downloadUrl(mDownloadUrl).targetFilePath(mTargetFilePath).startDownload();
        else//显示一个下载带进度条的通知
            ShowProgressFileDownload.with(mContext).isWifiRequired(isWifiRequired).downloadUrl(mDownloadUrl).targetFilePath(mTargetFilePath).startDownload();
        return mDownloadId;
    }
}
