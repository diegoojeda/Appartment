package com.uma.tfg.appartment.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.activities.CreateReceiptActivity;
import com.uma.tfg.appartment.adapters.ReceiptsListAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptsGet;
import com.uma.tfg.appartment.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsListFragment extends Fragment implements ReceiptsGet.ReceiptsGetListener, View.OnClickListener{

    public static final String EXTRA_GROUP = "ReceiptsListFragment.Group";
    private static final String SAVE_GROUP = "ReceiptsListFragment.SaveGroup";

    public static final int CREATE_RECEIPT_REQUEST_CODE = 2001;

    private Group mGroup;

    private ReceiptsListAdapter mPayedReceiptsAdapter;
    private ReceiptsListAdapter mPendingReceiptsAdapter;

    private ListView mPayedReceiptsListView;
    private ListView mPendingReceiptsListView;

    private ProgressBar mLoadingSpinner;
    private LinearLayout mListsLayoutWrapper;
    private FloatingActionButton mCreateNewReceiptButton;

    public static ReceiptsListFragment newInstance(Group group) {
        ReceiptsListFragment fragment = new ReceiptsListFragment();
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
        else{
            mPayedReceiptsAdapter = new ReceiptsListAdapter(new ArrayList<Receipt>());
            mPendingReceiptsAdapter = new ReceiptsListAdapter(new ArrayList<Receipt>());
        }
    }

    private void initializeAdapters(List<Receipt> receiptList){
        mPayedReceiptsListView.setAdapter(mPayedReceiptsAdapter);
        mPendingReceiptsListView.setAdapter(mPendingReceiptsAdapter);

        for (Receipt r : receiptList){
            if (r.mEverybodyHasPayed){
                mPayedReceiptsAdapter.add(r);
            }
            else{
                mPendingReceiptsAdapter.add(r);
            }
        }
        mPayedReceiptsAdapter.notifyDataSetChanged();
        mPendingReceiptsAdapter.notifyDataSetChanged();
    }

    private void loadReceiptsForGroupId() {
        showLoadingReceiptsSpinner();
        RequestsBuilder.sendGetReceiptsRequest(mGroup.mId, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_receipts_list, container, false);
        mPayedReceiptsListView = (ListView) contentView.findViewById(R.id.payed_receipts_lv);
        mPendingReceiptsListView = (ListView) contentView.findViewById(R.id.pending_receipts_lv);

        mLoadingSpinner = (ProgressBar) contentView.findViewById(R.id.loading_receipts_progress_bar);
        mListsLayoutWrapper = (LinearLayout) contentView.findViewById(R.id.receipts_lists_layout_wrapper);

        mCreateNewReceiptButton = (FloatingActionButton) contentView.findViewById(R.id.create_new_receipt_button);
        mCreateNewReceiptButton.setOnClickListener(this);

        loadReceiptsForGroupId();
        return contentView;
    }

    @Override
    public void onReceiptsGet(List<Receipt> receipts) {
        hideLoadingReceiptsSpinnerAndShowLists();
        if (receipts.isEmpty()){
            showEmptyReceiptsListInformation();
        }
        else {
            initializeAdapters(receipts);
        }
    }

    @Override
    public void onReceiptsGetError() {
        hideLoadingReceiptsSpinnerAndShowLists();
        Logger.e("Error al cargar la lista de recibos");
        finishWithError();
    }

    public void showLoadingReceiptsSpinner() {
        if (mLoadingSpinner != null && mListsLayoutWrapper != null) {
            mLoadingSpinner.setVisibility(View.VISIBLE);
            mListsLayoutWrapper.setVisibility(View.GONE);
        }
    }

    public void hideLoadingReceiptsSpinnerAndShowLists() {
        if (mLoadingSpinner != null && mListsLayoutWrapper != null) {
            mLoadingSpinner.setVisibility(View.GONE);
            mListsLayoutWrapper.setVisibility(View.VISIBLE);
        }
    }

    public void finishWithError() {
        //TODO
    }

    public void onCreateNewReceiptButtonPressed() {
        Intent i = new Intent(getActivity(), CreateReceiptActivity.class);
        i.putExtra(CreateReceiptActivity.EXTRA_GROUP, mGroup);
        startActivityForResult(i, CREATE_RECEIPT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_RECEIPT_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                hideEmptyReceiptsListInformation();
                Bundle extras = data.getExtras();
                if (extras != null) {
                    if (extras.containsKey(CreateReceiptActivity.EXTRA_RESULT_RECEIPT)) {
                        Receipt receipt = (Receipt) extras.getSerializable(CreateReceiptActivity.EXTRA_RESULT_RECEIPT);
                        mPendingReceiptsAdapter.add(receipt);
                    }
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_new_receipt_button:
                onCreateNewReceiptButtonPressed();
                break;
        }
    }

    private void showEmptyReceiptsListInformation() {
        View v = getView();
        if (v != null) {
            v.findViewById(R.id.empty_receipts_list_tv).setVisibility(View.VISIBLE);
            v.findViewById(R.id.receipts_lists_layout_wrapper).setVisibility(View.GONE);
        }
    }

    private void hideEmptyReceiptsListInformation() {
        View v = getView();
        if (v != null) {
            v.findViewById(R.id.empty_receipts_list_tv).setVisibility(View.GONE);
            v.findViewById(R.id.receipts_lists_layout_wrapper).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_GROUP, mGroup);
    }
}
