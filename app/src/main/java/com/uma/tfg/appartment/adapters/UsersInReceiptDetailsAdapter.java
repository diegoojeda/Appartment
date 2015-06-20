package com.uma.tfg.appartment.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.User;

import java.util.List;

public class UsersInReceiptDetailsAdapter extends BaseAdapter {

    public List<User> mUsersList;

    public UsersInReceiptDetailsAdapter(List<User> usersList){
        this.mUsersList = usersList;
    }

    @Override
    public int getCount() {
        return mUsersList != null ? mUsersList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mUsersList != null ? mUsersList.get(i) : null;
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
            v = inflater.inflate(R.layout.row_user_in_receipt_details, null);
        }

        User item = mUsersList.get(position);

        TextView userNameTv = (TextView) v.findViewById(R.id.receipt_user_name_tv);
        TextView userEmailTv = (TextView) v.findViewById(R.id.receipt_user_email_tv);
        ImageView userProfilePictureIv = (ImageView) v.findViewById(R.id.receipt_details_user_profile_picture_iv);
        ImageView userHasPaidIv = (ImageView) v.findViewById(R.id.receipt_state_iv);
        if (!TextUtils.isEmpty(item.userPictureUrl)) {
            Picasso.with(parent.getContext()).load(item.userPictureUrl).into(userProfilePictureIv);
        }
        else{
            Picasso.with(parent.getContext()).load(R.drawable.default_profile).into(userProfilePictureIv);
        }

        userNameTv.setText(item.userName);
        userEmailTv.setText(item.userEmail);
        if (item.userHasPaid){
            Picasso.with(parent.getContext()).load(R.drawable.green_tick).into(userHasPaidIv);
        }
        else{
            Picasso.with(parent.getContext()).load(R.drawable.red_cross).into(userHasPaidIv);
        }

        return v;
    }
}
