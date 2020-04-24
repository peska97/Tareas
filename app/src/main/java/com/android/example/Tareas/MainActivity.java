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

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * This class displays a list of words in a RecyclerView.
 * The words are saved in a Room database.
 * The layout for this activity also displays an FAB that
 * allows users to start the NewTareaActivity to add new words.
 * Users can delete a word by swiping it away, or delete all words
 * through the Options menu.
 * Whenever a new word is added, deleted, or updated, the RecyclerView
 * showing the list of words automatically updates.
 */
public class MainActivity extends AppCompatActivity {
    //var para todas las interacciones de la actividad
    private TareaViewModel mTareaViewModel;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    //click en item
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_DATA_UPDATE_TITULO = "extra_data_update_titulo";
    public static final String EXTRA_DATA_UPDATE_DESCRIPCION = "extra_data_update_descripcion";
    public static final String EXTRA_DATA_UPDATE_FECHA = "extra_data_update_fecha";
    public static final String EXTRA_DATA_UPDATE_FINALIZADO = "extra_data_update_finalizado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RecyclerView que llama al adaptador
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TareaListAdapter adapter = new TareaListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //lo utilizamos para que cuando se destruya la actividad, no se borre
        mTareaViewModel = ViewModelProviders.of(this).get(TareaViewModel.class);

        //observador, que actualiza los cambios cuando se producen
        //Obtiene todas las tareas de la BBDD y la asocia al adaptador
        mTareaViewModel.getAllTareas().observe(this, new Observer<List<Tarea>>() {
            @Override
            public void onChanged(@Nullable final List<Tarea> tareas) {
                //Actualiza las tareas en cache
                adapter.setWords(tareas);
            }
        });

        //Opciones del boton flotante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //al dar clic en FAB llama a la clase para crear otra palabra
                Intent intent = new Intent(MainActivity.this, NewTareaActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        //Funciones para los item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    //implementa codigo para poder mover el item
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    //al mover el item a un lado se elimina de la BBDD
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Tarea myTarea = adapter.getTareaAtPosition(position);
                        Toast.makeText(MainActivity.this,
                                getString(R.string.delete_word_preamble) + " " +
                                myTarea.getTitulo(), Toast.LENGTH_LONG).show();

                        //Eliminar tarea
                        mTareaViewModel.deleteTarea(myTarea);
                    }
                });
        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        //click en item
        adapter.setOnItemClickListener(new TareaListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                Tarea tarea = adapter.getTareaAtPosition(position);
                clickenitem(tarea);
            }
        });

    }

    //menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Item de menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Boton pra borrar todo
        if (id == R.id.clear_data) {
            //mensaje toast
            Toast.makeText(this, R.string.clear_data_toast_text, Toast.LENGTH_LONG).show();

            //Eliminar todos los datos
            mTareaViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //devolucion de llamada de la segunda actividad, inserta la palabra en la BBDD
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String titulo = data.getStringExtra(NewTareaActivity.EXTRA_TITULO);
            String descripcion = data.getStringExtra(NewTareaActivity.EXTRA_DESCRIPCION);
            String fecha = data.getStringExtra(NewTareaActivity.EXTRA_FECHA);
            Boolean finalizado = data.getBooleanExtra(NewTareaActivity.EXTRA_FINALIZADO,false);
            Tarea tarea = new Tarea(titulo,  descripcion, fecha, finalizado);

            //Guarda los datos
            mTareaViewModel.insert(tarea);
        } else {
            Toast.makeText(
                    this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    //click en item
    public void clickenitem( Tarea tarea) {
        Intent intent = new Intent(this, NewTareaActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_TITULO, tarea.getTitulo());
        intent.putExtra(EXTRA_DATA_UPDATE_DESCRIPCION, tarea.getDescripcion());
        startActivityForResult(intent, UPDATE_WORD_ACTIVITY_REQUEST_CODE);
    }
}