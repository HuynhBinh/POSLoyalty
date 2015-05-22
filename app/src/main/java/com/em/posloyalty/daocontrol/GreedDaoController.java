package com.em.posloyalty.daocontrol;

import android.content.Context;

import com.em.posloyalty.application.POSApplication;

import java.util.ArrayList;
import java.util.List;

import greendao.Customer;
import greendao.CustomerDao;
import greendao.Voucher;
import greendao.VoucherDao;

/**
 * Created by USER on 5/13/2015.
 */
public class GreedDaoController
{
    private static VoucherDao getVoucherDAO(Context c)
    {
        return ((POSApplication) c.getApplicationContext()).getDaoSession().getVoucherDao();
    }

    private static CustomerDao getCustomerDAO(Context c)
    {
        return ((POSApplication) c.getApplicationContext()).getDaoSession().getCustomerDao();
    }

    public static void insertVoucher(Context context, Voucher voucher)
    {
        getVoucherDAO(context).insert(voucher);
    }

    public static void insertVoucherInBulk(Context context, ArrayList<Voucher> listVouchers)
    {
        getVoucherDAO(context).insertOrReplaceInTx(listVouchers, true);//.insertInTx(listVouchers);
    }

    public static void updateVoucher(Context context, Voucher voucher)
    {
        getVoucherDAO(context).update(voucher);
    }

    public static List<Voucher> loadActiveVouchers(Context context)
    {
        return getVoucherDAO(context).queryRaw("WHERE IS_APPLIED = ? ORDER BY _id DESC", "0");
    }

    public static List<Voucher> loadUsedVouchers(Context context)
    {
        return getVoucherDAO(context).queryRaw("WHERE IS_APPLIED = ? ORDER BY APPLIED_TIME DESC", "1");
    }


    public static List<Customer> getCustomerByCustomerID(Context context, String cusID)
    {
        return getCustomerDAO(context).queryRaw("WHERE CUSTOMER_ID = ?", cusID);
    }


    public static void insertCustomer(Context context, Customer customer)
    {
        getCustomerDAO(context).insert(customer);
    }

    public static void updateCustomer(Context context, Customer customer)
    {
        getCustomerDAO(context).update(customer);
    }

    public static Customer getCustomerByID(Context context, long id)
    {
        return getCustomerDAO(context).load(id);
    }

    public static Voucher getVoucherByID(Context context, long id)
    {
        return getVoucherDAO(context).load(id);
    }

    public static void deleteAllCustomer(Context context)
    {

        getCustomerDAO(context).deleteAll();

    }


    public static void deleteAllVoucher(Context context)
    {

        getVoucherDAO(context).deleteAll();

    }

    public static Voucher getVoucherByVoucherCode(Context context, String vCode)
    {
        return getVoucherDAO(context).queryBuilder().where(VoucherDao.Properties.VoucherCode.eq(vCode)).uniqueOrThrow();
    }

}
