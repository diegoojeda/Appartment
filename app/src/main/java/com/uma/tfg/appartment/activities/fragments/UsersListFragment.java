package com.uma.tfg.appartment.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.adapters.UsersListAdapter;
import com.uma.tfg.appartment.model.Group;

public class UsersListFragment extends Fragment {

    private static final String EXTRA_GROUP = "UsersListFragment.Group";

    private static final String SAVE_GROUP = "UsersListFragment.SaveGroup";

    private Group mGroup;

    private UsersListAdapter mUsersListAdapter;
    private ListView mUsersListView;

    public static UsersListFragment newInstance(Group group) {
        UsersListFragment fragment = new UsersListFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean failed = true;
        if (getArguments() != null) {
            mGroup = (Group)getArguments().getSerializable(EXTRA_GROUP);
            if (mGroup != null && !TextUtils.isEmpty(mGroup.mId)){
                failed = false;
            }
        }
        else{
            if (savedInstanceState != null){
                if (savedInstanceState.containsKey(SAVE_GROUP)){
                    mGroup = (Group) savedInstanceState.getSerializable(SAVE_GROUP);
                    if (mGroup != null && !TextUtils.isEmpty(mGroup.mId)){
                        failed = false;
                    }
                }
            }
        }
        if (failed){
            finishWithError();
        }
    }

    private void initializeUsersListView() {
        mUsersListAdapter = new UsersListAdapter(mGroup.mMembers);
        mUsersListView.setAdapter(mUsersListAdapter);
        mUsersListAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_users_list, container, false);

        mUsersListView = (ListView) contentView.findViewById(R.id.users_listview);

        initializeUsersListView();

        return contentView;
    }

    private void finishWithError() {
        //TODO
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_GROUP, mGroup);
    }
}
