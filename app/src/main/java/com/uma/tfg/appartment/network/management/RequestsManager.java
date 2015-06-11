package com.uma.tfg.appartment.network.management;

import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.network.model.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestsManager implements SendHttpRequestTask.HttpResponseListener {

    private static RequestsManager sharedInstance;

    private List<Request> requestsList;

    private Request currentRequest;

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
        if (currentRequest == null) {
            sendNextRequest();
        }
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
            currentRequest = deQueueRequest();
            sendRequest(currentRequest);
        }
    }

    private void sendRequest(Request request) {
        SendHttpRequestTask task = new SendHttpRequestTask(request, this);
        task.execute();
    }

    //Se recibe el JSON con la respuesta de la petición, se procesa y se manejan errores
    @Override
    public void onResponseReceived(String response) {
        //TODO Procesamiento respuesta
        try{
            JSONObject jsonResponse = new JSONObject(response);
            //Comunicación con el server OK
            int responseCode = jsonResponse.getInt("rc");
            if (responseCode == -10) {
                //TODO Session expired. Reordenar cola peticiones y mandar ini de nuevo
            }
            currentRequest.processResponse(jsonResponse);
            currentRequest = null;
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        sendNextRequest();
    }
}
