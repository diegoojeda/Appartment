package com.uma.tfg.appartment.activities;

import android.app.Activity;
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
import com.uma.tfg.appartment.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Index extends Activity implements View.OnClickListener{

    //TODO Modificar nombres de actividades

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
                mGroupsList = (ArrayList) savedInstanceState.getSerializable(KEY_SAVE_GROUPS_LIST);
                onGroupsLoaded();
            }
        }
        else{
            loadUserGroups();
        }

        mGroupsList = new ArrayList<>();
        for (int i = 0; i<20; i++){
            Group g = new Group(""+i, "nombre" + i);
            mGroupsList.add(g);
        }
        onGroupsLoaded();//TODO Eliminar

    }

    private void loadUserGroups() {
        //TODO Comunicación server. LLamar a onGroupsLoaded en callback
    }

    private void initViews() {
        mCreateGroupButton = (Button) findViewById(R.id.create_group_button);
        mCreateFirstGroupButton = (Button) findViewById(R.id.create_first_group_button);
        mMyGroupsListView = (ListView) findViewById(R.id.my_groups_listview);

        mCreateGroupButton.setOnClickListener(this);
        mCreateFirstGroupButton.setOnClickListener(this);

        mGroupsListAdapter = new GroupsListAdapter(mGroupsList);
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
        if (mGroupsList != null){
            if (mGroupsList.isEmpty()){
                showEmptyGroupsListLayout();
            }
            else {
                hideLoadingSpinnerAndShowGroupsList();
                //No se comparte la lista entre la actividad y el adapter, no se por qué :(
                mGroupsListAdapter.mGroupsList = mGroupsList;
                mGroupsListAdapter.notifyDataSetChanged();
            }
        }
        else{
            Logger.e("Hubo un error al cargar los grupos en index");
        }
    }

    private AdapterView.OnItemClickListener getOnGroupClickedListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d("Clickado grupo en posición: " + position + " con id: " + mGroupsList.get(position).mId);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SAVE_GROUPS_LIST, new ArrayList(mGroupsList));
    }
}
