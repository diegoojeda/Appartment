package com.uma.tfg.appartment.network.model;

import android.net.Uri;

import com.uma.tfg.appartment.Constants;
import com.uma.tfg.appartment.Util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class Request {

    public enum HttpMethod {
        GET,
        POST
    }

    public abstract String getEntity();

    public abstract HttpMethod getHttpMethod();

    public abstract List<String> getUrlPath();

    public abstract JSONObject getUrlQueryParameters();

    public URL getUrlString() {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(Constants.HTTP_SCHEME);
        uriBuilder.authority(Constants.SERVER_URL);

        if (getEntity() != null) {
            uriBuilder.appendPath(getEntity());
        }

        if (getUrlPath() != null) {
            for (String pathComponent : getUrlPath()) {
                uriBuilder.appendPath(pathComponent);
            }
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
        requestString.append("Petición: \n");
        requestString.append("Método: ");
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
}
