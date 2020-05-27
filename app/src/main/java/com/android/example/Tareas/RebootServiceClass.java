package com.android.example.Tareas;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class RebootServiceClass extends IntentService {

    //Crea un IntentService. Invocado por el constructor de tu subclase.
    public RebootServiceClass(String name) {
        //name se usa para nombrar el subproceso de trabajo, importante solo para la depuración.
        super(name);
        startForeground(1, new Notification());
    }

    public RebootServiceClass() {
        super("RebootServiceClass");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }



    //cuado se reinicia y se llama
    //crea las alarmas que habia antes
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String intentType = intent.getExtras().getString("caller");
        if (intentType == null) return;
        if (intentType.equals("RebootReceiver")) {
            SharedPreferences settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            GestionAlarmas.setAlarm(settings.getInt("alarmID", 0), settings.getLong("alarmTime", 0), this);
        }
    }
}
