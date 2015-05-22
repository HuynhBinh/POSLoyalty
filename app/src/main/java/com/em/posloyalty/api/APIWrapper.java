package com.em.posloyalty.api;

import android.content.Context;
import android.util.Log;

import com.em.posloyalty.daocontrol.GreedDaoController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.dao.DaoException;
import greendao.Customer;
import greendao.Voucher;

/**
 * Created by USER on 5/13/2015.
 */
public class APIWrapper
{
    public static boolean login(Context context, String username, String pass)
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
                String groupName = data.optString("group_name");
                String customerName = data.optString("customer_name");
                String customerCode = data.optString("customer_code");
                String email = data.optString("email");
                Double loyaltyPoint = data.optDouble("points");
                String customerID = data.optString("customer_id");

                JSONObject company = data.getJSONObject("store");
                String companyName = company.optString("store_name");
                Double minAmount = company.optDouble("voucher_min_amount");
                Double maxAmount = company.optDouble("voucher_max_amount");
                Boolean isFixedAmount = company.optBoolean("voucher_is_fixed_amount");


                Customer customer = new Customer();
                customer.setId(1l);
                customer.setCustomerID(customerID);
                customer.setCustomerName(customerName);
                customer.setCustomerEmail(email);
                //if (!loyaltyPoint.isEmpty())
                //{
                customer.setCustomerPoint(loyaltyPoint);
                // }

                customer.setCustomerGroup(groupName);
                customer.setCustomerCode(customerCode);
                customer.setCompany(companyName);
                customer.setVoucherMin(minAmount);
                customer.setVoucherMax(maxAmount);
                customer.setIsFixedAmount(isFixedAmount);
                customer.setFixedAmount(10.0);


                List<Customer> customers = GreedDaoController.getCustomerByCustomerID(context, customerID);


                if (customers.size() > 0)
                {
                    customer.setId(customers.get(0).getId());
                    GreedDaoController.updateCustomer(context, customer);
                } else
                {
                    GreedDaoController.insertCustomer(context, customer);
                }


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


    public static boolean generateVoucher(Context context, String customerID, double amount)
    {


        boolean isSuccess = false;
        API api = new API();
        String result = api.generateVoucher(customerID, amount);
        JSONObject jsonResponse;

        try
        {
            jsonResponse = new JSONObject(result);

            JSONObject root = jsonResponse.getJSONObject("root");

            String statusFromServer = root.optString("status");

            if (statusFromServer.equalsIgnoreCase("success"))
            {


                JSONObject data = root.getJSONObject("data");
                String voucherCode = data.optString("voucher_code");
                String voucherAmount = data.optString("voucher_value");
                String createdAt = data.optString("created_at");


                Voucher voucher = new Voucher();
                voucher.setVoucherCode(voucherCode);
                voucher.setVoucherGeneratedTime(createdAt);
                voucher.setIsApplied(false);

                if (!voucherAmount.isEmpty())
                {
                    voucher.setVoucherAmount(Double.parseDouble(voucherAmount));
                }


                GreedDaoController.insertVoucher(context, voucher);


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

    public static boolean getAllVouchers(Context context, String customerID)
    {
        Log.e("API", "getAllVouchers");

        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllVouchers(customerID);
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
                    JSONObject jsonObject = data.getJSONObject(i);

                    String voucherCode = jsonObject.optString("voucher_code");
                    String voucherAmount = jsonObject.optString("voucher_value");
                    String createdAt = jsonObject.optString("created_at");
                    boolean isUsed = jsonObject.optBoolean("is_used");


                    Voucher voucher = new Voucher();
                    voucher.setVoucherCode(voucherCode);
                    voucher.setVoucherGeneratedTime(createdAt);
                    voucher.setIsApplied(isUsed);

                    if (!voucherAmount.isEmpty())
                    {
                        voucher.setVoucherAmount(Double.parseDouble(voucherAmount));
                    }

                    try
                    {

                        Voucher inDBVoucher = GreedDaoController.getVoucherByVoucherCode(context, voucherCode);
                        voucher.setId(inDBVoucher.getId());
                        GreedDaoController.updateVoucher(context, voucher);
                    } catch (DaoException dex)
                    {
                        GreedDaoController.insertVoucher(context, voucher);
                    }


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
