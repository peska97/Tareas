package com.android.example.Tareas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Calendar;

//importa de la actividad principal
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_ID;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_ALARMAID;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_DESCRIPCION;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHA;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHAFIN;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_HORAFIN;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FINALIZADO;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_TITULO;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_ALARMAACTIVADA;


public class NewTareaActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ID = "com.android.example.roomwordssample.ID";
    public static final String EXTRA_TITULO = "com.example.android.roomwordssample.TITULO";
    public static final String EXTRA_DESCRIPCION = "com.android.example.roomwordssample.DESCRIPCION";
    public static final String EXTRA_FECHA = "com.android.example.roomwordssample.FECHA";
    public static final String EXTRA_FECHAFIN = "com.android.example.roomwordssample.FECHAFIN";
    public static final String EXTRA_HORAFIN = "com.android.example.roomwordssample.HORAFIN";
    public static final String EXTRA_FINALIZADO = "com.android.example.roomwordssample.FINALIZADO";
    public static final String EXTRA_ALARMAID = "com.android.example.roomwordssample.ALARMAID";
    public static final String EXTRA_ALARMAACTIVADA = "com.android.example.roomwordssample.ALARMAACTIVADA";


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
    private CheckBox mAlarmaactivada;


    private TareaViewModel mTareaViewModel;

    public SharedPreferences settings;
    private int sharedalarmaid;
    private String sharedalarmaids;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lo utilizamos para que cuando se destruya la actividad no se borre
        mTareaViewModel = ViewModelProviders.of(this).get(TareaViewModel.class);
        setContentView(R.layout.activity_new_tarea);

        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        //valores para la alarmaid
        sharedalarmaid = settings.getInt("contadoralarmaid",0);

        //asociamos todas las variables con la parte visual
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
        mAlarmaactivada = findViewById(R.id.chalarmaactivada);


        //creamos la fecha actual
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        mes = mes + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);
        String mesconcero = ""+mes;
        String diaconcero = ""+dia;
        if (mes < 10) mesconcero = "0"+mes;
        if (dia < 10) diaconcero = "0"+dia;

        final String fechacompleta = (diaconcero + "/" + mesconcero + "/" + anio);
        //introducimos la fecha actual
        if (mTextFechaView.isEnabled()) {
            mTextFechaView.setText(fechacompleta);
        }

        //introducimos un nuevo alarmaid si no esta creado ya
        if (mTextAlarmaidView.isEnabled()) {
            sharedalarmaids = String.valueOf(sharedalarmaid);
            mTextAlarmaidView.setText(sharedalarmaids);
        }

            //cuando accedas a la clase por medio de click en item
        //introduce todos los datos en la parte visual
            final Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String titulo = extras.getString(EXTRA_DATA_UPDATE_TITULO, "");
                if (!titulo.isEmpty()) {
                    mEditTituloView.setText(titulo);
                    //hacer visible otras funciones
                    mFinalizado.setVisibility(View.VISIBLE);
                    mBorrar.setVisibility(View.VISIBLE);
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
                String alarmaid = extras.getString(EXTRA_DATA_UPDATE_ALARMAID, "");
                if (!alarmaid.isEmpty()) {
                    mTextAlarmaidView.setText(alarmaid);
                }
                Boolean alarmaactivada = extras.getBoolean(EXTRA_DATA_UPDATE_ALARMAACTIVADA, false);
                if (alarmaactivada == true) {
                    mAlarmaactivada.setChecked(true);
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
                        // Obtenr los nuevos datos introducidos
                        String titulo = mEditTituloView.getText().toString();
                        String descripcion = mEditDescripcionView.getText().toString();
                        String fecha = mTextFechaView.getText().toString();
                        String fechafin = mTextFechafinView.getText().toString();
                        String horafin = mTextHorafinView.getText().toString();
                        String alarmaid = mTextAlarmaidView.getText().toString();
                        Integer alarmaidint = Integer.parseInt(alarmaid);
                        Boolean finalizado = false;
                        Boolean alarmaactivada = false;
                        if (mFinalizado.isChecked()) {
                            finalizado = true;
                        }

                        //Alarma
                        SharedPreferences.Editor edit = settings.edit();

                        //cuando introduces una fecha
                        if (!TextUtils.isEmpty(mTextFechafinView.getText())) {
                            //cuando introduces un hora
                            if (!TextUtils.isEmpty(mTextHorafinView.getText())) {
                                //si la alarma esta activada
                                if (mAlarmaactivada.isChecked()) {
                                    alarmaactivada = true;
                                    //valores alarma
                                    String salanio, salmes, saldia, salhora, salminute;

                                    salanio = fechafin.substring(6, 10);
                                    salmes = fechafin.substring(3, 5);
                                    saldia = fechafin.substring(0, 2);
                                    salhora = horafin.substring(0, 2);
                                    salminute = horafin.substring(3, 5);
                                    int alanio = Integer.parseInt(salanio);
                                    int almes = Integer.parseInt(salmes);
                                    int aldia = Integer.parseInt(saldia);
                                    int alhora = Integer.parseInt(salhora);
                                    int alminute = Integer.parseInt(salminute);

                                    Calendar today = Calendar.getInstance();
                                    //si la fecha no es la de hoy tambien pasa la fecha
                                    if (!fechafin.equals(fechacompleta)) {
                                        today.set(Calendar.YEAR, alanio);
                                        today.set(Calendar.MONTH, almes);
                                        today.set(Calendar.DAY_OF_MONTH, aldia);
                                    }
                                        today.set(Calendar.HOUR_OF_DAY, alhora);
                                        today.set(Calendar.MINUTE, alminute);
                                        today.set(Calendar.SECOND, 0);



                                    edit.putString("year", salanio);
                                    edit.putString("month", salmes);
                                    edit.putString("day", saldia);
                                    edit.putString("hour", salhora);
                                    edit.putString("minute", salminute);



                                    //PARA REINICIO
                                    edit.putInt("alarmID", alarmaidint);
                                    edit.putLong("alarmTime", today.getTimeInMillis());

                                    edit.commit();

                                    Toast.makeText(NewTareaActivity.this, getString(R.string.changed_to, saldia + "/" + salmes + "/" + saldia + "-" + salhora + ":" + salminute), Toast.LENGTH_LONG).show();

                                    //Creamos la Alarma
                                    GestionAlarmas.setAlarm(alarmaidint, today.getTimeInMillis(), NewTareaActivity.this);

                                }
                                //si la alarma esta desactivada
                                else{
                                    //si hay alguna alarma asociada se borrara
                                    GestionAlarmas.deleteAlarm(alarmaidint, NewTareaActivity.this);
                                }
                            }
                        }


                        // Pon los valores en los extras para utilizarlos en MainActiviti
                        replyIntent.putExtra(EXTRA_TITULO, titulo);
                        replyIntent.putExtra(EXTRA_DESCRIPCION, descripcion);
                        replyIntent.putExtra(EXTRA_FECHA, fecha);
                        replyIntent.putExtra(EXTRA_FECHAFIN, fechafin);
                        replyIntent.putExtra(EXTRA_HORAFIN, horafin);
                        replyIntent.putExtra(EXTRA_FINALIZADO, finalizado);
                        replyIntent.putExtra(EXTRA_ALARMAID, alarmaid);
                        replyIntent.putExtra(EXTRA_ALARMAACTIVADA, alarmaactivada);
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
        //Fecha
            if (v == mButtonFechafin) {
                final Calendar cf = Calendar.getInstance();
                int diafin = cf.get(Calendar.DAY_OF_MONTH);
                int mesfin = cf.get(Calendar.MONTH);
                int aniofin = cf.get(Calendar.YEAR);

                //DatePicker para introducir la fecha
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
                }, aniofin, mesfin, diafin);
                datePickerDialog.show();

            }
            //Hora
            if (v == mButtonHorafin) {
                final Calendar cf = Calendar.getInstance();
                int horafin = cf.get(Calendar.HOUR_OF_DAY);
                int minutosfin = cf.get(Calendar.MINUTE);

                //TimePicker para introducir la hora
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


