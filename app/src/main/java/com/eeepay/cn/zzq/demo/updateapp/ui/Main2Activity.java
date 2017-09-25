package com.eeepay.cn.zzq.demo.updateapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.eeepay.cn.zzq.demo.updateapp.R;
import com.vise.log.ViseLog;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ViseLog.d("Main2Activity", "test messag");//普通消息
        //打印xml
        String xml = "<xyy><test1><test2>key</test2></test1><test3>name</test3><test4>value</test4></xyy>";
        ViseLog.xml(xml);
        //打印json
        String json = "{'xyy1':[{'test1':'test1'},{'test2':'test2'}],'xyy2':{'test3':'test3','test4':'test4'}}";
        ViseLog.json(json);
        // 打印HashMap
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put("xyy" + i, "test" + i);
        }
        ViseLog.d(map);
        // 打印List
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("test" + i);
        }
        ViseLog.d(list);

        // 打印Throwable对象
        ViseLog.e(new NullPointerException("this object is null!"));
        //打印Reference对象
        ViseLog.d(new SoftReference(15));
        //打印Intent对象
        ViseLog.d(new Intent());
        //打印Bundle对象
        Bundle bundle = new Bundle();
        bundle.putString("test","message");
        ViseLog.d(bundle);
        //打印基本对象
        ViseLog.d(new Boolean(true));





    }
}
