package com.uma.tfg.appartment.network.management;

import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.network.model.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestsManager implements SendHttpRequestTask.HttpResponseListener {

    private static RequestsManager sharedInstance;

    private List<Request> requestsList;

    public static RequestsManager getInstance() {
        if (sharedInstance == null){
            sharedInstance = new RequestsManager();
        }
        return sharedInstance;
    }

    private RequestsManager() {
        requestsList = new ArrayList<>();
    }

    public void queueRequest(Request request){
        requestsList.add(request);
        sendNextRequest();
    }

    public Request deQueueRequest(){
        if (!requestsList.isEmpty()) {
            return requestsList.remove(0);
        }
        else{
            Logger.e("Se intentó desencolar una petición y la cola estaba vacía");
            return null;
        }
    }

    public Request peekRequest() {
        if (!requestsList.isEmpty()) {
            return requestsList.get(0);
        }
        else{
            return null;
        }
    }

    private void sendNextRequest() {
        if (!requestsList.isEmpty()){
            Request request = deQueueRequest();
            sendRequest(request);
        }
    }

    private void sendRequest(Request request) {
        SendHttpRequestTask task = new SendHttpRequestTask(request, this);
        task.execute();
    }

    @Override
    public void onResponseReceived(String response) {
        //TODO Procesamiento respuesta
        sendNextRequest();
    }
}
