package com.share.jack.numberlockdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.share.jack.numberlockdemo.util.Consts;

/**
 * Created by Jack on 16/10/10.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doLogin(View v) {
        switch (v.getId()) {
            case R.id.am_btn_lock1:
                startActivity(new Intent(this, Lock1Activity.class).putExtra("type", Consts.SETTING_PASSWORD));
                break;
            case R.id.am_btn_lock2:
                startActivity(new Intent(this, Lock2Activity.class).putExtra("type", Consts.SETTING_PASSWORD));
                break;
        }
    }
}
