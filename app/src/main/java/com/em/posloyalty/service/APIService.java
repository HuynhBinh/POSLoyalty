package com.em.posloyalty.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.em.posloyalty.consts.APIConst;
import com.em.posloyalty.consts.StaticFunc;
import com.em.posloyalty.daocontrol.GreedDaoController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import greendao.Voucher;


/**
 * Created by USER on 3/24/2015.
 */
public class APIService extends IntentService
{

    public APIService()
    {
        super(APIService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String action = intent.getAction();

        // if command load -> load data
        if (action.equals(APIConst.ACTION_LOGIN))
        {
            Log.e("onHandleIntent", "ACTION_LOGIN");

            String username = intent.getStringExtra(APIConst.EXTRA_USERNAME);
            String password = intent.getStringExtra(APIConst.EXTRA_PASSWORD);

            if (StaticFunc.isNetworkAvailable(APIService.this))
            {
                boolean isSuccess = true;//APIWrapper.login(username, password);
                if (isSuccess)
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOGIN, APIConst.RESULT_OK, "-1");

                } else
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOGIN, APIConst.RESULT_FAIL, "-1");
                }

            } else
            {
                sendBroadCastResult(APIConst.RECEIVER_FINISH_LOGIN, APIConst.RESULT_NO_INTERNET, "-1");
            }

        }
        // if command save -> upload data to server
        else if (action.equals(APIConst.ACTION_LOAD_ACTIVE_VOUCHER))
        {
            String username = intent.getStringExtra(APIConst.EXTRA_USERNAME);

            Log.e("onHandleIntent", "ACTION_LOAD_ACTIVE_VOUCHER");

            if (StaticFunc.isNetworkAvailable(APIService.this))
            {
                boolean isSuccess = true;//APIWrapper.getActiveVouchers();
                if (isSuccess)
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_OK, "-1");

                } else
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_FAIL, "-1");
                }

            } else
            {
                sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_NO_INTERNET, "-1");
            }
        } else if (action.equals(APIConst.ACTION_LOAD_USED_VOUCHER))
        {
            Log.e("onHandleIntent", "ACTION_LOAD_USED_VOUCHER");
            if (StaticFunc.isNetworkAvailable(APIService.this))
            {

                boolean isSuccess = false;//login(username, password);
                if (isSuccess)
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_OK, "-1");
                } else
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_FAIL, "-1");
                }

            } else
            {
                sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_NO_INTERNET, "-1");
            }

        } else if (action.equals(APIConst.ACTION_GEN_VOUCHER))
        {
            Log.e("onHandleIntent", "ACTION_GEN_VOUCHER");

            String strAmount = intent.getStringExtra(APIConst.EXTRA_AMOUNT);


            if (StaticFunc.isNetworkAvailable(APIService.this))
            {

                boolean isSuccess = true;//signup(fullname, password, email, phone);
                if (isSuccess)
                {

                    Long time = System.currentTimeMillis();

                    String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                    Voucher voucher = new Voucher();
                    voucher.setVoucherCode(time.toString());
                    voucher.setVoucherAmount(Double.parseDouble(strAmount));
                    voucher.setIsApplied(false);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                    String strDate = sdf.format(c.getTime());
                    voucher.setVoucherGeneratedTime(strDate);

                    GreedDaoController.insertVoucher(APIService.this, voucher);

                    sendBroadCastResult(APIConst.RECEIVER_FINISH_GEN_VOUCHER, APIConst.RESULT_OK, "-1");
                } else
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_GEN_VOUCHER, APIConst.RESULT_FAIL, "-1");
                }
            } else
            {
                sendBroadCastResult(APIConst.RECEIVER_FINISH_GEN_VOUCHER, APIConst.RESULT_NO_INTERNET, "-1");
            }
        } else if (action.equals(APIConst.ACTION_APPLY_VOUCHER))
        {
            Log.e("onHandleIntent", "ACTION_APPLY_VOUCHER");
            
            String voucherCode = intent.getStringExtra(APIConst.EXTRA_VC_ID);

            if (StaticFunc.isNetworkAvailable(APIService.this))
            {


                boolean isSuccess = true;//loadTransactionForTheMonthForNewLoginUser(userid, month);

                if (isSuccess)
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_APPLY_VOUCHER, APIConst.RESULT_OK, voucherCode);
                } else
                {
                    sendBroadCastResult(APIConst.RECEIVER_FINISH_APPLY_VOUCHER, APIConst.RESULT_FAIL, voucherCode);
                }
            } else
            {
                sendBroadCastResult(APIConst.RECEIVER_FINISH_APPLY_VOUCHER, APIConst.RESULT_NO_INTERNET, voucherCode);
            }
        }
    }


    private void sendBroadCastResult(String receiver, String result, String id)
    {
        Intent intent = new Intent();
        intent.setAction(receiver);
        intent.putExtra(APIConst.EXTRA_RESULT, result);
        intent.putExtra(APIConst.EXTRA_RESULT_ID, id);
        sendBroadcast(intent);
    }


}
