package com.uma.tfg.appartment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.User;

import java.util.List;

public class UsersInReceiptDetailsAdapter extends BaseAdapter {

    public List<User> mPaidUsersList;
    public List<User> mDebtorsUsersList;

    public UsersInReceiptDetailsAdapter(List<User> paidUsersList, List<User> debtorsUsersList){
        this.mPaidUsersList = paidUsersList;
        this.mDebtorsUsersList = debtorsUsersList;
    }

    @Override
    public int getCount() {
        return (mPaidUsersList == null || mDebtorsUsersList == null) ? 0 : mPaidUsersList.size() + mDebtorsUsersList.size();
    }

    @Override
    public Object getItem(int i) {
        if (mPaidUsersList == null || mDebtorsUsersList == null){
            return null;
        }
        if (i <= mDebtorsUsersList.size()){
            return mDebtorsUsersList.get(i);
        }
        else{
            return mPaidUsersList.get(i);
        }
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

        User item;
        if (position <= mDebtorsUsersList.size()){
            item = mDebtorsUsersList.get(position);
        }
        else{
            item = mPaidUsersList.get(position);
        }

        TextView userNameTv = (TextView) v.findViewById(R.id.receipt_user_name_tv);
        TextView userEmailTv = (TextView) v.findViewById(R.id.receipt_user_email_tv);
        Switch userHasPaidSwitch = (Switch) v.findViewById(R.id.switch_receipt_paid);

        userNameTv.setText(item.userName);
        userEmailTv.setText(item.userEmail);
        userHasPaidSwitch.setChecked(!(position <= mDebtorsUsersList.size()));

        return v;
    }
}
