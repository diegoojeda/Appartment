package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.adapters.GroupsListAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupsGet;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Index extends Activity implements View.OnClickListener, GroupsGet.GroupsGetListener{

    //TODO Modificar nombres de actividades

    private static final int REQUEST_CODE_CREATE_GROUP_ACTIVITY = 1001;

    private static final String KEY_SAVE_GROUPS_LIST = "Index.groupsList";

    private Button mCreateGroupButton;
    private Button mCreateFirstGroupButton;

    private ListView mMyGroupsListView;

    private List<Group> mGroupsList;

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
//
//        for (int i = 0; i<5; i++){
//            Group g = new Group(""+i, "nombre" + i, new ArrayList<String>());
//            mGroupsListAdapter.mGroupsList.add(g);
//        }
//        onGroupsLoaded();//TODO Eliminar

    }

    private void loadUserGroups() {
        //TODO Comunicación server. LLamar a onGroupsLoaded en callback
        RequestsBuilder.sendGroupsGetRequest(this);
    }

    private void initViews() {
        mCreateGroupButton = (Button) findViewById(R.id.create_group_button);
        mCreateFirstGroupButton = (Button) findViewById(R.id.create_first_group_button);
        mMyGroupsListView = (ListView) findViewById(R.id.my_groups_listview);

        mCreateGroupButton.setOnClickListener(this);
        mCreateFirstGroupButton.setOnClickListener(this);

        mGroupsListAdapter = new GroupsListAdapter();
        mMyGroupsListView.setAdapter(mGroupsListAdapter);

        mMyGroupsListView.setOnItemClickListener(getOnGroupClickedListener());
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
                //No se comparte la lista entre la actividad y el adapter, no se por qué :(
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

    private AdapterView.OnItemClickListener getOnGroupClickedListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d("Clickado grupo en posición: " + position + " con id: " + mGroupsListAdapter.mGroupsList.get(position).mId);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_group_button:
                goToCreateGroupActivity();
                break;
            case R.id.create_first_group_button:
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
//                String groupName = data.getStringExtra(CreateGroupActivity.EXTRA_GROUP_NAME_RESULT);
//                ArrayList<String> groupEmails = data.getStringArrayListExtra(CreateGroupActivity.EXTRA_EMAIL_LIST_RESULT);
//                Group createdGroup = new Group("0", groupName, groupEmails);//TODO Sustituir "0" por el id del grupo una vez creado
//                Logger.d("Se creó el grupo: " + createdGroup.mId);
//                addNewGroupToList(createdGroup);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SAVE_GROUPS_LIST, new ArrayList<>(mGroupsListAdapter.mGroupsList));
    }

    @Override
    public void onGroupsReceivedSuccessfully(List<Group> groups) {
        this.mGroupsList = groups;
        onGroupsLoaded();
        Logger.d("Grupos recibidos satisfactoriamente");
    }

    @Override
    public void onErrorReceivingGroups() {
        Util.toast(this, "Hubo un error con los grupos");
        Logger.e("Error al recibir los grupos del usuario");
    }
}
