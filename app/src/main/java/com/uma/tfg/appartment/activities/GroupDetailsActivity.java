package com.uma.tfg.appartment.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.activities.fragments.ReceiptsListFragment;
import com.uma.tfg.appartment.activities.fragments.UsersListFragment;
import com.uma.tfg.appartment.adapters.GroupPagerAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupDetailsGet;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;
import com.uma.tfg.appartment.views.SlidingTabLayout;

public class GroupDetailsActivity extends FragmentActivity implements GroupDetailsGet.GroupDetailsGetListener{

    public final static String EXTRA_GROUP = "GroupDetailsActivity.ExtraGroup";

//    private final static String SAVE_KEY_GROUP = "GroupDetailsActivity.SaveKeyGroup";

    private ProgressDialog mProgressDialog;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private GroupPagerAdapter mGroupPagerAdapter;

    private Group mGroup;

    private ReceiptsListFragment mReceiptsListFragment;
    private UsersListFragment mUsersListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        boolean finish = true;
//        if (savedInstanceState != null){
//            if (savedInstanceState.containsKey(SAVE_KEY_GROUP)) {
//                finish = false;
//                mGroup = (Group) savedInstanceState.getSerializable(SAVE_KEY_GROUP);
//            }
//        }
//        else{
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.containsKey(EXTRA_GROUP)){
                mGroup = (Group) extras.getSerializable(EXTRA_GROUP);
                finish = false;
            }
        }
//        }
        if (finish){
            Util.toast(this, "Error");
            finish();
        }
        else{
            setTitle(mGroup.mGroupName);
            loadGroupDetails();
        }
    }

    private void loadGroupDetails() {
        showProgressDialog();
        RequestsBuilder.sendGetGroupsDetailsRequest(mGroup.mId, this);
    }

    private void initializeFragments() {
        mReceiptsListFragment = ReceiptsListFragment.newInstance(mGroup);
        mUsersListFragment = UsersListFragment.newInstance(mGroup);
    }

    private void initializeViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mGroupPagerAdapter = new GroupPagerAdapter(getSupportFragmentManager(), mReceiptsListFragment, mUsersListFragment);
        mViewPager.setAdapter(mGroupPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGroupDetailsLoaded(Group group) {
        hideProgressDialog();
        this.mGroup = group;
        initializeFragments();
        initializeViewPager();
    }

    @Override
    public void onGroupDetailsError() {
        hideProgressDialog();
        Logger.e("Error al cargar los detalles del grupo");
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
}
