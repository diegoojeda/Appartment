package com.uma.tfg.appartment.network.model;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

public abstract class PostRequest extends Request {

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    public abstract List<NameValuePair> getPostParameters();

    public abstract String getAction();

    @Override
    public String toString() {
        StringBuilder postRequestString = new StringBuilder(super.toString());
        if (getPostParameters() != null) {
            for (NameValuePair parameter : getPostParameters()) {
                postRequestString.append(parameter.getName());
                postRequestString.append(" : ");
                postRequestString.append(parameter.getValue());
                postRequestString.append("\n");
            }
            postRequestString.append("action : ") ;
            postRequestString.append(getAction());
        }
        return postRequestString.toString();
    }
}
