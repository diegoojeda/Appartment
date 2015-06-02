package com.uma.tfg.appartment;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.uma.tfg.appartment.util.Logger;
import com.uma.tfg.appartment.util.Util;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

//Los reportes se manejan en http://tracepot.com
@ReportsCrashes(formKey = "", // will not be used
        formUri = "https://collector.tracepot.com/70e13501",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class AppartmentApp extends Application {

        public static AppartmentApp sharedInstance;

        @Override
        public void onCreate() {
                super.onCreate();
                sharedInstance = this;
                ACRA.init(this);
                //TODO Poner esto a 0 cuando se salga a producción
                ACRA.getErrorReporter().putCustomData("TRACEPOT_DEVELOP_MODE", "1");//LLegarán los crashes a "develop" en tracepot
                AppartmentSharedPreferences.initSharedPreferences(getApplicationContext());
        }

        public void onServerErrorReceived(int httpCode) {
                Logger.e("Error del servidor con código: " + httpCode);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                                Util.toast(AppartmentApp.this, "Hubo un error, reinicie la aplicación");
                        }
                });

        }
}
