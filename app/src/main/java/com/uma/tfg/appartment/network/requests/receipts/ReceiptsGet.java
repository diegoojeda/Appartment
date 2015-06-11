package com.uma.tfg.appartment.network.requests.receipts;

// GET GROUP RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/?session_id=234434234324&idgroup=id'

import android.text.TextUtils;

import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.model.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsGet extends GetRequest{

    public interface ReceiptsGetListener {
        void onReceiptsGet(List<Receipt> receipts);
        void onReceiptsGetError();
    }

    private String mGroupId;
    private ReceiptsGetListener mListener;

    public ReceiptsGet(String groupId, ReceiptsGetListener listener){
        this.mGroupId = groupId;
        this.mListener = listener;
    }

    @Override
    public Entity getEntity() {
        return Entity.RECEIPT;
    }

    @Override
    public List<String> getUrlPath() {
        return null;
    }

    @Override
    public JSONObject getUrlQueryParameters() {
        if (!TextUtils.isEmpty(mGroupId)){
            JSONObject urlQueryParameters = new JSONObject();
            try {
                urlQueryParameters.put("idgroup", mGroupId);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
            //Comprobación por si salta el try/catch
            if (urlQueryParameters.length() != 0){
                return urlQueryParameters;
            }
        }
        return null;
    }

    @Override
    public void processResponse(JSONObject response) throws JSONException {
        boolean failed = true;
        int rc = response.getInt("rc");
        if (rc == 0){
            failed = false;
            List<Receipt> receiptsList = new ArrayList<>();
            JSONArray receiptsJSONArray = response.getJSONArray("receipts");
            for (int i = 0; i<receiptsJSONArray.length(); i++){
                receiptsList.add(new Receipt(receiptsJSONArray.getJSONObject(i)));
            }
            if (mListener != null){
                mListener.onReceiptsGet(receiptsList);
            }
        }
        if (mListener != null) {
            if (failed) {
                mListener.onReceiptsGetError();
            }
        }
    }

}
