package com.uma.tfg.appartment;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

//Los reportes se manejan en http://tracepot.com
@ReportsCrashes(formKey = "", // will not be used
        formUri = "https://collector.tracepot.com/70e13501",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class AppartmentApp extends Application {

        @Override
        public void onCreate() {
                super.onCreate();
                ACRA.init(this);
        }
}
