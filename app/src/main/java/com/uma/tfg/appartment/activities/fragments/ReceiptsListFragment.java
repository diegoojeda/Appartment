package com.uma.tfg.appartment.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.activities.ReceiptDetailsActivity;
import com.uma.tfg.appartment.adapters.ReceiptsListAdapter;
import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptsGet;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsListFragment extends Fragment implements ReceiptsGet.ReceiptsGetListener{

    public static final String EXTRA_GROUP = "ReceiptsListFragment.Group";
    private static final String SAVE_GROUP = "ReceiptsListFragment.SaveGroup";

    public static final int RECEIPT_DETAILS_REQUEST_CODE = 2002;

    private Group mGroup;

    private ReceiptsListAdapter mPaidReceiptsAdapter;
    private ReceiptsListAdapter mPendingReceiptsAdapter;

    private ListView mPaidReceiptsListView;
    private ListView mPendingReceiptsListView;

    private ProgressBar mLoadingSpinner;
    private LinearLayout mListsLayoutWrapper;
//    private FloatingActionButton mCreateNewReceiptButton;

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
            mPaidReceiptsAdapter = new ReceiptsListAdapter(new ArrayList<Receipt>());
            mPendingReceiptsAdapter = new ReceiptsListAdapter(new ArrayList<Receipt>());
        }
    }

    public void addNewReceipt(Receipt receipt){
        if (receipt.mEverybodyHasPaid){
            mPaidReceiptsAdapter.add(0, receipt);
        }
        else {
            mPendingReceiptsAdapter.add(0, receipt);
        }
        modifyViewsWhenReceiptsChange();
    }

    private void initializeAdapters(List<Receipt> receiptList){
        mPaidReceiptsListView.setAdapter(mPaidReceiptsAdapter);
        mPendingReceiptsListView.setAdapter(mPendingReceiptsAdapter);

        mPaidReceiptsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToReceiptDetails(mPaidReceiptsAdapter.mReceiptsList.get(i));
            }
        });

        mPendingReceiptsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToReceiptDetails(mPendingReceiptsAdapter.mReceiptsList.get(i));
            }
        });

        for (Receipt r : receiptList){
            if (r.mEverybodyHasPaid){
                mPaidReceiptsAdapter.add(r);
            }
            else{
                mPendingReceiptsAdapter.add(r);
            }
        }
        modifyViewsWhenReceiptsChange();
    }

    private void goToReceiptDetails(Receipt receipt){
        Intent i = new Intent(getActivity(), ReceiptDetailsActivity.class);
        i.putExtra(ReceiptDetailsActivity.EXTRA_KEY_RECEIPT, receipt);
        startActivityForResult(i, RECEIPT_DETAILS_REQUEST_CODE);
    }

    private void loadReceiptsForGroupId() {
        showLoadingReceiptsSpinner();
        RequestsBuilder.sendGetReceiptsRequest(mGroup.mId, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_receipts_list, container, false);
        mPaidReceiptsListView = (ListView) contentView.findViewById(R.id.paid_receipts_lv);
        mPendingReceiptsListView = (ListView) contentView.findViewById(R.id.pending_receipts_lv);

        mLoadingSpinner = (ProgressBar) contentView.findViewById(R.id.loading_receipts_progress_bar);
        mListsLayoutWrapper = (LinearLayout) contentView.findViewById(R.id.receipts_lists_layout_wrapper);

//        mCreateNewReceiptButton = (FloatingActionButton) contentView.findViewById(R.id.create_new_receipt_button);
//        mCreateNewReceiptButton.setOnClickListener(this);

        loadReceiptsForGroupId();
        return contentView;
    }

    @Override
    public void onReceiptsGet(List<Receipt> receipts) {
        hideLoadingReceiptsSpinnerAndShowLists();
        initializeAdapters(receipts);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECEIPT_DETAILS_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                if (extras != null){
                    if (extras.containsKey(ReceiptDetailsActivity.EXTRA_RETURN_RECEIPT_KEY)){
                        Receipt receipt = (Receipt) extras.getSerializable(ReceiptDetailsActivity.EXTRA_RETURN_RECEIPT_KEY);
                        if (receipt.mEverybodyHasPaid){
                            mPaidReceiptsAdapter.modify(receipt);
                            mPendingReceiptsAdapter.delete(receipt);
                        }
                        else{
                            mPendingReceiptsAdapter.modify(receipt);
                        }
                        modifyViewsWhenReceiptsChange();
                    }
                }
            }
            else if (resultCode == ReceiptDetailsActivity.RESULT_DELETED){
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Receipt receiptToDelete = (Receipt) extras.getSerializable(ReceiptDetailsActivity.EXTRA_RETURN_RECEIPT_KEY);
                    mPendingReceiptsAdapter.delete(receiptToDelete);
                    mPaidReceiptsAdapter.delete(receiptToDelete);
                    modifyViewsWhenReceiptsChange();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_GROUP, mGroup);
    }

    private void modifyViewsWhenReceiptsChange() {
        View v = getView();
        if (v != null) {
            if (mPendingReceiptsAdapter.mReceiptsList.isEmpty() && mPaidReceiptsAdapter.mReceiptsList.isEmpty()) {
                //Mostrar typefacetextview vista vacía
                v.findViewById(R.id.empty_receipts_list_tv).setVisibility(View.VISIBLE);
                v.findViewById(R.id.receipts_lists_layout_wrapper).setVisibility(View.GONE);
            } else {
                //Mostrar información en cada lista
                v.findViewById(R.id.empty_receipts_list_tv).setVisibility(View.GONE);
                v.findViewById(R.id.receipts_lists_layout_wrapper).setVisibility(View.VISIBLE);

                if (mPendingReceiptsAdapter.mReceiptsList.isEmpty()) {
                    v.findViewById(R.id.all_receipts_paid_tv).setVisibility(View.VISIBLE);
                    mPendingReceiptsListView.setVisibility(View.GONE);
                } else {
                    v.findViewById(R.id.all_receipts_paid_tv).setVisibility(View.GONE);
                    mPendingReceiptsListView.setVisibility(View.VISIBLE);
                }

                if (mPaidReceiptsAdapter.mReceiptsList.isEmpty()) {
                    v.findViewById(R.id.no_paid_receipts_tv).setVisibility(View.VISIBLE);
                    mPaidReceiptsListView.setVisibility(View.GONE);
                } else {
                    v.findViewById(R.id.no_paid_receipts_tv).setVisibility(View.GONE);
                    mPaidReceiptsListView.setVisibility(View.VISIBLE);
                }
                Util.setListViewHeightBasedOnChildren(mPaidReceiptsListView);
                Util.setListViewHeightBasedOnChildren(mPendingReceiptsListView);
            }
        }
    }
}
