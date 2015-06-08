package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupDetailsGet;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

public class GroupDetailsActivity extends Activity implements GroupDetailsGet.GroupDetailsGetListener {

    public final static String EXTRA_GROUP = "GroupDetailsActivity.ExtraGroup";

    private final static String SAVE_KEY_GROUP = "GroupDetailsActivity.SaveKeyGroup";

    private Group mGroup;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        boolean finish = true;
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVE_KEY_GROUP)) {
                finish = false;
                mGroup = (Group) savedInstanceState.getSerializable(SAVE_KEY_GROUP);
            }
        }
        else{
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                if (extras.containsKey(EXTRA_GROUP)){
                    mGroup = (Group) extras.getSerializable(EXTRA_GROUP);
                    finish = false;
                }
            }
        }
        if (finish){
            Util.toast(this, "Error");
            finish();
        }
        else{
            loadGroupDetails();
        }
    }

    private void loadGroupDetails() {
        if (mGroup != null && !TextUtils.isEmpty(mGroup.mId)) {
            showProgressDialog("Cargando…");
            RequestsBuilder.sendGetGroupsDetailsRequest(mGroup.mId, this);
        }
        else{
            Logger.e("Error, mGroup es null o no tiene ID");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog(String text){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(text);
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onGroupDetailsLoaded(Group group) {
        hideProgressDialog();
        mGroup = group;
    }

    @Override
    public void onGroupDetailsError() {
        hideProgressDialog();
        Util.toast(this, "Error al cargar los detalles del grupo");
        Logger.e("Error al cargar los detalles del grupo");
    }
}
