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
        startActivity(new Intent(this, LockActivity.class).putExtra("type", Consts.SETTING_PASSWORD));
    }
}
