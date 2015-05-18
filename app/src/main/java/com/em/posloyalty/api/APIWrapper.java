package com.em.posloyalty.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by USER on 5/13/2015.
 */
public class APIWrapper
{
    public static boolean login(String username, String pass)
    {
        boolean isLoginSuccess = false;
        API api = new API();
        String result = api.login(username, pass);
        JSONObject jsonResponse;

        try
        {
            jsonResponse = new JSONObject(result);

            JSONObject root = jsonResponse.getJSONObject("root");

            String statusFromServer = root.optString("status");

            if (statusFromServer.equalsIgnoreCase("success"))
            {
                JSONObject data = root.getJSONObject("data");
                long userId = data.optInt("user_id");
                String userName = data.optString("user_name");
                String password = data.optString("password");

                isLoginSuccess = true;
            } else
            {
                isLoginSuccess = false;
            }
        } catch (Exception ex)
        {
            isLoginSuccess = false;
        }

        return isLoginSuccess;
    }

    public static boolean getActiveVouchers(long userid)
    {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllCard(userid, 0);
        JSONObject jsonResponse;

        try
        {
            jsonResponse = new JSONObject(result);

            JSONObject root = jsonResponse.getJSONObject("root");

            String statusFromServer = root.optString("status");

            if (statusFromServer.equalsIgnoreCase("success"))
            {
                JSONArray data = root.getJSONArray("data");

                for (int i = 0; i < data.length(); i++)
                {
                    JSONObject jsonCard = data.getJSONObject(i);
                    long cardId = jsonCard.optLong("card_id");
                    String cardName = jsonCard.optString("card_name");
                    int rebatePercent = jsonCard.optInt("rebate_percent");
                    double minAmount = jsonCard.optDouble("min_amount");
                    double maxAmount = jsonCard.optDouble("max_amount");
                    int orderPosition = jsonCard.optInt("order_position");
                    String bankName = jsonCard.optString("bank_name");
                    String categories = jsonCard.optString("category");


                }


                isSuccess = true;
            } else
            {
                isSuccess = false;
            }
        } catch (Exception ex)
        {
            isSuccess = false;
        }

        return isSuccess;

    }
}
