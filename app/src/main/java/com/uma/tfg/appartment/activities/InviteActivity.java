package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupsPost;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;
import com.uma.tfg.appartment.views.MultiEmailAdderView;

import java.util.ArrayList;

public class InviteActivity extends Activity implements GroupsPost.GroupsPostListener{

    public static final String EXTRA_GROUP_ID = "InviteActivity.ExtraGroupId";

    private static final String SAVE_GROUP_ID = "InviteActivity.SaveGroupId";
    private static final String SAVE_EMAILS_LIST = "InviteActivity.SaveEmailsList";

    private String mGroupId;
    private ArrayList<String> mEmailsList;
    private MultiEmailAdderView mMultiEmailAdderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        boolean failed = true;
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVE_GROUP_ID) && savedInstanceState.containsKey(SAVE_EMAILS_LIST)){
                mGroupId = savedInstanceState.getString(SAVE_GROUP_ID);
                mEmailsList = savedInstanceState.getStringArrayList(SAVE_EMAILS_LIST);
                failed = false;
            }
        }
        else{
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                if (extras.containsKey(EXTRA_GROUP_ID)){
                    mGroupId = extras.getString(EXTRA_GROUP_ID);
                    mEmailsList = new ArrayList<>();
                    failed = false;
                }
            }
        }
        if (failed){
            finishActivityWithError();
        }
        else{
            loadViews();
        }
    }

    private void finishActivityWithError(){
        //TODO
    }

    private void loadViews() {
        mMultiEmailAdderView = (MultiEmailAdderView) findViewById(R.id.invite_multi_email_adder_view);
        mMultiEmailAdderView.initWithEmailsList(mEmailsList);
    }

    private void onInviteButtonPressed() {
        if (mMultiEmailAdderView.validateAllEmails()) {
            mEmailsList = mMultiEmailAdderView.getEmailList();
            if (mEmailsList.isEmpty()){
                Util.toast(this, "Escribe al menos una dirección de correo");
            }
            else{
                RequestsBuilder.sendInviteToGroupRequest(mGroupId, mEmailsList, this);
            }
        } else {
            Util.toast(this, "Hay algún E-mail inválido, revísalos");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_invite:
                onInviteButtonPressed();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_GROUP_ID, mGroupId);
        outState.putStringArrayList(SAVE_EMAILS_LIST, mEmailsList);
    }

    @Override
    public void onGroupsPostSuccess(GroupsPost.GroupsPostAction action, Group modifiedGroup) {
        Logger.d("Invitado con éxito");
        Util.toast(this, "¡Amigos invitados con éxito!");
        finish();
    }

    @Override
    public void onGroupsPostError() {
        Util.toast(this, "Hubo un error al invitar a tus amigos al grupo");
        Logger.e("Error al invitar");
    }
}
