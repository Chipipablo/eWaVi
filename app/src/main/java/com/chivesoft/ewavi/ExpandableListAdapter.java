package com.chivesoft.ewavi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<DrawerListModel> listDataHeader;
    private HashMap<DrawerListModel, List<View>> listDataChild;

    public ExpandableListAdapter(Context context, List<DrawerListModel> listDataHeader, HashMap<DrawerListModel, List<View>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public View getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

//        final String childText = getChild(groupPosition, childPosition).getTitle();
//        convertView = null;
//        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawer_list_layout_child, null);



            LinearLayout drawer_list_item = convertView.findViewById(R.id.drawer_list_item);

            View v = getChild(groupPosition, childPosition);
            if (v.getParent() != null) ((ViewGroup) v.getParent()).removeView(v);

            drawer_list_item.addView(v);


//        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public DrawerListModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
//        Log.e("groupId", "groupId: "+groupPosition);
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).getTitle();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawer_list_layout_header, null);
        }

        TextView text_list = convertView.findViewById(R.id.text_list);
//        text_list.setTypeface(Main2Activity.typefaceBold);
        text_list.setText(headerTitle);

        ImageView icon_list = convertView.findViewById(R.id.icon_list);
        icon_list.setImageBitmap(getGroup(groupPosition).getPicture());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}