package com.eeepay.cn.zzq.demo.updateapp.network;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eeepay.cn.zzq.demo.updateapp.R;
import com.eeepay.cn.zzq.demo.updateapp.utils.Utils;
import com.eeepay.cn.zzq.demo.updateapp.view.CustomDialog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.net.UnknownHostException;

/**
 * 描述：显示下载进度弹框的dialog 比如强制升级
 * 作者：zhuangzeqin
 * 时间: 2017/9/8-14:50
 * 邮箱：zzq@eeepay.cn
 */
public class ShowDialogFileDownload extends FileDownloadListener {
    private final String TAG = ShowDialogFileDownload.class.getSimpleName();
    private final int MININTERVALUPDATESPEED = 400;//最小时间间隔更新下载速度

    private final int CALLBACKPROGRESSTIMES = 300;//回调进度时间

    private Activity mContext;//上下文对象

    private String mDownloadUrl;//下载文件路径

    private String mTargetFilePath;//apk 文件路径

    private boolean isWifiRequired = false;//是否wifi 请求下载

    private boolean isForce = false; //是否强制更新 默认不强制升级

    private ProgressBar mProgress;
    private TextView mPercent;
    private CustomDialog dialog;
    private int mDownloadId = 0;//下载id

    private ShowDialogFileDownload(Activity activity) {
        this(activity, false);
    }

    private ShowDialogFileDownload(Activity activity, boolean isForce) {
        this.mContext = activity;
        this.isForce = isForce;
        isForceShowDialog();//初始化显示弹框
    }

    public static ShowDialogFileDownload with(Activity activity, boolean isForce) {
        return new ShowDialogFileDownload(activity,isForce);
    }

    public static ShowDialogFileDownload with(Activity activity) {
        return new ShowDialogFileDownload(activity);
    }

    public ShowDialogFileDownload downloadUrl(String url) {
        this.mDownloadUrl = url;
        return this;
    }

    /**
     * 设置apk 文件路径
     *
     * @param targetFilePath
     * @return
     */
    public ShowDialogFileDownload targetFilePath(String targetFilePath) {
        this.mTargetFilePath = targetFilePath;
        return this;
    }

    /**
     * 是否wifi 请求下载
     *
     * @param isWifiRequired
     * @return
     */
    public ShowDialogFileDownload isWifiRequired(boolean isWifiRequired) {
        this.isWifiRequired = isWifiRequired;
        return this;
    }

    public ShowDialogFileDownload isForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }

    /**
     * 暂停downloadId的任务
     */
    private void paused() {
        FileDownloader.getImpl().pause(mDownloadId);
    }

    /**
     * 开始下载
     */
    public void startDownload() {
        mDownloadId = FileDownloader.getImpl().
                create(mDownloadUrl).
                setPath(mTargetFilePath).
                setAutoRetryTimes(3).
                setWifiRequired(isWifiRequired).//是否在wifi下载
                setCallbackProgressTimes(CALLBACKPROGRESSTIMES).
                setMinIntervalUpdateSpeed(MININTERVALUPDATESPEED).setListener(this).start();
    }

    private void isForceShowDialog() {
//        if (dialog!=null)//不知道为什么；网络监听会回调2次？
//            dialog.cancel();
        View view = View.inflate(mContext, R.layout.app_update, null);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mPercent = (TextView) view.findViewById(R.id.tv_progress);
        dialog = new CustomDialog(mContext);
        dialog.setTitles("发现新版本").setView(view);
        if (isForce) {//强制升级
            dialog.setCancelable(false);
        } else {
            dialog.setNegativeButton(mContext.getResources().getString(R.string.cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    paused(); //取消下载；断点
                }
            });
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        //等待，已经进入下载队列	数据库中的soFarBytes与totalBytes
        Log.d(TAG, task.getFilename() + "等待，已经进入下载队列");
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        int progress = (int) ((float) soFarBytes / totalBytes * 100);
        if (totalBytes == -1) {
            // chunked transfer encoding data
            mProgress.setIndeterminate(true);
        } else {
            mProgress.setMax(100);
            mProgress.setProgress(progress);
            mPercent.setText(mProgress.getProgress() + "%");
        }
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        mProgress.setIndeterminate(false);
        //下载完成
        dialog.cancel();
        //跳转下载安装的界面
        Utils.installAPK(mContext, mTargetFilePath);
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

    }
}
