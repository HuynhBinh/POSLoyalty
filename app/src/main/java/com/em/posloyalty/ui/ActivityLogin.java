package com.em.posloyalty.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.em.posloyalty.R;
import com.em.posloyalty.consts.APIConst;
import com.em.posloyalty.service.APIService;

/**
 * Created by USER on 5/12/2015.
 */
public class ActivityLogin extends Activity
{

    private BroadcastReceiver activityReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase(APIConst.RECEIVER_FINISH_LOGIN))
            {
                String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                Intent intent1 = new Intent(ActivityLogin.this, ActivityMain.class);
                startActivity(intent1);
                finish();
            }
        }
    };


    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        registerButtonLoginClick();
        registerBroadcastReceiver();


    }

    private void initView()
    {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPass);
    }

    private void registerButtonLoginClick()
    {
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(APIConst.ACTION_LOGIN, null, ActivityLogin.this, APIService.class);
                intent.putExtra(APIConst.EXTRA_USERNAME, "HNB");
                intent.putExtra(APIConst.EXTRA_PASSWORD, "HNB");
                startService(intent);
            }
        });
    }


    private void registerBroadcastReceiver()
    {
        if (activityReceiver != null)
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(APIConst.RECEIVER_FINISH_LOGIN);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterBroadcastReceiver()
    {
        if (activityReceiver != null)
        {
            unregisterReceiver(activityReceiver);
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unRegisterBroadcastReceiver();
    }
}
