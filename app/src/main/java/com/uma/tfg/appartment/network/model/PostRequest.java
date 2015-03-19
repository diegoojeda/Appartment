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

    @Override
    public String toString() {
        StringBuilder postRequestString = new StringBuilder(super.toString());
        if (getPostParameters() != null) {
            postRequestString.append("Parámetros POST: ");
            for (NameValuePair parameter : getPostParameters()) {
                postRequestString.append("name: ");
                postRequestString.append(parameter.getName());
                postRequestString.append("\nvalue: ");
                postRequestString.append(parameter.getValue());
                postRequestString.append("\n");
            }
            postRequestString.append("\n");
        }
        return postRequestString.toString();
    }
}
