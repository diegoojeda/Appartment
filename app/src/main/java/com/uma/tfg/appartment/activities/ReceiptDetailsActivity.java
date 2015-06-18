package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.adapters.UsersInReceiptDetailsAdapter;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptDetailsGet;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

public class ReceiptDetailsActivity extends Activity implements ReceiptDetailsGet.ReceiptDetailsGetListener{

    private final static String SAVE_KEY_RECEIPT = "ReceiptDetailsActivity.SaveKeyReceipt";

    public final static String EXTRA_KEY_RECEIPT = "ReceiptDetailsActivity.ExtraKeyReceipt";

    private Receipt mReceipt;
    private UsersInReceiptDetailsAdapter mUsersListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        boolean failed = true;
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVE_KEY_RECEIPT)){
                failed = false;
                mReceipt = (Receipt) savedInstanceState.getSerializable(SAVE_KEY_RECEIPT);
            }
        }
        else{
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                if (extras.containsKey(EXTRA_KEY_RECEIPT)){
                    failed = false;
                    mReceipt = (Receipt) extras.getSerializable(EXTRA_KEY_RECEIPT);
                }
            }
        }
        if (failed){
            finishActivityWithError();
        }
        else{
            showReceiptInformation();
            loadFullReceipt();
        }
    }

    private void showReceiptInformation() {
        TextView receiptDetailsNameTv = (TextView) findViewById(R.id.receipt_details_name_tv);
        TextView receiptDetailsAmountTv = (TextView) findViewById(R.id.receipt_details_amount_tv);
        TextView receiptDetailsCreatorTv = (TextView) findViewById(R.id.receipt_creator_tv);

        receiptDetailsNameTv.setText(mReceipt.mName);
        receiptDetailsAmountTv.setText(String.valueOf(mReceipt.mAmount));
        receiptDetailsCreatorTv.setText(mReceipt.mCreatorId);
    }

    private void finishActivityWithError() {
        Logger.e("FinishActivityWithError -> ReceiptDetailsActivity");
        //TODO
    }

    private void loadFullReceipt() {
        if (!TextUtils.isEmpty(mReceipt.mId)) {
            RequestsBuilder.sendGetReceiptDetailsRequest(mReceipt.mId, this);
        }
        else{
            finishActivityWithError();
        }
    }

    private void showReceiptDetails() {
        ListView usersListView = (ListView) findViewById(R.id.receipt_details_user_list_lv);

        mUsersListAdapter = new UsersInReceiptDetailsAdapter(mReceipt.mPaid, mReceipt.mDebtors);
        usersListView.setAdapter(mUsersListAdapter);
        mUsersListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiptReceived(Receipt receipt) {
        findViewById(R.id.loading_users_spinner).setVisibility(View.GONE);
        findViewById(R.id.receipt_details_user_list_lv).setVisibility(View.VISIBLE);
        mReceipt = receipt;
        showReceiptDetails();
    }

    @Override
    public void onReceiptGetError() {
        Logger.e("Error al recibir los detalles de un recibo");
        Util.toast(this, "Error cargando los detalles del recibo");
        finishActivityWithError();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
