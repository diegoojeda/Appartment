package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.adapters.UserMembersForReceiptAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.model.User;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptsPost;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

import java.util.ArrayList;
import java.util.List;

public class CreateReceiptActivity extends Activity implements ReceiptsPost.ReceiptsPostListener{
    public static final String EXTRA_RESULT_RECEIPT = "CreateReceiptActivity.ExtraResultReceipt";

    public static final String EXTRA_GROUP = "CreateReceiptActivity.ExtraGroup";

    private static final String SAVE_KEY_GROUP = "CreateReceiptActivity.SaveKeyGroup";

    private Group mFullGroup;

    private UserMembersForReceiptAdapter mMembersForReceiptAdapter;

    private EditText mReceiptNameEditText;
    private EditText mReceiptQuantityEditText;
    private ListView mReceiptMembersListView;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_receipt);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        boolean failed = true;
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVE_KEY_GROUP)){
                mFullGroup = (Group) savedInstanceState.getSerializable(SAVE_KEY_GROUP);
            }
            failed = false;
        }
        else{
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                if (extras.containsKey(EXTRA_GROUP)){
                    failed = false;
                    mFullGroup = (Group)extras.getSerializable(EXTRA_GROUP);
                }
            }
        }
        if (failed){
            finishWithError();
        }
        else{
            initializeViews();
        }
    }

    private void initializeViews() {
        mReceiptNameEditText = (EditText) findViewById(R.id.receipt_name_edit_text);
        mReceiptQuantityEditText = (EditText) findViewById(R.id.receipt_quantity_edit_text);
        mReceiptMembersListView = (ListView) findViewById(R.id.receipt_members_lv);

        mMembersForReceiptAdapter = new UserMembersForReceiptAdapter(mFullGroup.mMembers);
        mReceiptMembersListView.setAdapter(mMembersForReceiptAdapter);
        mMembersForReceiptAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_receipt, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_create_receipt:
                onCreateReceiptActionTaken();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendCreateReceiptRequest() {
        showProgressDialog();
        RequestsBuilder.sendCreateReceiptRequest(
                mFullGroup.mId, Double.parseDouble(mReceiptQuantityEditText.getText().toString()),
                getDebtorsList(),
                mReceiptNameEditText.getText().toString(),
                this
        );
    }

    private List<String> getDebtorsList() {
        List<String> debtors = new ArrayList<>();
        for (User u : mMembersForReceiptAdapter.getMemberUsers()) {
            debtors.add(u.userId);
        }
        return debtors;
    }

    private void onCreateReceiptActionTaken() {
        if (validateFields()){
            sendCreateReceiptRequest();
        }
        else{

        }
    }

    private boolean validateFields() {
        boolean valid = true;
        if (mReceiptNameEditText.length() == 0){
            Util.toast(this, "Escribe un nombre para el recibo");
            valid = false;
        }
        try{
            Double.parseDouble(mReceiptQuantityEditText.getText().toString());
        }
        catch (NumberFormatException ex){
            valid = false;
            Util.toast(this, "Escriba una cantidad válida");
        }
        if (mMembersForReceiptAdapter.getMemberUsers().isEmpty()){
            Util.toast(this, "Debe seleccionar algún amigo");
            valid = false;
        }
        return valid;
    }

    private void finishWithError() {
        //TODO
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Cargando…");
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    @Override
    public void onReceiptsPostSuccess(Receipt singleReceipt) {
        hideProgressDialog();
        Intent resultData = new Intent();
        resultData.putExtra(EXTRA_RESULT_RECEIPT, singleReceipt);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    @Override
    public void onReceiptsPostError() {
        hideProgressDialog();
        Logger.e("Error al crear el recibo");
        Util.toast(this, "Hubo un error al crear el recibo");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_KEY_GROUP, mFullGroup);
    }
}
