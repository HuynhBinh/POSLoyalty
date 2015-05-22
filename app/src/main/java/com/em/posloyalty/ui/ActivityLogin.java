package com.em.posloyalty.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.em.posloyalty.R;
import com.em.posloyalty.consts.APIConst;
import com.em.posloyalty.consts.Consts;
import com.em.posloyalty.daocontrol.GreedDaoController;
import com.em.posloyalty.service.APIService;
import com.em.posloyalty.simpletoast.SimpleToast;

import greendao.Customer;

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
                if (result.equalsIgnoreCase(APIConst.RESULT_OK))
                {
                    // pre-load the data of voucher so that user can immidiately see the vouchers after login, no need to wait
                    Customer customer = GreedDaoController.getCustomerByID(ActivityLogin.this, 1);

                    //load active voucher update
                    Intent intent2 = new Intent(APIConst.ACTION_LOAD_ACTIVE_VOUCHER, null, ActivityLogin.this, APIService.class);
                    intent2.putExtra(APIConst.EXTRA_CUSTOMER_ID, customer.getCustomerID());
                    startService(intent2);

                    // start main activity
                    Intent intent1 = new Intent(ActivityLogin.this, ActivityMain.class);
                    startActivity(intent1);
                    finish();

                } else if (result.equalsIgnoreCase(APIConst.RESULT_NO_INTERNET))
                {

                    Customer customer = GreedDaoController.getCustomerByID(ActivityLogin.this, 1);
                    if (customer != null)
                    {
                        Intent intent1 = new Intent(ActivityLogin.this, ActivityMain.class);
                        startActivity(intent1);
                        finish();
                    } else
                    {

                        layoutFlash.setVisibility(View.GONE);
                        layoutLogin.setVisibility(View.VISIBLE);

                        btnLogin.setText("LOG IN");
                        btnLogin.setEnabled(true);
                        SimpleToast.error(ActivityLogin.this, "Please check internet connection!");
                    }
                } else
                {

                    layoutFlash.setVisibility(View.GONE);
                    layoutLogin.setVisibility(View.VISIBLE);

                    btnLogin.setText("LOG IN");
                    btnLogin.setEnabled(true);
                    SimpleToast.error(ActivityLogin.this, "Incorrect username, password!");
                }
            } else if (intent.getAction().equalsIgnoreCase(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER))
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.e("GO", "Go here");
                        APIConst.isLoadingActiveVoucher = false;
                    }
                }, Consts.LEAST_REFRESH_TIME);

            }
        }
    };


    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;

    ScrollView layoutLogin;
    LinearLayout layoutFlash;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        registerButtonLoginClick();
        registerBroadcastReceiver();


        Customer customer = GreedDaoController.getCustomerByID(this, 1);
        if (customer != null)
        {
            layoutFlash.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            login();
        } else
        {
            layoutFlash.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
        }


    }

    private void initView()
    {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPass);

        layoutFlash = (LinearLayout) findViewById(R.id.layoutFlash);
        layoutLogin = (ScrollView) findViewById(R.id.layoutLogin);

        progressBar = (ProgressBar) findViewById(R.id.progressBarFlashscreen);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFf18910, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void registerButtonLoginClick()
    {
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                btnLogin.setText("Logging in ...");
                btnLogin.setEnabled(false);

                layoutFlash.setVisibility(View.VISIBLE);
                layoutLogin.setVisibility(View.GONE);

                login();
            }
        });
    }


    public void login()
    {
        String customerCode = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Intent intent = new Intent(APIConst.ACTION_LOGIN, null, ActivityLogin.this, APIService.class);
        intent.putExtra(APIConst.EXTRA_USERNAME, customerCode);
        intent.putExtra(APIConst.EXTRA_PASSWORD, password);
        startService(intent);
    }


    private void registerBroadcastReceiver()
    {
        if (activityReceiver != null)
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(APIConst.RECEIVER_FINISH_LOGIN);
            intentFilter.addAction(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER);
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
