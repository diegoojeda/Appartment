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

import java.util.ArrayList;
import java.util.List;

public class UserMembersForReceiptAdapter extends BaseAdapter{


    private List<User> mUsersList;
    private List<User> mExcludedUsersList;

    public UserMembersForReceiptAdapter(List<User> userList){
        this.mUsersList = userList;
        this.mExcludedUsersList = new ArrayList<>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.row_user_member_receipt, null);
        }

        final User item = mUsersList.get(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.userName)){
                ((TextView)v.findViewById(R.id.receipt_members_user_name_tv)).setText(item.userName);
            }
            if (!TextUtils.isEmpty(item.userEmail)){
                ((TextView)v.findViewById(R.id.receipt_members_user_name_tv)).setText(item.userName);
            }
            if (!TextUtils.isEmpty(item.userPictureUrl)){
                Picasso.with(parent.getContext()).load(item.userPictureUrl).into((ImageView) v.findViewById(R.id.receipt_members_user_profile_iv));
            }
            ((Switch) v.findViewById(R.id.receipt_members_add_user_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {
                        mExcludedUsersList.remove(item);
                    } else {
                        mExcludedUsersList.add(item);
                    }
                }
            });
        }
        return v;
    }

    public List<User> getMemberUsers() {
        //Esto se puede hacer directamente sobre la lista mUsersList, pero eliminar elementos de una lista
        //que puede estar compartiendo su referencia en muchos otros sitios (pertenece al grupo) no parece
        //una buena idea. Mejor no jugársela y copiar esa lista a una nueva
        List<User> memberUsers = new ArrayList<>(mUsersList);
        memberUsers.removeAll(mExcludedUsersList);
        return memberUsers;
    }
}
