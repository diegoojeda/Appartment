package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.adapters.UsersInReceiptDetailsAdapter;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.model.User;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptDetailsGet;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptsPost;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

public class ReceiptDetailsActivity extends Activity implements
        ReceiptDetailsGet.ReceiptDetailsGetListener, View.OnClickListener{

    private final static String SAVE_KEY_RECEIPT = "ReceiptDetailsActivity.SaveKeyReceipt";

    public final static String EXTRA_KEY_RECEIPT = "ReceiptDetailsActivity.ExtraKeyReceipt";
    public final static String EXTRA_RETURN_RECEIPT_KEY = "ReceiptDetailsActivity.ExtraReturnReceiptKey";

    public final static int RESULT_DELETED = -10;

    private Receipt mReceipt;
    private UsersInReceiptDetailsAdapter mUsersListAdapter;

    private ProgressDialog mProgressDialog;

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
            setTitle(mReceipt.mName);
            showReceiptInformation();
            loadFullReceipt();
        }
    }

    private void showReceiptInformation() {
        TextView receiptDetailsNameTv = (TextView) findViewById(R.id.receipt_details_name_tv);
        TextView receiptDetailsAmountTv = (TextView) findViewById(R.id.receipt_details_amount_tv);
        findViewById(R.id.delete_receipt_button).setOnClickListener(this);
        findViewById(R.id.user_has_paid_button).setOnClickListener(this);
        findViewById(R.id.user_has_paid_button).setEnabled(false);
        receiptDetailsNameTv.setText(mReceipt.mName);
        receiptDetailsAmountTv.setText(String.valueOf(mReceipt.mAmount) + "€");
//        receiptDetailsCreatorTv.setText(mReceipt.mCreatorId);
    }

    private boolean userHasPaid() {
        String userId = AppartmentSharedPreferences.getUserId();
        for (User u : mReceipt.mUsersList){
            if (u.userId.equals(userId)){
                return u.userHasPaid;
            }
        }
        return false;
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

        mUsersListAdapter = new UsersInReceiptDetailsAdapter(mReceipt.mUsersList);
        usersListView.setAdapter(mUsersListAdapter);
        mUsersListAdapter.notifyDataSetChanged();
        if (userHasPaid()){
            hideViewsOnceUserHasPaid();
        }
        else{
            findViewById(R.id.user_has_paid_button).setEnabled(true);
        }

        TextView receiptDetailsCreatorTv = (TextView) findViewById(R.id.receipt_creator_tv);
        receiptDetailsCreatorTv.setText(mReceipt.mCreator.userName);
    }

    @Override
    public void onReceiptReceived(Receipt receipt) {
        mReceipt = receipt;
        showReceiptDetails();
        findViewById(R.id.loading_users_spinner).setVisibility(View.GONE);
        findViewById(R.id.receipt_details_user_list_lv).setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceiptGetError() {
        Logger.e("Error al recibir los detalles de un recibo");
        Util.toast(this, "Error cargando los detalles del recibo");
        finishActivityWithError();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_receipt_details, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

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

    private void finishActivityAndReturnReceipt(int resultCode) {
        Intent i = new Intent();
//        mReceipt.mUsersList = mUsersListAdapter.mUsersList;
        boolean everybodyHasPaid = true;
        int pending = 0;
        for (User u : mReceipt.mUsersList){
            if (!u.userHasPaid){
                pending++;
                everybodyHasPaid = false;
            }
        }
        mReceipt.mDebtorsQuantity = pending;
        mReceipt.mEverybodyHasPaid = everybodyHasPaid;
        i.putExtra(EXTRA_RETURN_RECEIPT_KEY, mReceipt);
        setResult(resultCode, i);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivityAndReturnReceipt(Activity.RESULT_OK);
    }

    private void showLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Espere…");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private void hideLoadingDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    private void onDeleteReceiptButtonPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Eliminar?");
        builder.setMessage("¿Seguro que desea eliminar este recibo?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showLoadingDialog();
                RequestsBuilder.sendDeleteReceiptRequest(mReceipt.mId, new ReceiptsPost.ReceiptsPostListener() {
                    @Override
                    public void onReceiptsPostSuccess(Receipt singleReceipt) {
                        Util.toast(ReceiptDetailsActivity.this, "Recibo borrado con éxito");
                        hideLoadingDialog();
                        finishActivityAndReturnReceipt(RESULT_DELETED);
                    }

                    @Override
                    public void onReceiptsPostError() {
                        Util.toast(ReceiptDetailsActivity.this, "Hubo un error al borrar el recibo");
                        hideLoadingDialog();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void hideViewsOnceUserHasPaid() {
        findViewById(R.id.user_has_paid_button).setVisibility(View.GONE);
        findViewById(R.id.view_separator_buttons).setVisibility(View.GONE);
    }

    private void onUserHasPaidButtonClicked() {
        Util.toast(this, "Marcado el recibo como pagado");
        hideViewsOnceUserHasPaid();
        String userId = AppartmentSharedPreferences.getUserId();
        for (User u : mReceipt.mUsersList){
            if (u.userId.equals(userId)){
                u.userHasPaid = true;
            }
        }
        RequestsBuilder.sendUserHasPaidReceiptRequest(mReceipt.mId, null);
        mUsersListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_receipt_button:
                onDeleteReceiptButtonPressed();
                break;
            case R.id.user_has_paid_button:
                onUserHasPaidButtonClicked();
                break;
        }
    }
}
