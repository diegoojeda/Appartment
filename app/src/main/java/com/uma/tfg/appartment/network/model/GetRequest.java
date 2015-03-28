package com.uma.tfg.appartment.network.model;

public abstract class GetRequest extends Request {

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
