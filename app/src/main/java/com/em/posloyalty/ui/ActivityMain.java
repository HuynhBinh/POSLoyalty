package com.em.posloyalty.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.posloyalty.R;
import com.em.posloyalty.adapter.VoucherAdapter;
import com.em.posloyalty.consts.APIConst;
import com.em.posloyalty.consts.Consts;
import com.em.posloyalty.consts.StaticFunc;
import com.em.posloyalty.daocontrol.GreedDaoController;
import com.em.posloyalty.receiver.NetworkChangeReceiver;
import com.em.posloyalty.service.APIService;
import com.em.posloyalty.simpletoast.SimpleToast;

import java.util.ArrayList;
import java.util.List;

import greendao.Customer;
import greendao.Voucher;


public class ActivityMain extends ActionBarActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewUsed;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerUsed;

    public RecyclerView.Adapter mAdapter;
    public RecyclerView.Adapter mAdapterUsed;

    private List<Voucher> listVouchers;

    LinearLayout tabActiveVoucher;
    LinearLayout tabUsedVoucher;
    LinearLayout tabCustomer;

    LinearLayout voucherView;
    LinearLayout usedVoucherView;
    LinearLayout customerView;

    int screenWidth = 600;

    int currentTab = Consts.TAB_CUSTOMER;

    public Drawable bgPress;
    public Drawable bgUnPress;

    RelativeLayout btnGenVoucher;
    LinearLayout btnLogout;

    LinearLayout actionBar;

    TextView txtNumberOfNewVoucher;
    public TextView txtNumberOfUsedVoucher;

    ProgressBar progressBar;

    TextView txtCustomerName;
    TextView txtCustomerEmail;
    TextView txtCustomerGroup;
    TextView txtCustomerLoyaltyPoint;

    TextView txtCompanyName;
    TextView txtButtonInfo;

    SwipeRefreshLayout voucherSwipeRefreshLayout;

    private BroadcastReceiver activityReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase(APIConst.RECEIVER_FINISH_GEN_VOUCHER))
            {

                String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                if (result.equalsIgnoreCase(APIConst.RESULT_OK))
                {

                    Customer customer = GreedDaoController.getCustomerByID(ActivityMain.this, 1);

                    txtCustomerLoyaltyPoint.setText(String.format("$ %.2f", customer.getCustomerPoint()));

                    Consts.NUM_OF_NEW_VOUCHER++;
                    if (Consts.NUM_OF_NEW_VOUCHER != 0)
                    {
                        txtNumberOfNewVoucher.setText(Consts.NUM_OF_NEW_VOUCHER + "");
                        txtNumberOfNewVoucher.setVisibility(View.VISIBLE);
                    }
                    SimpleToast.info(ActivityMain.this, "Generated successfully!");
                }
                else if (result.equalsIgnoreCase(APIConst.RESULT_NO_INTERNET))
                {
                    SimpleToast.error(ActivityMain.this, "Please check internet connection!");
                }
                else
                {
                    SimpleToast.error(ActivityMain.this, "Fail to generate voucher!");
                }


            }
            else if (intent.getAction().equalsIgnoreCase(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER))
            {

                Log.e("GO", "Go here main");

                String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                if (result.equalsIgnoreCase(APIConst.RESULT_OK))
                {
                    listVouchers = GreedDaoController.loadActiveVouchers(ActivityMain.this);
                    ((VoucherAdapter) mAdapter).listData = listVouchers;
                    ((VoucherAdapter) mAdapter).notifyDataChange();

                    if (voucherSwipeRefreshLayout != null)
                    {
                        voucherSwipeRefreshLayout.setRefreshing(false);
                    }

                    // update status of loading voucher
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            APIConst.isLoadingActiveVoucher = false;

                        }
                    }, Consts.LEAST_REFRESH_TIME);


                }
                else if (result.equalsIgnoreCase(APIConst.RESULT_JUST_LOAD_IN_A_SECOND))
                {

                    voucherSwipeRefreshLayout.setRefreshing(false);

                }
                else if (result.equalsIgnoreCase(APIConst.RESULT_NO_INTERNET))
                {
                    APIConst.isLoadingActiveVoucher = false;
                    SimpleToast.error(ActivityMain.this, "No internet connection! Fail to update data.");
                }
                else
                {
                    APIConst.isLoadingActiveVoucher = false;
                    SimpleToast.error(ActivityMain.this, "Error! Fail to update data");
                }


            }
            else if (intent.getAction().equalsIgnoreCase(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER))
            {

                Log.e("GO", "Go here main");

                String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                if (result.equalsIgnoreCase(APIConst.RESULT_OK))
                {
                    listVouchers = GreedDaoController.loadUsedVouchers(ActivityMain.this);
                    ((VoucherAdapter) mAdapterUsed).listData = listVouchers;
                    ((VoucherAdapter) mAdapterUsed).notifyDataChange();

                   /* if (voucherSwipeRefreshLayout != null)
                    {
                        voucherSwipeRefreshLayout.setRefreshing(false);
                    }*/

                    // update status of loading voucher
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            APIConst.isLoadingUsedVoucher = false;

                        }
                    }, Consts.LEAST_REFRESH_TIME);


                }
                else if (result.equalsIgnoreCase(APIConst.RESULT_JUST_LOAD_IN_A_SECOND))
                {

                    //voucherSwipeRefreshLayout.setRefreshing(false);

                }
                else if (result.equalsIgnoreCase(APIConst.RESULT_NO_INTERNET))
                {
                    APIConst.isLoadingUsedVoucher = false;
                    SimpleToast.error(ActivityMain.this, "No internet connection! Fail to update data.");
                }
                else
                {
                    APIConst.isLoadingUsedVoucher = false;
                    SimpleToast.error(ActivityMain.this, "Error! Fail to update data");
                }


            }
            else if (intent.getAction().equalsIgnoreCase(APIConst.RECEIVER_FINISH_APPLY_VOUCHER))
            {
                String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                String code = intent.getStringExtra(APIConst.EXTRA_RESULT_ID);

                if (result.equalsIgnoreCase(APIConst.RESULT_OK))
                {

                    Voucher voucher = GreedDaoController.getVoucherByVoucherCode(ActivityMain.this, code);

                    //voucher.setIsApplied(true);
                    //voucher.setAppliedTime(System.currentTimeMillis() + "");
                    //GreedDaoController.updateVoucher(context, voucher);

                    ((VoucherAdapter) mAdapter).removeItem(voucher);


                    SimpleToast.info(context, "Applied successfully!");
                }
                else if (result.equalsIgnoreCase(APIConst.RESULT_NO_INTERNET))
                {
                    SimpleToast.error(ActivityMain.this, "No internet connection! Fail to apply.");
                }
                else
                {
                    SimpleToast.error(ActivityMain.this, "Error! Fail to apply");
                }


            }
            else if (intent.getAction().equalsIgnoreCase(NetworkChangeReceiver.ON_NETWORK_CHANGE))
            {
                String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
                if (result.equals(NetworkChangeReceiver.ONLINE))
                {
                    enableBtnGen();

                }
                else if (result.equals(NetworkChangeReceiver.OFFLINE))
                {
                    disableBtnGen();
                }
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        bgPress = getResources().getDrawable(R.drawable.bg_tab_press);
        bgUnPress = getResources().getDrawable(R.drawable.bg_tab);

        // recycler view init
        listVouchers = new ArrayList<>();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;

        mAdapter = new VoucherAdapter(ActivityMain.this, listVouchers, screenWidth);
        mRecyclerView.setAdapter(mAdapter);

        mAdapterUsed = new VoucherAdapter(ActivityMain.this, listVouchers, screenWidth);
        mRecyclerViewUsed.setAdapter(mAdapterUsed);

        registerBroadcastReceiver();

        registerCoucherSwipeRefresh();

        setCustomerDataToScreen();

        registerTabClickListener();
        registerBtnGenVoucherClickListener();
        registerBtnLogoutClickListener();


    }

    private void setCustomerDataToScreen()
    {
        Customer customer;

        customer = GreedDaoController.getCustomerByID(ActivityMain.this, 1);


        txtCustomerName.setText(customer.getCustomerName());
        txtCustomerEmail.setText(customer.getCustomerEmail());
        txtCustomerGroup.setText(customer.getCustomerGroup());


        txtCustomerLoyaltyPoint.setText(String.format("$ %.2f", customer.getCustomerPoint()));

        txtCompanyName.setText(customer.getCompany());


    }


    private void initView()
    {

        voucherView = (LinearLayout) findViewById(R.id.voucherView);
        voucherView.setVisibility(View.GONE);
        usedVoucherView = (LinearLayout) findViewById(R.id.usedVoucherView);
        usedVoucherView.setVisibility(View.GONE);

        customerView = (LinearLayout) findViewById(R.id.customerView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewUsed = (RecyclerView) findViewById(R.id.recycler_view_used);
        mRecyclerViewUsed.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManagerUsed = new LinearLayoutManager(this);
        mRecyclerViewUsed.setLayoutManager(mLayoutManagerUsed);


        tabActiveVoucher = (LinearLayout) findViewById(R.id.tabActiveVoucher);
        tabUsedVoucher = (LinearLayout) findViewById(R.id.tabUsedVoucher);
        tabCustomer = (LinearLayout) findViewById(R.id.tabCustomer);

        btnGenVoucher = (RelativeLayout) findViewById(R.id.btnGenVoucher);
        txtButtonInfo = (TextView) findViewById(R.id.txtBtnInfo);
        if (StaticFunc.isNetworkAvailable(this) == false)
        {
            disableBtnGen();
        }


        btnLogout = (LinearLayout) findViewById(R.id.btnLogout);

        actionBar = (LinearLayout) findViewById(R.id.actionbar);

        txtNumberOfNewVoucher = (TextView) findViewById(R.id.txtNumberOfNewVoucher);
        txtNumberOfNewVoucher.setVisibility(View.GONE);

        txtNumberOfUsedVoucher = (TextView) findViewById(R.id.txtNumberOfUsedVoucher);
        txtNumberOfUsedVoucher.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtCustomerEmail = (TextView) findViewById(R.id.txtCustomerEmail);
        txtCustomerGroup = (TextView) findViewById(R.id.txtCustomerGroup);

        txtCustomerLoyaltyPoint = (TextView) findViewById(R.id.txtCurrentPoint);

        txtCompanyName = (TextView) findViewById(R.id.txtCompanyName);

        voucherSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        voucherSwipeRefreshLayout.setColorSchemeResources(R.color.bg_orance);

    }


    private void registerCoucherSwipeRefresh()
    {
        voucherSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Customer customer = GreedDaoController.getCustomerByID(ActivityMain.this, 1);

                //load active voucher update
                Intent intent = new Intent(APIConst.ACTION_LOAD_ACTIVE_VOUCHER, null, ActivityMain.this, APIService.class);
                intent.putExtra(APIConst.EXTRA_CUSTOMER_ID, customer.getCustomerID());
                startService(intent);
            }
        });
    }


    private void enableBtnGen()
    {
        btnGenVoucher.setEnabled(true);
        btnGenVoucher.setAlpha(1f);
        txtButtonInfo.setText("(click to generate voucher)");
    }

    private void disableBtnGen()
    {
        btnGenVoucher.setEnabled(false);
        btnGenVoucher.setAlpha(0.4f);

        txtButtonInfo.setText("Opp, no internet connection!");
    }


    private void registerBtnLogoutClickListener()
    {
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                showLogoutConfirmDialog(ActivityMain.this);
            }
        });
    }

    private void registerBtnGenVoucherClickListener()
    {
        btnGenVoucher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (StaticFunc.isNetworkAvailable(ActivityMain.this))
                {
                    showEnterAmountDialog(ActivityMain.this);
                }
                else
                {
                    SimpleToast.error(ActivityMain.this, "Sorry, please check internet connection!");
                }
            }
        });
    }


    private void registerTabClickListener()
    {

        {
            tabActiveVoucher.setBackground(bgUnPress);
            tabCustomer.setBackground(bgPress);
            tabUsedVoucher.setBackground(bgUnPress);
        }


        tabActiveVoucher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (currentTab != Consts.TAB_ACTIVE_VOUCHER)
                {

                    Customer customer = GreedDaoController.getCustomerByID(ActivityMain.this, 1);

                    //load active voucher update
                    Intent intent = new Intent(APIConst.ACTION_LOAD_ACTIVE_VOUCHER, null, ActivityMain.this, APIService.class);
                    intent.putExtra(APIConst.EXTRA_CUSTOMER_ID, customer.getCustomerID());
                    startService(intent);


                    tabActiveVoucher.setBackground(bgPress);
                    tabCustomer.setBackground(bgUnPress);
                    tabUsedVoucher.setBackground(bgUnPress);

                    currentTab = Consts.TAB_ACTIVE_VOUCHER;

                    voucherView.setVisibility(View.VISIBLE);
                    usedVoucherView.setVisibility(View.GONE);
                    customerView.setVisibility(View.GONE);

                    listVouchers = GreedDaoController.loadActiveVouchers(ActivityMain.this);
                    ((VoucherAdapter) mAdapter).listData = listVouchers;
                    ((VoucherAdapter) mAdapter).notifyDataChange();
                    //mAdapter = new VoucherAdapter(ActivityMain.this, listVouchers, screenWidth);
                    //mRecyclerView.setAdapter(mAdapter);

                    Consts.NUM_OF_NEW_VOUCHER = 0;
                    txtNumberOfNewVoucher.setVisibility(View.GONE);
                }


            }
        });

        tabUsedVoucher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (currentTab != Consts.TAB_USED_VOUCHER)
                {

                    Customer customer = GreedDaoController.getCustomerByID(ActivityMain.this, 1);

                    //load active voucher update
                    Intent intent = new Intent(APIConst.ACTION_LOAD_USED_VOUCHER, null, ActivityMain.this, APIService.class);
                    intent.putExtra(APIConst.EXTRA_CUSTOMER_ID, customer.getCustomerID());
                    startService(intent);


                    tabActiveVoucher.setBackground(bgUnPress);
                    tabCustomer.setBackground(bgUnPress);
                    tabUsedVoucher.setBackground(bgPress);

                    currentTab = Consts.TAB_USED_VOUCHER;

                    usedVoucherView.setVisibility(View.VISIBLE);
                    voucherView.setVisibility(View.GONE);
                    customerView.setVisibility(View.GONE);

                    listVouchers = GreedDaoController.loadUsedVouchers(ActivityMain.this);
                    ((VoucherAdapter) mAdapterUsed).listData = listVouchers;
                    ((VoucherAdapter) mAdapterUsed).notifyDataChange();


                    //mAdapter = new VoucherAdapter(ActivityMain.this, listVouchers, screenWidth);
                    //mRecyclerView.setAdapter(mAdapter);

                    Consts.NUM_OF_USED_VOUCHER = 0;
                    txtNumberOfUsedVoucher.setVisibility(View.GONE);
                }

            }
        });

        tabCustomer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (currentTab != Consts.TAB_CUSTOMER)
                {
                    tabActiveVoucher.setBackground(bgUnPress);
                    tabCustomer.setBackground(bgPress);
                    tabUsedVoucher.setBackground(bgUnPress);

                    currentTab = Consts.TAB_CUSTOMER;

                    usedVoucherView.setVisibility(View.GONE);
                    customerView.setVisibility(View.VISIBLE);
                    voucherView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void registerBroadcastReceiver()
    {
        if (activityReceiver != null)
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(APIConst.RECEIVER_FINISH_GEN_VOUCHER);
            intentFilter.addAction(APIConst.RECEIVER_FINISH_APPLY_VOUCHER);
            intentFilter.addAction(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER);
            intentFilter.addAction(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER);
            // this is for online offline status
            intentFilter.addAction(NetworkChangeReceiver.ON_NETWORK_CHANGE);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String validateEnterAmount(String amount, double minAmountAllow, double maxAmountAllow)
    {
        double dAmount = 0.0;

        if (amount.isEmpty())
        {
            return "Amount cannot be empty!";
        }

        if (amount.length() > 6)
        {
            return "Cannot generate voucher with this amount. Please try a smaller amount!";
        }

        try
        {
            dAmount = Double.parseDouble(amount);


        }
        catch (Exception ex)
        {
            return "Please enter a valid amount. Must be numeric!";
        }

        if (dAmount <= 0)
        {
            return "Amount must greater than zero!";
        }


        if (dAmount < minAmountAllow)
        {
            return "Amount cannot be less than " + minAmountAllow;
        }

        if (dAmount > maxAmountAllow)
        {
            return "Amount cannot be greater than " + maxAmountAllow;
        }


        return "OK";

    }


    private void showEnterAmountDialog(Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);//.setTitle("Apply Voucher");
        dialog.setContentView(R.layout.popup_gen_voucher_amount);


        LinearLayout btnCancel = (LinearLayout) dialog.findViewById(R.id.btnCancel);
        LinearLayout btnGen = (LinearLayout) dialog.findViewById(R.id.btnGenVoucher);
        final EditText edtAmount = (EditText) dialog.findViewById(R.id.edtAmount);
        final TextView txtError = (TextView) dialog.findViewById(R.id.txtError);

        final Customer customer = GreedDaoController.getCustomerByID(ActivityMain.this, 1);

        edtAmount.setHint("enter amount $" + customer.getVoucherMin() + " - $" + customer.getVoucherMax());


        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        btnGen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String strAmount = edtAmount.getText().toString().trim();


                String validatedResult = validateEnterAmount(strAmount, customer.getVoucherMin(), customer.getVoucherMax());

                if (!validatedResult.equalsIgnoreCase("OK"))
                {
                    txtError.setText(validatedResult);
                    txtError.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtError.setVisibility(View.GONE);
                    Intent intent = new Intent(APIConst.ACTION_GEN_VOUCHER, null, ActivityMain.this, APIService.class);
                    intent.putExtra(APIConst.EXTRA_AMOUNT, strAmount);

                    //

                    //
                    intent.putExtra(APIConst.EXTRA_CUSTOMER_ID, customer.getCustomerID());
                    startService(intent);
                    dialog.dismiss();
                }


            }
        });


        dialog.show();
    }


    private void showLogoutConfirmDialog(final Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_confirm_voucher);


        LinearLayout btnCancel = (LinearLayout) dialog.findViewById(R.id.btnCancel);
        LinearLayout btnApply = (LinearLayout) dialog.findViewById(R.id.btnApply);
        TextView txtTittle = (TextView) dialog.findViewById(R.id.txtTittle);
        TextView txtContent = (TextView) dialog.findViewById(R.id.txtContent);
        TextView txtAmount = (TextView) dialog.findViewById(R.id.txtAmount);
        TextView txtAlertFooter = (TextView) dialog.findViewById(R.id.txtAlertFooter);
        TextView txtAlertHeader = (TextView) dialog.findViewById(R.id.txtAlertHeader);
        TextView txtApply = (TextView) dialog.findViewById(R.id.txtApply);


        txtContent.setVisibility(View.GONE);
        txtAmount.setVisibility(View.GONE);
        txtAlertFooter.setVisibility(View.GONE);
        txtAlertHeader.setText("Do you wish to Logout the application?");
        txtApply.setText("Logout");


        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GreedDaoController.deleteAllCustomer(ActivityMain.this);
                GreedDaoController.deleteAllVoucher(ActivityMain.this);

                Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
                startActivity(intent);
                finish();

                dialog.dismiss();
            }
        });


        dialog.show();
    }


    // testing only
    private void genTestVoucherCode()
    {

        ArrayList<Voucher> listData = new ArrayList<>();

        for (int i = 0; i < 50; i++)
        {
            String time = "May-" + i + "-2015";
            String amount = "$ " + i + ".00";
            double dAmount = i;
            String voucherCode = "vouchercode" + i;


            Voucher voucher = new Voucher();
            voucher.setId((long) i);
            voucher.setVoucherGeneratedTime(time);
            voucher.setVoucherAmount(dAmount);
            voucher.setVoucherCode(voucherCode);
            if (i % 2 == 0)
            {
                voucher.setIsApplied(false);

            }
            else
            {
                voucher.setIsApplied(true);
            }

            listData.add(voucher);
        }

        GreedDaoController.insertVoucherInBulk(ActivityMain.this, listData);

        //return listData;

    }


}
