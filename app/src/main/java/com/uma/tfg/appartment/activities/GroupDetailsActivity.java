package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.activities.fragments.ReceiptsListFragment;
import com.uma.tfg.appartment.activities.fragments.UsersListFragment;
import com.uma.tfg.appartment.adapters.GroupPagerAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.groups.GroupDetailsGet;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;
import com.uma.tfg.appartment.views.SlidingTabLayout;

public class GroupDetailsActivity extends FragmentActivity implements GroupDetailsGet.GroupDetailsGetListener,
        View.OnClickListener{

    public static final int CREATE_RECEIPT_REQUEST_CODE = 2001;

    public final static String EXTRA_GROUP = "GroupDetailsActivity.ExtraGroup";

//    private final static String SAVE_KEY_GROUP = "GroupDetailsActivity.SaveKeyGroup";

    private ProgressDialog mProgressDialog;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private GroupPagerAdapter mGroupPagerAdapter;

    private Group mGroup;

    private ReceiptsListFragment mReceiptsListFragment;
    private UsersListFragment mUsersListFragment;

    private FloatingActionButton mAddNewUserFab;
    private FloatingActionButton mAddNewReceiptFab;
    private FloatingActionsMenu mFloatingActionsMenu;

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
            initializeFloatingActionButtons();
            setTitle(mGroup.mGroupName);
            loadGroupDetails();
        }
    }

    private void initializeFloatingActionButtons() {
        mAddNewUserFab = (FloatingActionButton) findViewById(R.id.fab_action_add_new_user);
        mAddNewReceiptFab = (FloatingActionButton) findViewById(R.id.fab_action_add_new_receipt);
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab_actions_menu_group_details);

        mAddNewUserFab.setOnClickListener(this);
        mAddNewReceiptFab.setOnClickListener(this);
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
        this.mGroup = group;
        initializeFragments();
        initializeViewPager();
        hideProgressDialog();
    }

    @Override
    public void onGroupDetailsError() {
        hideProgressDialog();
        Logger.e("Error al cargar los detalles del grupo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_RECEIPT_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    if (extras.containsKey(CreateReceiptActivity.EXTRA_RESULT_RECEIPT)) {
                        Receipt receipt = (Receipt) extras.getSerializable(CreateReceiptActivity.EXTRA_RESULT_RECEIPT);
                        mReceiptsListFragment.addNewReceipt(receipt);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void onAddNewReceiptButtonPressed() {
        Intent i = new Intent(GroupDetailsActivity.this, CreateReceiptActivity.class);
        i.putExtra(CreateReceiptActivity.EXTRA_GROUP, mGroup);
        startActivityForResult(i, CREATE_RECEIPT_REQUEST_CODE);
    }

    private void onAddNewUserButtonPressed() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_action_add_new_receipt:
                mFloatingActionsMenu.collapse();
                onAddNewReceiptButtonPressed();
                break;
            case R.id.fab_action_add_new_user:
                mFloatingActionsMenu.collapse();
                onAddNewUserButtonPressed();
                break;
        }
    }
}
