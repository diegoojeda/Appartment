package com.uma.tfg.appartment.network.management;

import android.os.AsyncTask;

import com.uma.tfg.appartment.Util.Logger;
import com.uma.tfg.appartment.network.model.GetRequest;
import com.uma.tfg.appartment.network.model.Request;
import com.uma.tfg.appartment.network.model.PostRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SendHttpRequestTask extends AsyncTask<Void, Void, String>{

    public interface HttpResponseListener {
        void onResponseReceived(String response);
    }

    private Request mRequest;
    private HttpResponseListener mResponseListener;

    public SendHttpRequestTask (Request request, HttpResponseListener responseListener){
        this.mRequest = request;
        this.mResponseListener = responseListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (mRequest != null) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;
            String responseString = null;

            try {

                if (mRequest.getHttpMethod() == Request.HttpMethod.GET){
                    response = httpclient.execute(buildGetRequest((GetRequest) mRequest));
                }
                else if (mRequest.getHttpMethod() == Request.HttpMethod.POST){
                    response = httpclient.execute(buildPostRequest((PostRequest) mRequest));
                }

                if (response != null) {
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == HttpStatus.SC_OK) {
                        Logger.d("Recibida respuesta correctamente. Http code: " + statusCode);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        responseString = out.toString();
                        out.close();
                    } else {
                        //Closes the connection.
                        Logger.e("Error del servidor. Http code: " + statusCode);
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                }
                else{
                    Logger.e("Error al procesar la respuesta de una petición");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }
        Logger.e("Se intentó enviar una petición null");
        return null;
    }

    private HttpGet buildGetRequest(GetRequest req) {
        if (req.getUrlString() != null) {
            Logger.d("Se envía petición: \n " + req.toString());
            return new HttpGet(req.getUrlString().toString());
        }
        return null;
    }

    private HttpPost buildPostRequest(PostRequest req) throws UnsupportedEncodingException {
        if (req.getUrlString() != null) {
            Logger.d("Se envía petición: \n " + req.toString());
            HttpPost httpPostRequest = new HttpPost(req.getUrlString().toString());
            httpPostRequest.setEntity(new UrlEncodedFormEntity(req.getPostParameters()));
            return httpPostRequest;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            Logger.d("Resultado peticion: " + result);
            if (mResponseListener != null) {
                mResponseListener.onResponseReceived(result);
            }
        }
    }
}
