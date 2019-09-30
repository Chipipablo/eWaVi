package com.chivesoft.ewavi;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ListDataModel> implements View.OnClickListener {

    private ArrayList<ListDataModel> dataSet;
    private Context mContext;
    private float MaxWidth;
    private float MaxHeight;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtPrice;
        ImageView item_picture;
        ImageView item_marketplace;
        LinearLayout list_layout;
        ConstraintLayout imgLayout;
    }

    CustomAdapter(ArrayList<ListDataModel> data, Context context) {
        super(context, R.layout.list_layout, data);
        this.dataSet = data;
        this.mContext = context;
        MaxWidth = mContext.getResources().getDimension(R.dimen.item_img_max_width);
        MaxHeight = mContext.getResources().getDimension(R.dimen.item_img_max_height);
    }

    @Override
    public void onClick(View v) {
//        Log.e("onClick_adapter","getTag is null");
        if (v.getTag() == null) {
            Log.e("onClick_adapter", "getTag is null");
            return;
        }
        int position = (Integer) v.getTag();
//        ListDataModel dataModel = getItem(position);
        switch (v.getId()) {
            case R.id.img_layout:
                if (dataSet.get(position).getImgUrl() == null) {
                    Toast.makeText(mContext, R.string.noImg, Toast.LENGTH_LONG).show();
                } else {
                    Main2Activity.largeImageView.setImageBitmap(dataSet.get(position).getPicture());
                    Main2Activity.requestLargeImg(dataSet.get(position).getImgUrl());
                }
                break;
                /*case R.id.item_marketplace:
                    //Toast.makeText(mContext, dataSet.get(position).getUrl(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(dataSet.get(position).getUrl()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    break;*/
            default:

                break;
        }
    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        //ListDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.title);
            viewHolder.txtDescription = convertView.findViewById(R.id.description);
            viewHolder.txtPrice = convertView.findViewById(R.id.price);
            viewHolder.item_picture = convertView.findViewById(R.id.item_picture);
            viewHolder.item_marketplace = convertView.findViewById(R.id.item_marketplace);

//            viewHolder.txtTitle.setTypeface(MainActivity.typefaceBold);
//            viewHolder.txtDescription.setTypeface(MainActivity.typeface);
//            viewHolder.txtPrice.setTypeface(MainActivity.typefaceBold);

            viewHolder.list_layout = convertView.findViewById(R.id.list_layout);

            viewHolder.imgLayout = convertView.findViewById(R.id.img_layout);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        if (DataGetter.checkListEffects) {
            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
        }

        lastPosition = position;
        //viewHolder.item_marketplace.setOnClickListener(this);

        if (dataSet.get(position).getView() == null) {
            viewHolder.txtTitle.setVisibility(View.VISIBLE);
            viewHolder.txtDescription.setVisibility(View.VISIBLE);
            viewHolder.txtPrice.setVisibility(View.VISIBLE);
            viewHolder.item_picture.setVisibility(View.VISIBLE);
            viewHolder.item_marketplace.setVisibility(View.VISIBLE);
            viewHolder.imgLayout.setVisibility(View.VISIBLE);
            viewHolder.txtTitle.setText(dataSet.get(position).getTitle());
            viewHolder.txtDescription.setText(dataSet.get(position).getDescription());
            viewHolder.txtPrice.setText(dataSet.get(position).getPrice());
            viewHolder.item_picture.setImageBitmap(dataSet.get(position).getPicture());
/*
            float finalWidth = MaxWidth / dataSet.get(position).getPicture().getWidth();
            float finalHeight = MaxHeight / dataSet.get(position).getPicture().getHeight();
            float finalSize;
            if (finalWidth < finalHeight) finalSize = finalWidth;
            else finalSize = finalHeight;
//            Log.e("finalWidth", "finalWidth: " + finalWidth);
//            Log.e("finalHeight", "finalHeight: " + finalHeight);
//            Log.e("finalSize", "finalSize: " + finalSize);
            if (finalSize > 1.0f) {
                viewHolder.item_picture.setScaleX(finalSize);
                viewHolder.item_picture.setScaleY(finalSize);

            }*/
//            viewHolder.item_picture.setOnClickListener(this);
//            viewHolder.item_picture.setTag(position);
            viewHolder.item_marketplace.setImageBitmap(dataSet.get(position).getMarketplace());
            viewHolder.imgLayout.setOnClickListener(this);
            viewHolder.imgLayout.setTag(position);
            viewHolder.list_layout.setVisibility(View.GONE);
            if (viewHolder.list_layout.getChildCount() > 0)
                viewHolder.list_layout.removeAllViews();
        } else {
            viewHolder.txtTitle.setVisibility(View.GONE);
            viewHolder.txtDescription.setVisibility(View.GONE);
            viewHolder.txtPrice.setVisibility(View.GONE);
            viewHolder.item_picture.setVisibility(View.GONE);
            viewHolder.item_marketplace.setVisibility(View.GONE);
            viewHolder.imgLayout.setVisibility(View.GONE);
            AdView adView = dataSet.get(position).getView();
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }
            viewHolder.list_layout.addView(adView);
            viewHolder.list_layout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
