package com.hellotractor.icomparecrypts;

/**
 * Created by Abdulmajid on 10/21/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.hellotractor.icomparecrypts.model.ExchangeRateItem;
import com.hellotractor.icomparecrypts.network.VolleyClient;
import com.hellotractor.icomparecrypts.util.DateTimeUtil;
import com.hellotractor.icomparecrypts.util.Fonts;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrencyListAdapter extends BaseAdapter {
    private final Activity mContext;
    private final ArrayList<ExchangeRateItem> exchangeRateItems;

    public CurrencyListAdapter(Activity context, ArrayList<ExchangeRateItem> exchangeRateItems) {
        this.mContext = context;
        this.exchangeRateItems = exchangeRateItems;
    }

    // 2
    @Override
    public int getCount() {
        return exchangeRateItems.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return exchangeRateItems.get(position).getId();
    }

    // 4
    @Override
    public ExchangeRateItem getItem(int position) {
        return exchangeRateItems.get(position);
    }

    // 5
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.currency_list_item, null);
            holder = new ViewHolder();
            holder.mFromCurrencyAmount = (TextView) convertView.findViewById(R.id.from_amount);
            holder.mToCurrencyAmount = (TextView) convertView.findViewById(R.id.to_amount);
            holder.mFromCurrencyImage = (CircleImageView) convertView.findViewById(R.id.from_currency_image);
            holder.mToCurrencyImage = (CircleImageView) convertView.findViewById(R.id.to_currency_image);
            holder.mFromText = (TextView) convertView.findViewById(R.id.from_text);
            holder.mToText = (TextView) convertView.findViewById(R.id.to_text);
            holder.mFromFullname = (TextView) convertView.findViewById(R.id.from_fullname);
            holder.mToFullname = (TextView) convertView.findViewById(R.id.to_fullname);
            holder.mUpdated = (TextView) convertView.findViewById(R.id.updated);

            holder.mToCurrencyAmount.setTypeface(Fonts.titleFont(mContext));
            holder.mFromCurrencyAmount.setTypeface(Fonts.titleFont(mContext));
            holder.mToText.setTypeface(Fonts.subtitleFont(mContext));
            holder.mFromText.setTypeface(Fonts.subtitleFont(mContext));
            holder.mFromFullname.setTypeface(Fonts.subtitleFont(mContext));
            holder.mToFullname.setTypeface(Fonts.subtitleFont(mContext));
            holder.mUpdated.setTypeface(Fonts.subtitleFont(mContext));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);

        final ExchangeRateItem currentItem = exchangeRateItems.get(position);
        holder.mFromCurrencyAmount.setText(numberFormat.format(currentItem.getFromAmount())+" "+currentItem.getFromCurrencySymbol());
        holder.mToCurrencyAmount.setText(numberFormat.format(currentItem.getToAmount())+" "+currentItem.getToCurrencySymbol());
        holder.mFromFullname.setText(currentItem.getFromCurrencyFullname());
        holder.mToFullname.setText(currentItem.getToCurrencyFullname());

        Glide.with(mContext).load(currentItem.getFromImageURL()).into(holder.mFromCurrencyImage);
        Glide.with(mContext).load(currentItem.getToImageURL()).into(holder.mToCurrencyImage);

        holder.mUpdated.setText(DateTimeUtil.getTimeAgo(currentItem.getUpdated(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE, mContext));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConversionActivity.class);
                intent.putExtra(ConversionActivity.FROM_CURRENCY_SYMBOL, currentItem.getFromCurrencySymbol());
                intent.putExtra(ConversionActivity.TO_CURRENCY_SYMBOL, currentItem.getToCurrencySymbol());
                intent.putExtra(ConversionActivity.FROM_CURRENCY_AMOUNT, currentItem.getFromAmount());
                intent.putExtra(ConversionActivity.TO_CURRENCY_AMOUNT, currentItem.getToAmount());
                intent.putExtra(ConversionActivity.FROM_CURRENCY_FULLNAME, currentItem.getFromCurrencyFullname());
                intent.putExtra(ConversionActivity.TO_CURRENCY_FULLNAME, currentItem.getToCurrencyFullname());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        CircleImageView mFromCurrencyImage;
        CircleImageView mToCurrencyImage;
        TextView mFromCurrencyAmount;
        TextView mToCurrencyAmount;
        TextView mToText;
        TextView mFromText;
        TextView mFromFullname;
        TextView mToFullname;
        TextView mUpdated;
    }
}
