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
        receiptNameTextView.setText(item.mName);

        return v;
    }

    public void add(Receipt receipt){
        mReceiptsList.add(receipt);
        this.notifyDataSetChanged();
    }

}
