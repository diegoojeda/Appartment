package com.uma.tfg.appartment.network.model;

import android.net.Uri;

import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.Constants;
import com.uma.tfg.appartment.network.requests.IniPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//********************//
//*DEFINICION-ERRORES*//
//********************//
//-1 ERROR DB
//-2 NO ENTITY
//-3 NO ACTION
//-4 NO SESSION
//-5 FAIL PARAMS
//-6 FAIL INSERT USER
//-7 FAIL INSERT DEVICE
//-8 FAIL ENTITY
//-9 FAIL ACTION
//-10 SESSION EXPIRED
//-11 FAIL CONNECTION DB
//-12 FAIL ACCESS UPSERT
//-13 FAIL UPDATE GROUP
//-14 FAIL SEND MAIL
//-15 FAIL ADD FRIEND
//-16 NO TYPE GET
//-17 FAIL TYPE GET
//-18 FAIL INSERT RECEIPT
//-19 FAIL INSERT HASH
//-20 FAIL FINDONE USER
//-21 FAIL UPDATE USER
//-22 FAIL FINDONNE DEVICE
//-23 FAIL DEVICE UPSERT
//-24 FAIL INSERT GROUP
//-25 FAIL UPDATE GROUP
//-26 FAIL GET GROUP
//-27 FAIL FINDMODIFY HASH
//-28 FAIL DELETE GROUP
//-29 FAIL GET FRIENDS
//-30 FAIL GET RECEIPTS
//-31 FAIL GET GROUP RECEIPTS
//-32 FAIL UPDATE DEBTORS
//-33 FAIL UPDATE RECEIPT
//-34 FAIL DELETE RECEIPT
//-35 FAIL GET DEVICES BY USER
//-35 FAIL GET RECEIPT
//-36 FAIL GET USERS BY ID

/*
* //Resumen de peticiones
 // INI -> 'http://appartment-pruebamarja.rhcloud.com/ini/';
 // LOGIN -> 'http://appartment-pruebamarja.rhcloud.com/user/?session_id=234434234324'; Y action => login Y email Y id Y name Y lang
 // UPDATE USER -> 'http://appartment-pruebamarja.rhcloud.com/user/?session_id=234434234324'; Y action => update
 // INSERT GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324' action => insert Y name
 // UPDATE GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324' Y action => update Y type => group Y idgroup
 // GET GROUPS -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324'
 // GET GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo/?session_id=234434234324'
 // INVITE GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324'  Y action => invite Y emails => array() Y idgroup Y group_name
 // ADD NEWMEMBER -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo(sacado de referee)/?session_id=234434234324' Y action => update Y type => umember
 // ADD REGISTEREDMEMBER -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324' Y action => update Y type => rmember Y namegroup Y idgroup Y array => ids
 // DELETE MEMBER -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo?session_id=234434234324' Y action => update Y type => dmember
 // DELETE group -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo?session_id=234434234324' Y action => delete
 // GET ALL FRIENDS -> 'http://appartment-pruebamarja.rhcloud.com/user/?session_id=234434234324' Y type => friends
 // (Por ahora no)GET GROUP FRIENDS -> 'http://appartment-pruebamarja.rhcloud.com/user/?session_id=234434234324' Y type => members Y idgroup
 // INSERT receipt -> 'http://appartment-pruebamarja.rhcloud.com/receipt/?session_id=234434234324' Y action => insert Y idgroup Y amount Y debtors => array() Y namereceipt
 // GET  RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/id_recibo/?session_id=234434234324'
 // GET GROUP RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/?session_id=234434234324&id_group=id'
 // UPDATE CREDITORS RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/id_recibo/?session_id=234434234324' Y action => update Y type => debtors
 // UPDATE  RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/id_recibo/?session_id=234434234324' Y action => update Y type => receipt Y name => nuevo_nombre
 // DELETE  RECEIPT -> 'http://appartment-pruebamarja.rhcloud.com/receipt/id_recibo/?session_id=234434234324' Y action => delete
* */
public abstract class Request {

    public enum HttpMethod {
        GET,
        POST
    }

    public enum Entity{
        INI("ini"),
        USER("user"),
        GROUP("group"),
        RECEIPT("receipt");

        private String mEntityString;

        Entity(String entityString){
            this.mEntityString = entityString;
        }

        public String toString(){
            return this.mEntityString;
        }
    }

    public abstract Entity getEntity();

    public abstract HttpMethod getHttpMethod();

    public abstract List<String> getUrlPath();

    public abstract JSONObject getUrlQueryParameters();

    public URL getUrlString() {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(Constants.HTTP_SCHEME);
        uriBuilder.authority(Constants.SERVER_URL);

        if (getEntity() != null) {
            uriBuilder.appendPath(getEntity().toString());
        }

        if (getUrlPath() != null) {
            for (String pathComponent : getUrlPath()) {
                uriBuilder.appendPath(pathComponent);
            }
        }

        String sessionId = AppartmentSharedPreferences.getSessionId();
        //El ini es la única petición que no necesita session_id, la excluimos
        if (sessionId != null && this.getClass() != IniPost.class){
            if (simulateSessionExpired()){
                sessionId += "a";
            }
            uriBuilder.appendQueryParameter("session_id", sessionId);
        }

        JSONObject urlQueryParameters = getUrlQueryParameters();
        if (urlQueryParameters != null) {
            for (int i = 0; i < urlQueryParameters.length(); i++) {
                try {
                    String key = urlQueryParameters.names().getString(i);
                    uriBuilder.appendQueryParameter(key, urlQueryParameters.getString(key));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }

        try {
            Uri requestUri = uriBuilder.build();
            return new URL(requestUri.toString());
        }
        catch (MalformedURLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder requestString = new StringBuilder();
        requestString.append(this.getHttpMethod());
        requestString.append(" \n");
        if (getEntity() != null) {
            requestString.append("Entidad: ");
            requestString.append(this.getEntity());
            requestString.append(" \n");
        }
        requestString.append("URL: ");
        requestString.append(this.getUrlString().toString());
        requestString.append(" \n");
        return requestString.toString();
    }

    public abstract void processResponse(JSONObject response) throws JSONException;

    public boolean simulateSessionExpired(){
        return false;
    }
}
