package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.adapters.GroupsListAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupsGet;
import com.uma.tfg.appartment.network.requests.groups.GroupsPost;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends Activity implements View.OnClickListener, GroupsGet.GroupsGetListener,
        GroupsPost.GroupsPostListener{

    //TODO Modificar nombres de actividades

    private static final int REQUEST_CODE_CREATE_GROUP_ACTIVITY = 1001;

    private static final String KEY_SAVE_GROUPS_LIST = "IndexActivity.groupsList";

    private FloatingActionButton mCreateGroupButton;

    private ListView mMyGroupsListView;

    private GroupsListAdapter mGroupsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initViews();
//        if (AppartmentSharedPreferences.getInstallReferrer() != null){
//            Logger.d("Recibido referrer!! " + AppartmentSharedPreferences.getInstallReferrer());
//        }
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(KEY_SAVE_GROUPS_LIST)){
                mGroupsListAdapter.mGroupsList = (ArrayList) savedInstanceState.getSerializable(KEY_SAVE_GROUPS_LIST);
                onGroupsLoaded();
            }
        }
        else{
            loadUserGroups();
        }
    }

    private void loadUserGroups() {
        if (AppartmentSharedPreferences.getInstallReferrer() != null){
            String groupId = AppartmentSharedPreferences.getInstallReferrer();
            RequestsBuilder.sendAddNewMemberToGroupRequest(groupId, new GroupsPost.GroupsPostListener() {
                @Override
                public void onGroupsPostSuccess(GroupsPost.GroupsPostAction action, Group modifiedGroup) {
                    RequestsBuilder.sendGroupsGetRequest(IndexActivity.this);
                }

                @Override
                public void onGroupsPostError() {
                    Logger.e("Error al añadir un usuario a un grupo nuevo");
                }
            });
            AppartmentSharedPreferences.setInstallReferrer(null);
        }
        else{
            RequestsBuilder.sendGroupsGetRequest(this);
        }
    }

    private void initViews() {
        mCreateGroupButton = (FloatingActionButton) findViewById(R.id.create_new_group_button);
        mMyGroupsListView = (ListView) findViewById(R.id.my_groups_listview);

        mCreateGroupButton.setOnClickListener(this);

        mGroupsListAdapter = new GroupsListAdapter();
        mMyGroupsListView.setAdapter(mGroupsListAdapter);

        setOnGroupClickedListener();
    }

    private void showEmptyGroupsListLayout() {
        findViewById(R.id.index_layout).setVisibility(View.GONE);
        findViewById(R.id.index_layout_no_content).setVisibility(View.VISIBLE);
    }

    private void showNonEmptyGroupsListLayout() {
        findViewById(R.id.index_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.index_layout_no_content).setVisibility(View.GONE);
    }

    private void hideLoadingSpinnerAndShowGroupsList() {
        findViewById(R.id.loading_groups_spinner).setVisibility(View.GONE);
        findViewById(R.id.my_groups_listview).setVisibility(View.VISIBLE);
    }

    private void onGroupsLoaded() {
        if (mGroupsListAdapter.mGroupsList != null){
            if (mGroupsListAdapter.mGroupsList.isEmpty()){
                showEmptyGroupsListLayout();
            }
            else {
                hideLoadingSpinnerAndShowGroupsList();
                mGroupsListAdapter.notifyDataSetChanged();
            }
        }
        else{
            Logger.e("Hubo un error al cargar los grupos en index");
        }
    }

    private void addNewGroupToList(Group g){
        if (mGroupsListAdapter.mGroupsList.isEmpty()){
            showNonEmptyGroupsListLayout();
        }
        mGroupsListAdapter.mGroupsList.add(g);
        mGroupsListAdapter.notifyDataSetChanged();
    }

    private void setOnGroupClickedListener() {

        mMyGroupsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showGroupActionsDialog(i);
                return true;
            }
        });

        mMyGroupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToGroupDetailsActivity(mGroupsListAdapter.mGroupsList.get(position));
            }
        });
    }

    private void showGroupActionsDialog(final int groupPosition) {
        AlertDialog.Builder groupActionsDialog = new AlertDialog.Builder(IndexActivity.this);
        groupActionsDialog.setTitle("¿Qué deseas hacer?");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(IndexActivity.this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add("Abandonar grupo");
        arrayAdapter.add("Eliminar grupo");

        groupActionsDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Abandonar grupo
                        leaveGroupInPosition(groupPosition);
                        break;
                    case 1:
                        //Eliminar grupo
                        deleteGroupInPosition(groupPosition);
                        break;
                }
            }
        });
        groupActionsDialog.show();
    }

    private void deleteGroupInPosition(int position){
        RequestsBuilder.sendDeleteGroupRequest(mGroupsListAdapter.mGroupsList.get(position).mId, IndexActivity.this);
        mGroupsListAdapter.remove(position);
        if (mGroupsListAdapter.mGroupsList.isEmpty()){
            showEmptyGroupsListLayout();
        }
    }

    private void leaveGroupInPosition(int position){
        RequestsBuilder.sendLeaveGroupRequest(mGroupsListAdapter.mGroupsList.get(position).mId, IndexActivity.this);
        mGroupsListAdapter.remove(position);
        if (mGroupsListAdapter.mGroupsList.isEmpty()){
            showEmptyGroupsListLayout();
        }
    }

    private void goToGroupDetailsActivity(Group selectedGroup){
        Intent i = new Intent(IndexActivity.this, GroupDetailsActivity.class);
        i.putExtra(GroupDetailsActivity.EXTRA_GROUP, selectedGroup);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_new_group_button:
                goToCreateGroupActivity();
                break;
        }
    }

    private void goToCreateGroupActivity() {
        //TODO
        Intent i = new Intent(this, CreateGroupActivity.class);
        startActivityForResult(i, REQUEST_CODE_CREATE_GROUP_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE_GROUP_ACTIVITY){
            if (resultCode == RESULT_OK){
                Group newGroup = (Group) data.getSerializableExtra(CreateGroupActivity.EXTRA_GROUP_RESULT);
                addNewGroupToList(newGroup);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SAVE_GROUPS_LIST, new ArrayList<>(mGroupsListAdapter.mGroupsList));
    }

    @Override
    public void onGroupsReceivedSuccessfully(List<Group> groups) {
//        this.mGroupsList = groups;
        this.mGroupsListAdapter.mGroupsList = groups;
        onGroupsLoaded();
        Logger.d("Grupos recibidos satisfactoriamente");
    }

    @Override
    public void onErrorReceivingGroups() {
        Util.toast(this, "Hubo un error al cargar los grupos");
        Logger.e("Error al recibir los grupos del usuario");
    }

    @Override
    public void onGroupsPostSuccess(GroupsPost.GroupsPostAction action, Group modifiedGroup) {
        if (action == GroupsPost.GroupsPostAction.DELETE){
            Logger.d("Grupo borrado satisfactoriamente");
        }
    }

    @Override
    public void onGroupsPostError() {
        Logger.e("Error en groups post");
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
