package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
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

public class Index extends Activity implements View.OnClickListener, GroupsGet.GroupsGetListener,
        GroupsPost.GroupsPostListener{

    //TODO Modificar nombres de actividades

    private static final int REQUEST_CODE_CREATE_GROUP_ACTIVITY = 1001;

    private static final String KEY_SAVE_GROUPS_LIST = "Index.groupsList";

    private FloatingActionButton mCreateGroupButton;

    private ListView mMyGroupsListView;

    private GroupsListAdapter mGroupsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initViews();
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
        RequestsBuilder.sendGroupsGetRequest(this);
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
        mGroupsListAdapter.mGroupsList.add(g);
        mGroupsListAdapter.notifyDataSetChanged();
    }

    private void setOnGroupClickedListener() {
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
            new ListViewAdapter(mMyGroupsListView),
            new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListViewAdapter view, int position) {
                    RequestsBuilder.sendDeleteGroupRequest(mGroupsListAdapter.mGroupsList.get(position).mId, Index.this);
                    mGroupsListAdapter.remove(position);
                }
            });
        mMyGroupsListView.setOnTouchListener(touchListener);
        mMyGroupsListView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        mMyGroupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Util.toast(Index.this, "Position" + position);
                }
            }
        });


//        return new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Logger.d("Clickado grupo en posición: " + position + " con id: " + mGroupsListAdapter.mGroupsList.get(position).mId);
//            }
//        };
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
}
