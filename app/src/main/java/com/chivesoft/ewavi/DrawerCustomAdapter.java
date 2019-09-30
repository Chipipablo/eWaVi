package com.chivesoft.ewavi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawerCustomAdapter extends ArrayAdapter<DrawerListModel> {

    private ArrayList<DrawerListModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView text_list;
        ImageView icon_list;
    }

    public DrawerCustomAdapter(ArrayList<DrawerListModel> data, Context context) {
        super(context, R.layout.drawer_list_layout_header, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DrawerListModel drawerListModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

//        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.drawer_list_layout_header, parent, false);
            viewHolder.text_list = convertView.findViewById(R.id.text_list);
            viewHolder.icon_list = convertView.findViewById(R.id.icon_list);

//            viewHolder.text_list.setTypeface(Main2Activity.typefaceBold);
//            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            result = convertView;
        }

        lastPosition = position;

        if (drawerListModel != null) {
            viewHolder.text_list.setText(drawerListModel.getTitle());
            viewHolder.icon_list.setImageBitmap(drawerListModel.getPicture());
        }


        return convertView;
    }
}
