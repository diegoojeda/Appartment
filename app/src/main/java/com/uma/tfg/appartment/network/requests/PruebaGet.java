package com.uma.tfg.appartment.network.requests;

import com.uma.tfg.appartment.network.model.GetRequest;

import org.json.JSONObject;

import java.util.List;

public class PruebaGet extends GetRequest{

    @Override
    public String getEntity() {
        return null;
    }

    @Override
    public List<String> getUrlPath() {
        return null;
    }

    @Override
    public JSONObject getUrlQueryParameters() {
        return null;
    }
}
