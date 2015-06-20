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
    public User mCreator;
    public String mName;
    public double mAmount;
    public String mGroupId;//Id del grupo al que pertenece el recibo
    public int mDebtorsQuantity;//Número de personas que aún no han pagado el recibo
    public List<User> mUsersList;

    public boolean mEverybodyHasPaid;

    public Receipt(JSONObject receiptJSON){
        this.mId = JSONUtils.getStringFromJSONObject("id", receiptJSON);
        this.mCdt = JSONUtils.getStringFromJSONObject("cdt", receiptJSON);
        this.mAmount = JSONUtils.getDoubleFromJSONObject("amount", receiptJSON);
        this.mName = JSONUtils.getStringFromJSONObject("name", receiptJSON);
        this.mCreator = new User(JSONUtils.getJSONObjectFromJSONObject("creator", receiptJSON));
        this.mEverybodyHasPaid = JSONUtils.getIntFromJSONObject("flag", receiptJSON) == 1;
        this.mGroupId = JSONUtils.getStringFromJSONObject("idgroup", receiptJSON);
        this.mDebtorsQuantity = JSONUtils.getIntFromJSONObject("numdebtors", receiptJSON);

        JSONArray usersJSONArray = JSONUtils.getJSONArrayFromJSONObject("users", receiptJSON);
        if (usersJSONArray != null){
            mUsersList = new ArrayList<>();
            for (int i = 0; i < usersJSONArray.length(); i++){
                mUsersList.add(new User(JSONUtils.getJSONObjectFromJSONArray(usersJSONArray, i)));
            }
        }
    }
}
