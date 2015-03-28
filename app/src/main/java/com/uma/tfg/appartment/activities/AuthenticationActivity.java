package com.uma.tfg.appartment.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.model.User;

public class AuthenticationActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final String DIALOG_ERROR = "dialog_error";

    public static final String AUTHENTICATED_USER_KEY = "authenticated_user_key";

    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingError = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Logger.d("Conectado al cliente de Google+ con éxito");
        getUserProfileInformation();
    }

    private void getUserProfileInformation() {
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String userName = currentPerson.getDisplayName();
            String userProfilePictureUrl = currentPerson.getImage().getUrl();
            String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            String userId = currentPerson.getId();
            User u = new User(userId, userEmail, userName, userProfilePictureUrl);
            finalizeAndReturnUser(u);
        }
        else{
            finalizeAndReturnError();
        }
    }

    private void finalizeAndReturnUser(User u){
        Intent dataToReturn = new Intent();
        dataToReturn.putExtra(AUTHENTICATED_USER_KEY, u);
        setResult(Activity.RESULT_OK, dataToReturn);
        finish();
    }

    private void finalizeAndReturnError() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.e("Conexión suspendida");
        finalizeAndReturnError();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Logger.w("Ha fallado la conexión. Se intenta recuperar");
        if (mResolvingError) {
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                //Hubo un error, se intenta reconectar
                mGoogleApiClient.connect();
            }
        } else {
            // Se muestra un diálogo proporcionado por google para el error devuelto
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    //Creación del diálogo de error a partir del errorCode
    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((AuthenticationActivity)getActivity()).onDialogDismissed();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }
}
