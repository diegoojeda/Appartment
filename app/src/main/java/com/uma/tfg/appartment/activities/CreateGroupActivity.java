package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupsPost;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;
import com.uma.tfg.appartment.views.MultiEmailAdderView;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends Activity implements GroupsPost.GroupsPostListener{

    public final static String EXTRA_GROUP_RESULT = "CreateGroupActivity.GroupResult";

//    public final static String EXTRA_EMAIL_LIST_RESULT = "CreateGroupActivity.EmailListResult";
//    public final static String EXTRA_GROUP_NAME_RESULT = "CreateGroupActivity.GroupNameResult";

    private final static String KEY_SAVE_EMAIL_LIST = "CreateGroupActivity.EmailListKey";
    private final static String KEY_SAVE_GROUP_NAME = "CreateGroupActivity.GroupNameKey";

    private ProgressDialog mProgressDialog;

    private EditText mGroupNameEditText;
    private MultiEmailAdderView mMultiEmailAdderView;

    private String mGroupName;
    private List<String> mEmailsList;

    private Group mGroupToReturn;//TODO Eliminar, el grupo debería volver del servidor cuando se crea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(KEY_SAVE_EMAIL_LIST)){
                mEmailsList = savedInstanceState.getStringArrayList(KEY_SAVE_EMAIL_LIST);
            }
            if (savedInstanceState.containsKey(KEY_SAVE_GROUP_NAME)){
                mGroupName = savedInstanceState.getString(KEY_SAVE_GROUP_NAME);
            }
        }
        init();
    }

    private void init() {
        if (mEmailsList == null){
            mEmailsList = new ArrayList<>();
        }
        else{
            if (!mEmailsList.isEmpty()){
                mMultiEmailAdderView.initWithEmailsList(mEmailsList);
            }
        }
        mEmailsList = new ArrayList<>();
    }

    private void initViews() {
        mGroupNameEditText = (EditText) findViewById(R.id.group_creation_name_edit_text);
        mMultiEmailAdderView = (MultiEmailAdderView) findViewById(R.id.multi_email_adder);
    }

    private void onFinishActionPressed() {
        if (mGroupNameEditText.length() == 0){
            Util.toast(this, "El nombre del grupo no puede ser vacío");
        }
        else {
            if (mMultiEmailAdderView.validateAllEmails()) {
                mEmailsList = mMultiEmailAdderView.getEmailList();
                mGroupName = mGroupNameEditText.getText().toString();
                createGroupOnServer();
            } else {
                Util.toast(this, "Hay algún E-mail inválido, revísalos");
            }
        }
    }

    private void createGroupOnServer() {
        showLoadingDialog("Creando grupo…");
        RequestsBuilder.sendCreateGroupRequest(mGroupName, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_SAVE_EMAIL_LIST, new ArrayList<>(mEmailsList));
        outState.putString(KEY_SAVE_GROUP_NAME, mGroupNameEditText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_finish_group_creation:
                onFinishActionPressed();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGroupsPostSuccess(GroupsPost.GroupsPostAction action, Group modifiedGroup) {
        hideLoadingDialog();
        if (action == GroupsPost.GroupsPostAction.INSERT) {
            mGroupToReturn = modifiedGroup;
            inviteFriendsToCreatedGroup(mGroupToReturn);
        }
        else if (action == GroupsPost.GroupsPostAction.INVITE){
            Util.toast(this, "Grupo creado correctamente");
            Intent resultData = new Intent();
            resultData.putExtra(EXTRA_GROUP_RESULT, mGroupToReturn);
            setResult(RESULT_OK, resultData);
            finish();
        }
        else{
            Logger.e("Error en GroupsPost");
        }
    }

    @Override
    public void onGroupsPostError() {
        hideLoadingDialog();
        Util.toast(this, "Hubo un error al crear el grupo");
        Logger.e("Error al crear el grupo en el servidor");
    }

    private void inviteFriendsToCreatedGroup(Group createdGroup) {
        showLoadingDialog("Invitando amigos…");
//        mEmailsList.add(AppartmentSharedPreferences.getUserEmail());
        RequestsBuilder.sendInviteToGroupRequest(createdGroup.mId, mEmailsList, this);
    }

    private void showLoadingDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }
}
