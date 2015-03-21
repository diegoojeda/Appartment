package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.model.User;

public class LoginActivity extends Activity implements View.OnClickListener{

    private final static int REQUEST_CODE_AUTHENTICATION_GPLUS = 1100;

    private ProgressBar mProgressSpinner;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        mProgressSpinner = (ProgressBar) findViewById(R.id.progress_spinner_auth_gplus);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                onGPlusSignInButtonClicked();
                break;
        }
    }

    private void onGPlusSignInButtonClicked() {
        mSignInButton.setVisibility(View.GONE);
        mProgressSpinner.setVisibility(View.VISIBLE);
        doLoginWithGPlus();
    }

    private void doLoginWithGPlus() {
        Intent i = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(i, REQUEST_CODE_AUTHENTICATION_GPLUS);
    }

    private void onUserAuthenticated() {
        Intent i = new Intent(this, Index.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AUTHENTICATION_GPLUS){
            mSignInButton.setVisibility(View.VISIBLE);
            mProgressSpinner.setVisibility(View.GONE);
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Hubo un error al hacer login con Google+", Toast.LENGTH_LONG).show();
            }
            else{
                if (data != null){
                    User u = (User)data.getSerializableExtra(AuthenticationActivity.AUTHENTICATED_USER_KEY);
                    Logger.d("Usuario: " + u.toString());
                    onUserAuthenticated();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

