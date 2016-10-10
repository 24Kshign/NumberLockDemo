package com.share.jack.numberlockdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Jack on 16/10/10.
 */
public class PasswordTextView extends TextView{
    private final String sing = "*";//密文显示的内容
    private String content = "";//显示的内容
    //文本改变事件回调接口
    private OnTextChangedListener onTextChangedListener;
    /**
     * Handler线程对象,用来更新密码框的显示内容
     * 实现将输入的内容显示为密文
     */
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //密文显示
            PasswordTextView.this.setText(sing);
            //回调改变事件接口
            if(onTextChangedListener != null){
                onTextChangedListener.textChanged(content);
            }
        };
    };

    /**
     * 构造方法
     * @param context
     * @param attrs
     */
    public PasswordTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置文本改变事件监听
     * @param onTextChangedListener
     */
    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener){
        this.onTextChangedListener = onTextChangedListener;
    }

    /**
     * 设置密码框显示的内容
     * @param text
     */
    public void setTextContent(String text){
        //获得输入的内容
        this.content = text;
        if(!TextUtils.isEmpty(text)){
            handler.sendEmptyMessage(0);//向Handler发送消息
        }else{
            this.setText("");
        }
    }

    /**
     * 获取显示的内容
     * @return
     */
    public String getTextContent(){
        return content;
    }

    /**
     * 文本改变事件接口
     * @ClassName: OnTextChangedListener
     * @author haoran.shu
     * @date 2014年6月12日 上午11:37:17
     * @version 1.0
     *
     */
    public interface OnTextChangedListener{
        /**
         * 密码框文本改变时调用
         * @param content
         */
        public void textChanged(String content);
    }

}