package com.uma.tfg.appartment.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.util.Logger;

//Test:
//am broadcast -a com.android.vending.INSTALL_REFERRER -n com.uma.tfg.appartment/.broadcastReceivers.InstallReferrerReceiver --es "referrer" "prueba"

public class InstallReferrerReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("referrer")) {
            String referrer = intent.getStringExtra("referrer");
            Logger.d("Recibido referrer " + referrer);
            AppartmentSharedPreferences.setInstallReferrer(referrer);
        }
    }
}
