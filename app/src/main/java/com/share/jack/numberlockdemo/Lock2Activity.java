package com.share.jack.numberlockdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.share.jack.numberlockdemo.util.Consts;
import com.share.jack.numberlockdemo.util.MyPrefs;
import com.share.jack.numberlockdemo.widget.MyPasswordTextView;
import com.share.jack.numberlockdemo.widget.NumericKeyboard;

/**
 * Created by Jack on 16/10/10.
 */
public class Lock2Activity extends Activity implements View.OnClickListener {

    private TextView mTvDelete;
    private TextView mTvForgetPwd;
    // 数字键盘布局
    private NumericKeyboard nk;
    // 密码框
    private MyPasswordTextView et_pwd1, et_pwd2, et_pwd3, et_pwd4;
    private int type;
    private TextView tv_info;//提示信息
    //声明字符串保存每一次输入的密码
    private String input;
    private StringBuffer fBuffer = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock2);
        initView();
        initListener();// 事件处理
        //获取界面传递的值
        type = getIntent().getIntExtra("type", 1);
    }

    private void initView() {
        nk = (NumericKeyboard) findViewById(R.id.nk);// 数字键盘
        // 密码框
        et_pwd1 = (MyPasswordTextView) findViewById(R.id.et_pwd1);
        et_pwd2 = (MyPasswordTextView) findViewById(R.id.et_pwd2);
        et_pwd3 = (MyPasswordTextView) findViewById(R.id.et_pwd3);
        et_pwd4 = (MyPasswordTextView) findViewById(R.id.et_pwd4);
        tv_info = (TextView) findViewById(R.id.tv_info);//提示信息
        mTvDelete = (TextView) findViewById(R.id.tv_delete);
        mTvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
    }

    /**
     * 事件处理
     */
    private void initListener() {
        // 设置点击的按钮回调事件
        nk.setOnNumberClick(new NumericKeyboard.OnNumberClick() {
            @Override
            public void onNumberReturn(int number) {
                // 设置显示密码
                setText(number + "");
            }
        });
        et_pwd1.setOnMyTextChangedListener(new MyPasswordTextView.OnMyTextChangedListener() {
            @Override
            public void textChanged(String content) {
                if (TextUtils.isEmpty(content)) {
                    mTvDelete.setVisibility(View.GONE);
                } else {
                    mTvDelete.setVisibility(View.VISIBLE);
                }
            }
        });
        //监听最后一个密码框的文本改变事件回调
        et_pwd4.setOnMyTextChangedListener(new MyPasswordTextView.OnMyTextChangedListener() {
            @Override
            public void textChanged(String content) {
                input = et_pwd1.getTextContent() + et_pwd2.getTextContent() +
                        et_pwd3.getTextContent() + et_pwd4.getTextContent();
                //判断类型
                if (type == Consts.SETTING_PASSWORD) {//设置密码
                    //重新输入密码
                    tv_info.setText(getString(R.string.please_input_pwd_again));
                    type = Consts.SURE_SETTING_PASSWORD;
                    fBuffer.append(input);//保存第一次输入的密码
                } else if (type == Consts.LOGIN_PASSWORD) {//登录
                    if (!input.equals(MyPrefs.getInstance().readString("password"))) {
                        showToastMsg("密码错误,请重新输入");
                    } else {
                        showToastMsg("密码正确,欢迎登录");
                        startActivity(new Intent(Lock2Activity.this, LuckyActivity.class));
                        finish();
                    }
                } else if (type == Consts.SURE_SETTING_PASSWORD) {//确认密码
                    //判断两次输入的密码是否一致
                    if (input.equals(fBuffer.toString())) {//一致
                        showToastMsg(getString(R.string.setting_pwd_success));
                        //保存密码到文件中
                        MyPrefs.getInstance().initSharedPreferences(Lock2Activity.this);
                        MyPrefs.getInstance().writeString("password", input);
                        type = Consts.LOGIN_PASSWORD;
                        tv_info.setText("你可以登录了");
                    } else {//不一致
                        showToastMsg(getString(R.string.not_equals));
                    }
                }
                startTimer();
            }
        });
        mTvDelete.setOnClickListener(this);
        mTvForgetPwd.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            clearText();
        }
    };

    private void startTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 设置显示的密码
     *
     * @param text
     */
    private void setText(String text) {
        // 从左往右依次显示
        if (TextUtils.isEmpty(et_pwd1.getTextContent())) {
            et_pwd1.setTextContent(text);
        } else if (TextUtils.isEmpty(et_pwd2.getTextContent())) {
            et_pwd2.setTextContent(text);
        } else if (TextUtils.isEmpty(et_pwd3.getTextContent())) {
            et_pwd3.setTextContent(text);
        } else if (TextUtils.isEmpty(et_pwd4.getTextContent())) {
            et_pwd4.setTextContent(text);
        }
    }

    /**
     * 清除输入的内容--重输
     */
    private void clearText() {
        et_pwd1.setTextContent("");
        et_pwd2.setTextContent("");
        et_pwd3.setTextContent("");
        et_pwd4.setTextContent("");
        mTvDelete.setVisibility(View.GONE);
    }

    /**
     * 删除刚刚输入的内容
     */
    private void deleteText() {
        // 从右往左依次删除
        if (!TextUtils.isEmpty(et_pwd4.getTextContent())) {
            et_pwd4.setTextContent("");
        } else if (!TextUtils.isEmpty(et_pwd3.getTextContent())) {
            et_pwd3.setTextContent("");
        } else if (!TextUtils.isEmpty(et_pwd2.getTextContent())) {
            et_pwd2.setTextContent("");
        } else if (!TextUtils.isEmpty(et_pwd1.getTextContent())) {
            et_pwd1.setTextContent("");
            mTvDelete.setVisibility(View.GONE);
        }
    }

    /**
     * 显示Toast提示信息
     *
     * @param text
     */
    private void showToastMsg(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        //判断点击的按钮
        switch (view.getId()) {
            case R.id.tv_forget_pwd://忘记密码?
                finish();
                showToastMsg("快去退出登录取消锁屏吧");
                break;
            case R.id.tv_delete://删除
                deleteText();//删除刚刚输入的内容
                break;
        }
    }
}
