package com.uma.tfg.appartment.network.requests.receipts;

import android.text.TextUtils;

import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.model.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// GET  RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/id_recibo/?session_id=234434234324'

public class ReceiptDetailsGet extends GetRequest{

    public interface ReceiptDetailsGetListener {
        void onReceiptReceived(Receipt receipt);
        void onReceiptGetError();
    }

    public String mReceiptId;

    private ReceiptDetailsGetListener mListener;

    public ReceiptDetailsGet(String receiptId, ReceiptDetailsGetListener listener){
        this.mReceiptId = receiptId;
        this.mListener = listener;
    }

    @Override
    public Entity getEntity() {
        return Entity.RECEIPT;
    }

    @Override
    public List<String> getUrlPath() {
        if (!TextUtils.isEmpty(mReceiptId)){
            List<String> urlPath = new ArrayList<>();
            urlPath.add(mReceiptId);
            return urlPath;
        }
        return null;
    }

    @Override
    public JSONObject getUrlQueryParameters() {
        return null;
    }

    @Override
    public void processResponse(JSONObject response) throws JSONException {
        boolean failed = true;
        int rc = response.getInt("rc");
        if (rc == 0){
            failed = false;
            Receipt singleReceipt = new Receipt(response.getJSONObject("receipt"));
            if (mListener != null){
                mListener.onReceiptReceived(singleReceipt);
            }
        }
        if (mListener != null) {
            if (failed) {
                mListener.onReceiptGetError();
            }
        }
    }
}
