package com.em.posloyalty.api;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

public class API
{

    private HttpClient client = new DefaultHttpClient();
    private HttpPost post = new HttpPost("http://ewallet.latsolutions.com/public/apiCaller");
    HttpParams httpParams = client.getParams();

    private int TIME_OUT = 30 * 1000;
    //HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
    //HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);


    public String uploadTransactions(String jsonOfListTransactions)
    {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("class", "Transaction"));
        params.add(new BasicNameValuePair("task", "transaction_createTrans"));
        params.add(new BasicNameValuePair("trans", jsonOfListTransactions));

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;

    }

    public String getAllCard(long userid, int version)
    {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("class", "Card"));
        params.add(new BasicNameValuePair("task", "card_GetAllSync"));
        params.add(new BasicNameValuePair("version", version + ""));
        params.add(new BasicNameValuePair("userid", userid + ""));

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;

    }


    public String useVoucher()
    {
        return "";
    }


    // get already used voucher code after login for current customer id
    public String getUsedVoucher(long customerID)
    {
        return "";
    }


    // get not used voucher code after login for current customer id
    public String getActiveVoucher(long customerID)
    {
        return "";
    }


    // generate voucher code base on point user type in
    public String generateVoucher(double point)
    {
        return "";
    }


    // login and return customer information
    public String login(String Username, String Password)
    {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("class", "User"));
        params.add(new BasicNameValuePair("task", "user_Login"));
        params.add(new BasicNameValuePair("username", Username));
        params.add(new BasicNameValuePair("password", Password));

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String uploadGCMRegisterIdToServer(String regId)
    {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("action", "insert_notification"));
        params.add(new BasicNameValuePair("register_id", regId));

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


}