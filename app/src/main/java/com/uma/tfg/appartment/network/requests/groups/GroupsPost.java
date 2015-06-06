package com.uma.tfg.appartment.network.requests.groups;

import com.uma.tfg.appartment.network.model.PostRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// INSERT GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324' action => insert Y name
// INVITE GROUP -> 'http://appartment-pruebamarja.rhcloud.com/utils/?session_id=234434234324'  Y action => invite Y emails => array() Y idgroup Y group_name
// El update no se para que sirve
// UPDATE GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324' Y action => update Y type => group Y idgroup

public class GroupsPost extends PostRequest {

    public enum GroupsPostAction {
        INSERT("insert"),
        INVITE("invite"),
        UPDATE("update");

        private String action;

        GroupsPostAction(String action){
            this.action = action;
        }

        public String toString() {
            return action;
        }
    }

    public enum GroupsUpdate {

        GROUP("group"),
        //TODO
        GROUP_MEMBERS("");

        private String type;

        GroupsUpdate(String type){
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }

    private GroupsPostAction mAction;
    public String mName;
    public String mId;
    public List<String> mEmails;

    public GroupsPost(GroupsPostAction action){
        this.mAction = action;
    }

    @Override
    public List<NameValuePair> getPostParameters() {
        List<NameValuePair> parameters = new ArrayList<>();
        switch(mAction){
            case INSERT:
                parameters.add(new BasicNameValuePair("name", mName));
                break;
            case INVITE:
                //TODO
                JSONArray emailsArray = new JSONArray(mEmails);
                parameters.add(new BasicNameValuePair("emails", emailsArray.toString()));
                break;
            case UPDATE:
                //TODO
                break;
        }
        return parameters;
    }

    @Override
    public String getAction() {
        return mAction.toString();
    }

    @Override
    public String getEntity() {
        return Entity.GROUP.toString();
    }

    @Override
    public List<String> getUrlPath() {
        List<String> urlPath = new ArrayList<>();
        urlPath.add(mId);
        return urlPath;
    }

    @Override
    public JSONObject getUrlQueryParameters() {
        return null;
    }

    @Override
    public void processResponse(JSONObject response) throws JSONException {

    }
}
