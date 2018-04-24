package com.imovie.mogic.mine.attend.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.imovie.mogic.R;

/**
 * Created by Jimmy on 2016/10/14 0014.
 */
public class AttendDialog extends Dialog implements View.OnClickListener{


    private TextView tvTitle;
    private TextView tvContent;
    public interface onSelectListener {
        void onSelect(String type);
    }
    private onSelectListener listener;
    public void setOnSelectListener(onSelectListener listener) {
        this.listener = listener;
    }

//    private EditText etTime;

    public AttendDialog(Context context) {
        super(context, R.style.DialogFullScreen);
        initView();

    }

    private void initView() {
        setContentView(R.layout.dialog_attend);
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setContent(String content){
        tvContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvConfirm:
                listener.onSelect("1");
                dismiss();
                break;
        }
    }

}
