package com.android.example.Tareas;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.example.Tareas.R;

import java.util.Calendar;


import static com.android.example.Tareas.MainActivity.EXTRA_DATA_ID;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_ALARMAID;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_DESCRIPCION;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHA;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHAFIN;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_HORAFIN;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FINALIZADO;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_TITULO;


public class NewTareaActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ID = "com.android.example.roomwordssample.ID";
    public static final String EXTRA_TITULO = "com.example.android.roomwordssample.TITULO";
    public static final String EXTRA_DESCRIPCION = "com.android.example.roomwordssample.DESCRIPCION";
    public static final String EXTRA_FECHA = "com.android.example.roomwordssample.FECHA";
    public static final String EXTRA_FECHAFIN = "com.android.example.roomwordssample.FECHAFIN";
    public static final String EXTRA_HORAFIN = "com.android.example.roomwordssample.HORAFIN";
    public static final String EXTRA_FINALIZADO = "com.android.example.roomwordssample.FINALIZADO";
    public static final String EXTRA_ALARMAID = "com.android.example.roomwordssample.ALARMAID";

    private final static String CHANNEL_ID = "NOTIFICACIONES";
    private final static int NOTIFICACION_ID = 0;


    private EditText mEditTituloView;
    private EditText mEditDescripcionView;
    private TextView mTextFechaView;
    private EditText mTextFechafinView;
    private EditText mTextHorafinView;
    private CheckBox mFinalizado;
    private Button mButtonFechafin;
    private Button mButtonHorafin;
    private Button mBorrar;
    private TextView mTextAlarmaidView;

    //private int alarmID = 1;
    private String contadoralarma;
    private int contadoralarmaint;

    private TareaViewModel mTareaViewModel;

    private String finalHour, finalMinute;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lo utilizamos para que cuando se destruya la actividad, no se borre
        mTareaViewModel = ViewModelProviders.of(this).get(TareaViewModel.class);
        setContentView(R.layout.activity_new_tarea);

        mEditTituloView = findViewById(R.id.edit_titulo);
        mEditDescripcionView = findViewById(R.id.edit_descripcion);
        mTextFechaView = findViewById(R.id.fecha_creacion);
        mTextFechafinView = findViewById(R.id.fecha_fin);
        mTextHorafinView = findViewById(R.id.hora_fin);
        mFinalizado = findViewById(R.id.finalizada);
        mBorrar = findViewById(R.id.button_delete);
        mButtonFechafin = findViewById(R.id.button_fecha);
        mButtonHorafin = findViewById(R.id.button_hora);
        mButtonFechafin.setOnClickListener(this);
        mButtonHorafin.setOnClickListener(this);
        mTextAlarmaidView = findViewById(R.id.alarmaid_text);


        //creamos la fecha actual
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int mescorrecto = mes + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);
        String fechacompleta = (dia + " / " + mescorrecto + " / " + anio);
        //introducimos la fecha actual
        if (mTextFechaView.isEnabled()) {
            mTextFechaView.setText(fechacompleta);
        }

        //introducimos un nuevo alarmaid si no esta creado ya
        if (mTextAlarmaidView.isEnabled()) {
            contadoralarma = String.valueOf(contadoralarmaint);
            mTextAlarmaidView.setText(contadoralarma);
            //para darle un valor diferente a la proxima alarma

        }

            //cuando accedas a la clase por medio de click en item
            final Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String titulo = extras.getString(EXTRA_DATA_UPDATE_TITULO, "");
                String alarmaid = extras.getString(EXTRA_DATA_UPDATE_ALARMAID, "");
                if (!titulo.isEmpty()) {
                    mEditTituloView.setText(titulo);
                    //hacer visible otras funciones
                    mFinalizado.setVisibility(View.VISIBLE);
                    mBorrar.setVisibility(View.VISIBLE);
                    //mostrar id de alarma
                    mTextAlarmaidView.setText(alarmaid);

                }
                String descripcion = extras.getString(EXTRA_DATA_UPDATE_DESCRIPCION, "");
                if (!descripcion.isEmpty()) {
                    mEditDescripcionView.setText(descripcion);
                }
                String fecha = extras.getString(EXTRA_DATA_UPDATE_FECHA, "");
                if (!fecha.isEmpty()) {
                    mTextFechaView.setText(fecha);
                }
                String fechafin = extras.getString(EXTRA_DATA_UPDATE_FECHAFIN, "");
                if (!fechafin.isEmpty()) {
                    mTextFechafinView.setText(fechafin);
                }
                String horafin = extras.getString(EXTRA_DATA_UPDATE_HORAFIN, "");
                if (!horafin.isEmpty()) {
                    mTextHorafinView.setText(horafin);
                }
                Boolean finalizado = extras.getBoolean(EXTRA_DATA_UPDATE_FINALIZADO, false);
                if (finalizado == true) {
                    mFinalizado.setChecked(true);
                }
            }


            //Cuando tocan boton guardar la nueva intencion se pasa al MainActivity
            final Button buttonsave = findViewById(R.id.button_save);
            buttonsave.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StringFormatInvalid")
                public void onClick(View view) {
                    // Crea un nuevo Intent
                    Intent replyIntent = new Intent();
                    //cuando no introduces ninguna tarea
                    if (TextUtils.isEmpty(mEditTituloView.getText())) {
                        setResult(RESULT_CANCELED, replyIntent);
                    } else {
                        // Obtenga el nuevo titulo y descripcion que ingresó el usuario.
                        String titulo = mEditTituloView.getText().toString();
                        String descripcion = mEditDescripcionView.getText().toString();
                        String fecha = mTextFechaView.getText().toString();
                        String fechafin = mTextFechafinView.getText().toString();
                        String horafin = mTextHorafinView.getText().toString();
                        String alarmaid = mTextAlarmaidView.getText().toString();
                        Integer alarmaidint = Integer.parseInt(alarmaid);
                        Boolean finalizado = false;

                        //para darle un valor diferente a la proxima alarma
                        int cont = contadoralarmaint + 1;
                        contadoralarmaint = cont;

                        //cuando introduces una fecha
                        if (!TextUtils.isEmpty(mTextFechafinView.getText())) {
                            //cuando introduces un hora
                            if (!TextUtils.isEmpty(mTextHorafinView.getText())) {
                                //valores alarma
                                String salanio, salmes, saldia, salhora, salminute;

                                //salanio = fechafin.substring(6,10);
                                //salmes = fechafin.substring(3,5);
                                //saldia = fechafin.substring(0,2);
                                salhora = horafin.substring(0, 2);
                                salminute = horafin.substring(3, 5);
                                //int alanio = Integer.parseInt(salanio);
                                //int almes = Integer.parseInt(salmes);
                                //int aldia = Integer.parseInt(saldia);
                                int alhora = Integer.parseInt(salhora);
                                int alminute = Integer.parseInt(salminute);

                                //Alarma

                                Toast.makeText(NewTareaActivity.this, getString(R.string.notificaciontoast, (titulo), salhora + ":" + salminute), Toast.LENGTH_LONG).show();

                            }

                        }


                        if (mFinalizado.isChecked()) {
                            finalizado = true;
                        }
                        // Pon el nuevo titulo en los extras para la respuesta Intención.
                        replyIntent.putExtra(EXTRA_TITULO, titulo);
                        replyIntent.putExtra(EXTRA_DESCRIPCION, descripcion);
                        replyIntent.putExtra(EXTRA_FECHA, fecha);
                        replyIntent.putExtra(EXTRA_FECHAFIN, fechafin);
                        replyIntent.putExtra(EXTRA_HORAFIN, horafin);
                        replyIntent.putExtra(EXTRA_FINALIZADO, finalizado);
                        replyIntent.putExtra(EXTRA_ALARMAID, alarmaid);
                        if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                            int identificador = extras.getInt(EXTRA_DATA_ID, -1);
                            if (identificador != -1) {
                                replyIntent.putExtra(EXTRA_ID, identificador);
                            }
                        }
                        // Establece el estado del resultado para indicar éxito.
                        setResult(RESULT_OK, replyIntent);
                    }
                    finish();
                }

            });


            //Cuando tocan boton borrar
            final Button buttondelete = findViewById(R.id.button_delete);
            buttondelete.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {


                    // Envia el result para acceder borrar la tarea
                    setResult(RESULT_FIRST_USER);

                    finish();
                }

            });

    }

        //para introducir las fechas en la parte visual, dandole a los botones


    @Override
    public void onClick(View v){
            if (v == mButtonFechafin) {
                final Calendar cf = Calendar.getInstance();
                int diafin = cf.get(Calendar.DAY_OF_MONTH);
                int mesfin = cf.get(Calendar.MONTH);
                int aniofin = cf.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String finalDia, finalMes;
                        month = month + 1;
                        finalDia = "" + dayOfMonth;
                        finalMes = "" + month;
                        if (dayOfMonth < 10) finalDia = "0" + dayOfMonth;
                        if (month < 10) finalMes = "0" + month;
                        mTextFechafinView.setText(finalDia + "/" + finalMes + "/" + year);


                    }
                }, diafin, mesfin, aniofin);
                datePickerDialog.show();

            }
            if (v == mButtonHorafin) {
                final Calendar cf = Calendar.getInstance();
                int horafin = cf.get(Calendar.HOUR_OF_DAY);
                int minutosfin = cf.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("StringFormatInvalid")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String finalHour, finalMinute;
                        finalHour = "" + hourOfDay;
                        finalMinute = "" + minute;
                        if (hourOfDay < 10) finalHour = "0" + hourOfDay;
                        if (minute < 10) finalMinute = "0" + minute;
                        mTextHorafinView.setText(finalHour + ":" + finalMinute);


                    }
                }, horafin, minutosfin, false);
                timePickerDialog.show();

            }
        }

}


