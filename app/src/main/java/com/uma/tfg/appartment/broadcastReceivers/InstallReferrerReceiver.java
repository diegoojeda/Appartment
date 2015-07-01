package com.uma.tfg.appartment.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.util.Logger;

import java.net.URLDecoder;

//Test:
//am broadcast -a com.android.vending.INSTALL_REFERRER -n com.uma.tfg.appartment/.broadcastReceivers.InstallReferrerReceiver --es "referrer" "prueba"

public class InstallReferrerReceiver extends BroadcastReceiver{

    //--------------------------------------------------------------------------
    public InstallReferrerReceiver()
    {
        Logger.d("ReferrerReceiver.ReferrerReceiver()");
    }

    //--------------------------------------------------------------------------
    @Override public void onReceive(Context context, Intent intent)
    {
        Logger.d("Recibido install intent");

        try
        {
            // Make sure this is the intent we expect - it always should be.
            if ((null != intent) && (intent.getAction().equals("com.android.vending.INSTALL_REFERRER")))
            {
                // This intent should have a referrer string attached to it.
                String rawReferrer = intent.getStringExtra("referrer");
                if (null != rawReferrer)
                {
                    // The string is usually URL Encoded, so we need to decode it.
                    String referrer = URLDecoder.decode(rawReferrer, "UTF-8");

                    // Log the referrer string.
                    Logger.d("Recibido referrer: " + rawReferrer + " y " + referrer);

                    // Persist the referrer string.
                    AppartmentSharedPreferences.setInstallReferrer(referrer);
                }
            }
        }
        catch (Exception e)
        {
            Logger.e(e.toString());
        }
    }
}
