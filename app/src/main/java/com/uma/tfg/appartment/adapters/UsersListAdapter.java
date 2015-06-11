package com.uma.tfg.appartment.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.User;

import java.util.List;

public class UsersListAdapter extends BaseAdapter{

    public List<User> mUsersList;

    public UsersListAdapter(List<User> usersList){
        this.mUsersList = usersList;
    }

    @Override
    public int getCount() {
        return this.mUsersList != null ? mUsersList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return this.mUsersList != null ? mUsersList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.row_user_item_list, null);
        }

        User item = mUsersList.get(position);

        TextView userNameTextView = (TextView) v.findViewById(R.id.user_name_tv);
        userNameTextView.setText(item.userName);

        TextView userEmailTextView = (TextView) v.findViewById(R.id.user_email_tv);
        if (!TextUtils.isEmpty(item.userEmail)){
            userEmailTextView.setText(item.userEmail);
        }
        if (!TextUtils.isEmpty(item.userPictureUrl)){
            Picasso.with(parent.getContext()).load(item.userPictureUrl).into((ImageView) v.findViewById(R.id.user_profile_picture_iv));
        }

        return v;
    }
}
