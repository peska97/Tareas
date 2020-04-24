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

import com.android.example.Tareas.R;

import java.util.Calendar;

import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_DESCRIPCION;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FECHA;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_FINALIZADO;
import static com.android.example.Tareas.MainActivity.EXTRA_DATA_UPDATE_TITULO;

/**
 * This class displays a screen where the user enters a new word.
 * The NewTareaActivity returns the entered word to the calling activity
 * (MainActivity) which then stores the new word and updates the list of
 * displayed words.
 */
public class NewTareaActivity extends AppCompatActivity {

    public static final String EXTRA_TITULO = "EXTRA_TITULO";
    public static final String EXTRA_DESCRIPCION = "EXTRA_DESCRIPCION";
    public static final String EXTRA_FECHA = "EXTRA_FECHA";
    public static final String EXTRA_FINALIZADO = "EXTRA_FINALIZADO";

    private EditText mEditTituloView;
    private EditText mEditDescripcionView;
    private TextView mEditFechaView;
    private CheckBox mFinalizado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tarea);

        mEditTituloView = findViewById(R.id.edit_titulo);
        mEditDescripcionView = findViewById(R.id.edit_descripcion);
        mEditFechaView = findViewById(R.id.fecha_creacion);
        mFinalizado = findViewById(R.id.finalizada);

        //creamos la fecha actual y la introducimos
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        mes = mes + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);
        String fechacompleta = (dia+ " / " +mes+ " / " +anio);
        mEditFechaView.setText(fechacompleta);






        //cuando accedas a la clase por medio de click en item
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String titulo = extras.getString(EXTRA_DATA_UPDATE_TITULO, "");
            if (!titulo.isEmpty()) {
                mEditTituloView.setText(titulo);
                mEditTituloView.setSelection(titulo.length());
                mEditTituloView.requestFocus();
                mFinalizado.setVisibility(View.VISIBLE);

            }
            String descripcion = extras.getString(EXTRA_DATA_UPDATE_DESCRIPCION, "");
            if (!descripcion.isEmpty()) {
                mEditDescripcionView.setText(descripcion);
                mEditDescripcionView.setSelection(descripcion.length());
                mEditDescripcionView.requestFocus();
            }
            String fecha = extras.getString(EXTRA_DATA_UPDATE_FECHA, "");
            if (!fecha.isEmpty()) {
                mEditFechaView.setText(fecha);
                mEditFechaView.requestFocus();
            }
            Boolean finalizado = extras.getBoolean(EXTRA_DATA_UPDATE_FINALIZADO, false);
            if (finalizado = true) {
                mFinalizado.isChecked();
                mFinalizado.requestFocus();
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
                    String fecha = mEditFechaView.getText().toString();
                    Boolean finalizado = false;
                    if (mFinalizado.isChecked()){ finalizado = true; };
                    // Pon el nuevo titulo en los extras para la respuesta Intención.
                    replyIntent.putExtra(EXTRA_TITULO, titulo);
                    replyIntent.putExtra(EXTRA_DESCRIPCION, descripcion);
                    replyIntent.putExtra(EXTRA_FECHA, fecha);
                    replyIntent.putExtra(EXTRA_FINALIZADO, finalizado);
                    // Establece el estado del resultado para indicar éxito.
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
