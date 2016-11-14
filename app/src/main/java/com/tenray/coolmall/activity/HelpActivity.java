package com.tenray.coolmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tenray.coolmall.R;

/**
 * Created by en on 2016/11/11.
 */

public class HelpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void onClickBack(View v){
        startActivity(new Intent(this,GoodsActivity.class));
    }
}
