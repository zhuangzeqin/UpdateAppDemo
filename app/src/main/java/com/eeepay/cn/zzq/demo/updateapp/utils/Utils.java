package com.eeepay.cn.zzq.demo.updateapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 描述：class describe
 * 作者：zhuangzeqin
 * 时间: 2017/9/6-16:13
 * 邮箱：zzq@eeepay.cn
 */
public class Utils {
    /**
     * 获取缓存存放路径
     *
     * @return
     */
    public static String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            /**获取缓存路径:/sdcard/Android/data/<application package>/cache  */
            cachePath = context.getExternalCacheDir().getPath() + File.separator + "APP#CacheDir";
        } else {
            /**获取缓存路径:/data/data/<application package>/cache   */
            cachePath = context.getCacheDir().getPath() + File.separator + "APP#CacheDir";
        }
        return cachePath;
    }

    /**
     *   7.0 的 Intent 离开你的应用，应用失败，并出现 FileUriExposedException 异常。
     * @param context
     * @param apkPath
     */
    public static void installAPK(Context context,String apkPath)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        File file = (new File(apkPath));
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.eeepay.cn.zzq.demo.updateapp.installapkAPK", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
    /**
     * 通过递归调用删除一个文件夹及下面的所有文件
     *
     * @param file
     */
    /**
     * 删除某目录下所有文件包括子文件夹
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }
}
