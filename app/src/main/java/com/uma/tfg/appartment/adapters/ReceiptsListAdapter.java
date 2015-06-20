package com.uma.tfg.appartment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.Receipt;

import java.util.List;

public class ReceiptsListAdapter extends BaseAdapter{

    public List<Receipt> mReceiptsList;

    public ReceiptsListAdapter(List<Receipt> receiptsList){
        this.mReceiptsList = receiptsList;
    }

    @Override
    public int getCount() {
        return this.mReceiptsList != null ? mReceiptsList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return this.mReceiptsList != null ? mReceiptsList.get(i) : null;
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
            v = inflater.inflate(R.layout.row_receipt_item_list, null);
        }

        Receipt item = mReceiptsList.get(position);

        TextView receiptNameTextView = (TextView) v.findViewById(R.id.receipt_name_tv);
        TextView receiptAmountTextView = (TextView) v.findViewById(R.id.receipt_amount_tv);
        TextView receiptMembersNotPaidTextView = (TextView) v.findViewById(R.id.members_not_paid_yet_tv);

        receiptNameTextView.setText(item.mName);
        receiptAmountTextView.setText(String.valueOf(item.mAmount));
        receiptMembersNotPaidTextView.setText(String.valueOf(item.mDebtorsQuantity));

        return v;
    }

    public void add(Receipt receipt){
        mReceiptsList.add(receipt);
        this.notifyDataSetChanged();
    }

    public void add(int position, Receipt receipt){
        mReceiptsList.add(position, receipt);
        this.notifyDataSetChanged();
    }

    public void modify(Receipt receipt){
        for (int i = 0; i<mReceiptsList.size(); i++){
            if (mReceiptsList.get(i).mId.equals(receipt.mId)){
                mReceiptsList.set(i, receipt);
            }
        }
        notifyDataSetChanged();
    }

    public void delete(Receipt receipt){
        int pos = -1;
        for (int i = 0; i<mReceiptsList.size(); i++){
            if (mReceiptsList.get(i).mId.equals(receipt.mId)){
                pos = i;
            }
        }
        if (pos != -1){
            mReceiptsList.remove(pos);
            notifyDataSetChanged();
        }
    }
}
