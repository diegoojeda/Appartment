package com.uma.tfg.appartment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.Group;

import java.util.List;

public class GroupsListAdapter extends BaseAdapter {

    public List<Group> mGroupsList;

    public GroupsListAdapter(List<Group> groupsList){
        mGroupsList = groupsList;
    }

    @Override
    public int getCount() {
        return mGroupsList != null ? mGroupsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mGroupsList != null ? mGroupsList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.row_group_item_list, null);
        }

        Group item = mGroupsList.get(position);

        TextView groupIdTextView = (TextView) v.findViewById(R.id.group_id_textview);
        TextView groupNameTextView = (TextView) v.findViewById(R.id.group_name_textview);

        groupIdTextView.setText(item.mId);
        groupNameTextView.setText(item.mGroupName);
        return v;
    }
}
