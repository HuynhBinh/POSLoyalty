package com.em.posloyalty.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.posloyalty.R;
import com.em.posloyalty.consts.APIConst;
import com.em.posloyalty.consts.StaticFunc;
import com.em.posloyalty.service.APIService;
import com.google.zxing.BarcodeFormat;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import greendao.Voucher;

/**
 * Created by USER on 5/12/2015.
 */
public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder>
{

    public List<Voucher> listData;
    int screenWidth = 500;

    private Context context;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public ImageView barcodeImageView;
        public ImageView usedImageView;
        public TextView txtGenTime;
        public TextView txtAmount;
        public LinearLayout wholeView;
        public LinearLayout subWholeView;
        public TextView txtCode;

        public ViewHolder(View v)
        {
            super(v);
            barcodeImageView = (ImageView) v.findViewById(R.id.barcodeImageView);
            usedImageView = (ImageView) v.findViewById(R.id.barcodeImageViewUsed);
            txtGenTime = (TextView) v.findViewById(R.id.txtGeneratedTime);
            txtAmount = (TextView) v.findViewById(R.id.txtAmount);
            wholeView = (LinearLayout) v.findViewById(R.id.wholeView);
            subWholeView = (LinearLayout) v.findViewById(R.id.subWholeView);
            txtCode = (TextView) v.findViewById(R.id.txtCode);
        }

    }

    public VoucherAdapter(Context ctx, List<Voucher> listVouchers, int screenWidth)
    {
        this.listData = listVouchers;
        this.screenWidth = screenWidth;
        this.context = ctx;

        initImageLoaderOption();
    }


    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_voucher, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        final Voucher voucher = listData.get(position);

        holder.wholeView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!voucher.getIsApplied())
                {
                    showConfirmDialog(context, voucher, position);
                }

            }
        });


        String barcodeData = voucher.getVoucherCode();
        String imagePath = StaticFunc.isFileExist(barcodeData);

        if (!imagePath.equals(""))
        {

            String loadURL = "file://" + Environment.getExternalStorageDirectory() + "/" + "POS Loyalty" + "/" + barcodeData + ".png";
            imageLoader.displayImage(loadURL, holder.barcodeImageView, options, new ImageLoadingListener()
            {
                @Override
                public void onLoadingStarted(String s, View view)
                {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason)
                {

                    Log.e("", "");

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap)
                {

                }

                @Override
                public void onLoadingCancelled(String s, View view)
                {

                }
            });
            //.displayImage(loadURL, holder.barcodeImageView, options);

            if (voucher.getIsApplied())
            {

                holder.usedImageView.setImageResource(R.drawable.used);
                holder.usedImageView.setVisibility(View.VISIBLE);

                holder.barcodeImageView.setAlpha(0.4f);
                //holder.subWholeView.setAlpha(0.7f);
            } else
            {

                holder.usedImageView.setVisibility(View.GONE);
                holder.barcodeImageView.setAlpha(1f);

                //holder.subWholeView.setAlpha(1.0f);
            }
        } else
        {
            Bitmap bitmap = null;

            try
            {
                bitmap = StaticFunc.generateBarcodeBitmap(barcodeData, BarcodeFormat.CODE_128, screenWidth, screenWidth / 4);

                SaveImageAsyncTask task = new SaveImageAsyncTask();
                task.bitmap = bitmap;
                task.execute(barcodeData);

                //StaticFunc.saveBitmapToSDCard(barcodeData, bitmap);

            } catch (Exception ex)
            {

            }

            if (bitmap != null)
            {

                holder.barcodeImageView.setImageBitmap(bitmap);


            }

            if (voucher.getIsApplied())
            {
                holder.usedImageView.setImageResource(R.drawable.used);
                holder.usedImageView.setVisibility(View.VISIBLE);
                holder.barcodeImageView.setAlpha(0.4f);
                //holder.subWholeView.setAlpha(0.7f);
            } else
            {


                holder.usedImageView.setVisibility(View.GONE);
                holder.barcodeImageView.setAlpha(1f);
                //holder.subWholeView.setAlpha(1.0f);
            }
        }


        holder.txtCode.setText("*" + voucher.getVoucherCode() + "*");
        holder.txtGenTime.setText(voucher.getVoucherGeneratedTime());
        holder.txtAmount.setText("$ " + voucher.getVoucherAmount().toString());
    }

    public void notifyDataChange()
    {
        notifyDataSetChanged();
    }

    public void removeItem(Voucher voucher)
    {


        int position = this.listData.indexOf(voucher);
        this.listData.remove(voucher);
        notifyItemRemoved(position);
        Handler han = new Handler();
        han.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                notifyDataChange();
            }
        }, 600);

    }

    public void initImageLoaderOption()
    {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.bg_grey).showImageOnFail(R.drawable.bg_grey).cacheInMemory(true).cacheOnDisc(true).build();
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return listData.size();
    }


    private void showConfirmDialog(final Context context, final Voucher voucher, final int position)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);//.setTitle("Apply Voucher");
        dialog.setContentView(R.layout.popup_confirm_voucher);


        LinearLayout btnCancel = (LinearLayout) dialog.findViewById(R.id.btnCancel);
        LinearLayout btnApply = (LinearLayout) dialog.findViewById(R.id.btnApply);
        TextView txtTittle = (TextView) dialog.findViewById(R.id.txtTittle);
        TextView txtContent = (TextView) dialog.findViewById(R.id.txtContent);
        TextView txtAmount = (TextView) dialog.findViewById(R.id.txtAmount);


        txtContent.setText(voucher.getVoucherCode());
        txtAmount.setText("$ " + voucher.getVoucherAmount().toString());


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

                Intent intent = new Intent(APIConst.ACTION_APPLY_VOUCHER, null, context, APIService.class);
                intent.putExtra(APIConst.EXTRA_VC_ID, voucher.getId().toString());
                context.startService(intent);


                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private class SaveImageAsyncTask extends AsyncTask<String, Void, String>
    {
        public Bitmap bitmap = null;

        @Override
        protected String doInBackground(String... params)
        {

            if (bitmap != null)
            {
                StaticFunc.saveBitmapToSDCard(params[0], bitmap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {

            super.onPostExecute(result);


        }
    }

}
