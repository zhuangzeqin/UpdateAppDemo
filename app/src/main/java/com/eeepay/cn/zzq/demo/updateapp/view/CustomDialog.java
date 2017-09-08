package com.eeepay.cn.zzq.demo.updateapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eeepay.cn.zzq.demo.updateapp.utils.ABPixelUtil;
import com.eeepay.cn.zzq.demo.updateapp.utils.Constants;

import com.eeepay.cn.zzq.demo.updateapp.R;

public class CustomDialog extends Dialog {
    private View _view;
    private TextView title, message, tv_cancel, tv_ok, loading,
            Verticalloading;
    private LinearLayout messageGroup, btnGroup, vertical_loadGroup,
            horizontal_loadGroup;
    private Context mContext = null;
    private int mode;


    public CustomDialog(Context context) {
        this(context, Constants.Dialog_General);
    }

    public CustomDialog(Context context, int mode) {
        this(context, R.style.dialog_custom_style, mode);
    }

    public CustomDialog(Context context, int theme, int mode) {
        super(context, theme);
        mContext = context;
        this.mode = mode;
        _view = LayoutInflater.from(context).inflate(
                R.layout.dialog_custom_layout, null);
        title = (TextView) _view.findViewById(R.id.title);
        message = (TextView) _view.findViewById(R.id.message);
        tv_cancel = (TextView) _view.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) _view.findViewById(R.id.tv_ok);
        loading = (TextView) _view.findViewById(R.id.loading);
        messageGroup = (LinearLayout) _view.findViewById(R.id.messageGroup);
        horizontal_loadGroup = (LinearLayout) _view
                .findViewById(R.id.horizontal_loadGroup);
        btnGroup = (LinearLayout) _view.findViewById(R.id.btnGroup);

        vertical_loadGroup = (LinearLayout) _view
                .findViewById(R.id.vertical_loadGroup);
        Verticalloading = (TextView) _view.findViewById(R.id.Verticalloading);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(_view);
        // setCancelable(false);//屏蔽返回键
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        DisplayMetrics metric = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        switch (mode) {
            case Constants.Dialog_General:
                lp.width = (int) (0.7f * width);
                break;
            case Constants.Dialog_HorizontalProgress:
                lp.width = (int) (0.6f * width);
                break;
            case Constants.Dialog_VerticalProgress:
                lp.width = ABPixelUtil.dp2px(110, this.getContext());
                break;
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 不调用此方法不显示此按钮
     *
     * @param text     设置"确定"按钮文字
     * @param listener 设置点击事件
     * @return
     */
    public CustomDialog setPositiveButton(CharSequence text,
                                          final View.OnClickListener listener) {
        tv_ok.setVisibility(View.VISIBLE);
        tv_ok.setText(text);
        tv_ok.setTag(this);
        showView();
        tv_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
        return this;
    }

    /**
     * 不调用此方法不显示此按钮
     *
     * @param text     设置"取消"按钮文字
     * @param listener 设置点击事件
     * @return
     */
    public CustomDialog setNegativeButton(CharSequence text,
                                          final View.OnClickListener listener) {
        tv_cancel.setVisibility(View.VISIBLE);
        tv_cancel.setText(text);
        tv_cancel.setTag(this);
        showView();
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
        return this;
    }

    private void showView() {
        btnGroup.setVisibility(View.VISIBLE);
        if (tv_cancel.getVisibility() == View.VISIBLE
                && tv_ok.getVisibility() == View.VISIBLE) {
//			tv_cancel
//					.setBackgroundResource(R.drawable.dialog_btn_select_left_bg);
//			tv_ok.setBackgroundResource(R.drawable.dialog_btn_select_right_bg);
        } else {
        }
    }

    /**
     * @param view 设置自定义view
     * @return
     */
    public CustomDialog setView(View view) {
        message.setVisibility(View.GONE);
        messageGroup.setVisibility(View.VISIBLE);
        messageGroup.addView(view);
        return this;
    }

    /**
     * @param text 设置对话框标题
     * @return
     */
    public CustomDialog setTitles(CharSequence text) {
        title.setText(text);
        title.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * @param text
     * @param gravity ,文字对齐方式 设置对话框标题
     * @return
     */
    public CustomDialog setTitles(CharSequence text, int gravity) {
        title.setText(text);
        title.setGravity(gravity);
        title.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * @param text 设置对话框消息
     * @return
     */
    public CustomDialog setMessage(CharSequence text) {
        messageGroup.setVisibility(View.VISIBLE);
        message.setVisibility(View.VISIBLE);
        message.setText(text);
        return this;
    }

    /**
     * 简单竖向进度对话框（只有加载菊花和等待文字提示）
     *
     * @param msg 加载进度文字提示
     * @return
     */
    public CustomDialog setVerticalProgress(CharSequence msg) {
        vertical_loadGroup.setVisibility(View.VISIBLE);
        Verticalloading.setText(msg);
        return this;
    }

    public CustomDialog setVerticalProgress() {
        vertical_loadGroup.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomDialog setHorizontalProgress(CharSequence text) {
        horizontal_loadGroup.setVisibility(View.VISIBLE);
        loading.setText(text);
        return this;
    }

    public CustomDialog setHorizontalProgress() {
        horizontal_loadGroup.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * @param item     //菜单项字符数组
     * @param listener //监听器
     * @return
     */
    public CustomDialog setItems(final CharSequence[] item,
                                 final CustomDialogItemOnClick listener) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp_View = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, ABPixelUtil.dp2px(1, this.getContext()));
        LinearLayout.LayoutParams lp_TextView = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, ABPixelUtil.dp2px(50, this.getContext()));
        TextView textView;
        View line;
        for (int i = 0; i < item.length; i++) {
            final int postion = i;
            textView = new TextView(mContext);
            textView.setBackgroundResource(R.drawable.dialog_btn_select_bg);
            textView.setText(item[i]);
            textView.setId(i);
            textView.setTextSize(15);
            textView.setTextColor(mContext.getResources().getColor(
                    R.color.dialog_listview_item_textColor));
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemOnClick(postion, item[postion]);
                    }
                    dismiss();
                }
            });
            linearLayout.addView(textView, lp_TextView);
            if (i != (item.length - 1)) {
                line = new View(mContext);
                line.setBackgroundColor(mContext.getResources().getColor(
                        R.color.dialog_line_bg_Color));
                linearLayout.addView(line, lp_View);
            }

        }
        setView(linearLayout);
        return this;
    }

    public interface CustomDialogItemOnClick {
        void onItemOnClick(int postion, CharSequence value);
    }

}
