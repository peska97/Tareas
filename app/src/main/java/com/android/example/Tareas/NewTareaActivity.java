/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.Tareas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.Tareas.R;

import java.util.Calendar;

import static com.android.example.Tareas.MainActivity.EXTRA_DATA_ID;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_DESCRIPCION;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHA;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHAFIN;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FINALIZADO;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_TITULO;

public class NewTareaActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.android.example.roomwordssample.ID";
    public static final String EXTRA_TITULO = "com.example.android.roomwordssample.TITULO";
    public static final String EXTRA_DESCRIPCION = "com.android.example.roomwordssample.DESCRIPCION";
    public static final String EXTRA_FECHA = "com.android.example.roomwordssample.FECHA";
    public static final String EXTRA_FECHAFIN = "com.android.example.roomwordssample.FECHAFIN";
    public static final String EXTRA_FINALIZADO = "com.android.example.roomwordssample.FINALIZADO";

    private EditText mEditTituloView;
    private EditText mEditDescripcionView;
    private TextView mTextFechaView;
    private EditText mTextFechafinView;
    private CheckBox mFinalizado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tarea);

        mEditTituloView = findViewById(R.id.edit_titulo);
        mEditDescripcionView = findViewById(R.id.edit_descripcion);
        mTextFechaView = findViewById(R.id.fecha_creacion);
        mTextFechafinView = findViewById(R.id.fecha_fin);
        mFinalizado = findViewById(R.id.finalizada);

        //creamos la fecha actual
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        mes = mes + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);
        String fechacompleta = (dia+ " / " +mes+ " / " +anio);
        //introducimos la fecha actual
        if (mTextFechaView.isEnabled()){
            mTextFechaView.setText(fechacompleta);
        }
        mTextFechafinView.setText("9/9/8");

        //cuando accedas a la clase por medio de click en item
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String titulo = extras.getString(EXTRA_DATA_UPDATE_TITULO, "");
            if (!titulo.isEmpty()) {
                mEditTituloView.setText(titulo);
                mFinalizado.setVisibility(View.VISIBLE);
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
            if (!fecha.isEmpty()) {
                mTextFechafinView.setText(fechafin);
            }
            Boolean finalizado = extras.getBoolean(EXTRA_DATA_UPDATE_FINALIZADO, false);
            if (finalizado == true) {
                mFinalizado.setChecked(true);
            }
        }



        final Button button = findViewById(R.id.button_save);
        //Cuando tocan a guardar la nueva intencion se para al MainActivity
        button.setOnClickListener(new View.OnClickListener() {
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
                    Boolean finalizado = false;
                    if (mFinalizado.isChecked()){ finalizado = true; };
                    // Pon el nuevo titulo en los extras para la respuesta Intención.
                    replyIntent.putExtra(EXTRA_TITULO, titulo);
                    replyIntent.putExtra(EXTRA_DESCRIPCION, descripcion);
                    replyIntent.putExtra(EXTRA_FECHA, fecha);
                    replyIntent.putExtra(EXTRA_FECHAFIN, fechafin);
                    replyIntent.putExtra(EXTRA_FINALIZADO, finalizado);
                    if (extras !=null && extras.containsKey(EXTRA_DATA_ID)) {
                        int identificador = extras.getInt(EXTRA_DATA_ID, -1);
                        if (identificador !=-1) {
                            replyIntent.putExtra(EXTRA_ID, identificador);
                        }
                    }
                    // Establece el estado del resultado para indicar éxito.
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
