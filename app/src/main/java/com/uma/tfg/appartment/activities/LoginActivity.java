package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.SignInButton;
import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.network.management.RequestsBuilder;
import com.uma.tfg.appartment.network.requests.IniPost;
import com.uma.tfg.appartment.network.requests.LoginPost;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.model.User;
import com.uma.tfg.appartment.util.Util;

import java.util.Locale;

public class LoginActivity extends Activity implements View.OnClickListener,
        IniPost.IniPostListener, LoginPost.LoginPostListener {

    private final static int REQUEST_CODE_AUTHENTICATION_GPLUS = 1100;

    private ProgressBar mProgressSpinner;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestForPushToken();

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        mProgressSpinner = (ProgressBar) findViewById(R.id.progress_spinner_auth_gplus);
        changeViewsGivenUserLoginState();
    }

    private void changeViewsGivenUserLoginState() {
        if (AppartmentSharedPreferences.isUserLoggedIn()) {
            mSignInButton.setVisibility(View.GONE);
            mProgressSpinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onGPlusSignInButtonClicked();
                break;
        }
    }

    private void requestForPushToken(){
        //TODO pedir push token, y luego INI
        sendIni();
    }

    private void sendIni(){
        String devId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String lang = Locale.getDefault().toString();
        String pushToken = "";
        int appVersion = -1;
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException ex){
            ex.printStackTrace();
        }
        RequestsBuilder.sendIniPostRequest(devId, lang, pushToken, appVersion, this);
    }

    private void onGPlusSignInButtonClicked() {
        mSignInButton.setVisibility(View.GONE);
        mProgressSpinner.setVisibility(View.VISIBLE);
        if (needToAuthenticateWithGplus()) {
            doLoginWithGPlus();
        }
        else{
            doLoginWithAppartmentServer();
        }
    }

    private boolean needToAuthenticateWithGplus() {
        return AppartmentSharedPreferences.getUserId() == null;
    }

    private void doLoginWithAppartmentServer() {
        String userId = AppartmentSharedPreferences.getUserId();
        String userName = AppartmentSharedPreferences.getUserName();
        String userEmail = AppartmentSharedPreferences.getUserEmail();
        RequestsBuilder.sendLoginPostRequest(userId, userEmail, userName, this);
    }

    private void doLoginWithGPlus() {
        Intent i = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(i, REQUEST_CODE_AUTHENTICATION_GPLUS);
    }

    private void onUserAuthenticated() {
        doLoginWithAppartmentServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AUTHENTICATION_GPLUS){
            mSignInButton.setVisibility(View.VISIBLE);
            mProgressSpinner.setVisibility(View.GONE);
            if (resultCode == RESULT_CANCELED){
                Util.toast(this, "Hubo un error al hacer login con Google+");
            }
            else{
                if (data != null){
                    User u = (User)data.getSerializableExtra(AuthenticationActivity.AUTHENTICATED_USER_KEY);
                    saveUserInformationToSharedPreferences(u);
                    Logger.d("Usuario: " + u.toString());
                    onUserAuthenticated();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveUserInformationToSharedPreferences(User user){
        AppartmentSharedPreferences.setUserId(user.userId);
        AppartmentSharedPreferences.setUserEmail(user.userEmail);
        AppartmentSharedPreferences.setUserName(user.userName);
    }

    @Override
    public void onLoginPostSuccess() {
        Intent i = new Intent(this, Index.class);
        startActivity(i);
    }

    @Override
    public void onLoginPostError() {
        Util.toast(this, "Error al hacer login");
    }

    @Override
    public void onIniSuccess() {
        Logger.d("INI recibido satisfactoriamente");
        if (AppartmentSharedPreferences.isUserLoggedIn()) {
            doLoginWithAppartmentServer();
        }
    }

    @Override
    public void onIniError() {
        Logger.e("Error en INI");
    }
}

