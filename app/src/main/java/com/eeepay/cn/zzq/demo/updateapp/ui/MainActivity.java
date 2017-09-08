package com.eeepay.cn.zzq.demo.updateapp.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eeepay.cn.zzq.demo.updateapp.R;
import com.eeepay.cn.zzq.demo.updateapp.network.UpdateAppBuilder;
import com.eeepay.cn.zzq.demo.updateapp.utils.NetUtils;

/**
 * 描述：全局的网络监听
 * 作者：zhuangzeqin
 * 时间: 2017/9/5-14:59
 * 邮箱：zzq@eeepay.cn
 */
public class MainActivity extends BaseActivity {
    String url = "http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        Button btn_start = (Button) findViewById(R.id.btn_start);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                finish();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.0.1
                UpdateAppBuilder.with(MainActivity.this).
                        apkPath(url).//apk 的下载路径
                        isForce(false).//是否需要强制升级
                        serverVersionName("1.0.1")//服务的版本(会与当前应用的版本号进行比较)
                        .updateInfo("有新的版本发布啦！赶紧下载体验")//升级版本信息
                        .start();//开始下载
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        if (type == NetUtils.NetType.WIFI)
            Toast.makeText(mContext, "wifi网络已连接", Toast.LENGTH_SHORT).show();
        else if (type == NetUtils.NetType.TYPE_MOBILE)
            Toast.makeText(mContext, "移动网络已连接", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNetworkDisConnected() {
        Toast.makeText(mContext, "网络已经断开", Toast.LENGTH_SHORT).show();
    }
}
