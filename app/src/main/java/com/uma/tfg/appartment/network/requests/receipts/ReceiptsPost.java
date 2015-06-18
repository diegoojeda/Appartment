package com.uma.tfg.appartment.network.requests.receipts;

import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.model.PostRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// INSERT receipt -> 'http://appartment-pruebamarja.rhcloud.com/receipt/?session_id=234434234324' Y action => insert Y idgroup Y amount Y debtors => array() Y namereceipt
// DELETE  RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/id_recibo/?session_id=234434234324' Y action => delete

public class ReceiptsPost extends PostRequest{

    public interface ReceiptsPostListener{
        void onReceiptsPostSuccess(Receipt singleReceipt);
        void onReceiptsPostError();
    }

    public enum ReceiptsPostActions {
        INSERT("insert"),
        UPDATE("update"),
        DELETE("delete");

        private String action;

        ReceiptsPostActions(String action){
            this.action = action;
        }

        public String toString() {
            return action;
        }
    }

    private ReceiptsPostActions mAction;

    public String mGroupId;//Usado para insert
    public String mReceiptName;
    public List<String> mDebtorsIdsList;

    public String mReceiptId;//Usado para delete/update

    public double mAmount;

    private ReceiptsPostListener mListener;

    public ReceiptsPost(ReceiptsPostActions action, ReceiptsPostListener listener){
        this.mAction = action;
        this.mListener = listener;
    }

    @Override
    public List<NameValuePair> getPostParameters() {
        List<NameValuePair> postParameters = new ArrayList<>();
        switch (mAction) {
            case INSERT:
                postParameters.add(new BasicNameValuePair("idgroup", mGroupId));
                postParameters.add(new BasicNameValuePair("amount", String.valueOf(mAmount)));
                postParameters.add(new BasicNameValuePair("namereceipt", mReceiptName));
                postParameters.add(new BasicNameValuePair("debtors", new JSONArray(mDebtorsIdsList).toString()));
                break;
            case DELETE:

                break;
            case UPDATE:

                break;
        }
        return postParameters;
    }

    @Override
    public String getAction() {
        return mAction.toString();
    }

    @Override
    public Entity getEntity() {
        return Entity.RECEIPT;
    }

    @Override
    public List<String> getUrlPath() {
        List<String> urlPathList = new ArrayList<>();
        if (mAction == ReceiptsPostActions.DELETE){
            urlPathList.add(mReceiptId);
        }
        return urlPathList;
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
            Receipt singleReceipt = new Receipt(response.getJSONObject("inserted"));
            if (mListener != null){
                mListener.onReceiptsPostSuccess(singleReceipt);
            }
        }
        if (mListener != null) {
            if (failed) {
                mListener.onReceiptsPostError();
            }
        }
    }
}
