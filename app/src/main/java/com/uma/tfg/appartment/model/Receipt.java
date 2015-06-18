package com.uma.tfg.appartment.model;

import com.uma.tfg.appartment.util.JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*Lista de recibos
{
"_id":"55707386d839dadceb8b4567",
"creator":1111,
"cdt":1433432966,
"amount":"11",
"debtors":[
{
"status":1,
"id":"1111"
}
]
}
*
*
* Single receipt
*
* */
public class Receipt implements Serializable{

    public String mId;
    public String mCdt; //TimeStamp para ordenar los recibos por fecha
    public String mCreatorId;
    public String mName;
    public double mAmount;
    public String mGroupId;//Id del grupo al que pertenece el recibo
    public int mDebtorsQuantity;
    public List<User> mDebtors;
    public List<User> mPaid;

    public boolean mEverybodyHasPaid;

    public Receipt(JSONObject receiptJSON){
        this.mId = JSONUtils.getStringFromJSONObject("id", receiptJSON);
        this.mCdt = JSONUtils.getStringFromJSONObject("cdt", receiptJSON);
        this.mAmount = JSONUtils.getDoubleFromJSONObject("amount", receiptJSON);
        this.mName = JSONUtils.getStringFromJSONObject("name", receiptJSON);
        this.mCreatorId = JSONUtils.getStringFromJSONObject("creator", receiptJSON);
        this.mEverybodyHasPaid = JSONUtils.getIntFromJSONObject("flag", receiptJSON) == 1;
        this.mGroupId = JSONUtils.getStringFromJSONObject("idgroup", receiptJSON);
        this.mDebtorsQuantity = JSONUtils.getIntFromJSONObject("numdebtors", receiptJSON);

        JSONArray debtorsJSONArray = JSONUtils.getJSONArrayFromJSONObject("debtors", receiptJSON);
        JSONArray paidJSONArray = JSONUtils.getJSONArrayFromJSONObject("paid", receiptJSON);

        if (debtorsJSONArray != null){
            mDebtors = new ArrayList<>();
            for (int i = 0; i < debtorsJSONArray.length(); i++){
                mDebtors.add(new User(JSONUtils.getJSONObjectFromJSONArray(debtorsJSONArray, i)));
            }
        }

        if (paidJSONArray != null) {
            mPaid = new ArrayList<>();
            for (int i = 0; i < paidJSONArray.length(); i++){
                mPaid.add(new User(JSONUtils.getJSONObjectFromJSONArray(paidJSONArray, i)));
            }
        }
    }
}
