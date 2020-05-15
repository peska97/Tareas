package com.android.example.Tareas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //var para todas las interacciones de la actividad
    private TareaViewModel mTareaViewModel;

    public static final int NEW_TAREA_ACTIVITY_REQUEST_CODE = 1;

    //click en item
    public static final int UPDATE_TAREA_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_DATA_UPDATE_TITULO = "extra_data_update_titulo";
    public static final String EXTRA_DATA_UPDATE_DESCRIPCION = "extra_data_update_descripcion";
    public static final String EXTRA_DATA_UPDATE_FECHA = "extra_data_update_fecha";
    public static final String EXTRA_DATA_UPDATE_FECHAFIN = "extra_data_update_fechafin";
    public static final String EXTRA_DATA_UPDATE_HORAFIN = "extra_data_update_horafin";
    public static final String EXTRA_DATA_UPDATE_FINALIZADO = "extra_data_update_finalizado";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_TAREA = "extra_tarea";
    //para ordenar la lista
    public int mOrdenar;
    public Tarea tareaPulsada;
    //deslizar hacia abajo
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //actualizar lista al deslizar abajo
        swipeRefreshLayout =findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //llama al adaptador
                adaptador(mOrdenar);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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
                    adapter.setTareas(tareas);
                }
            });

        //Opciones del boton flotante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //al dar clic en FAB llama a la clase para crear otra palabra
                Intent intent = new Intent(MainActivity.this, NewTareaActivity.class);
                startActivityForResult(intent, NEW_TAREA_ACTIVITY_REQUEST_CODE);
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
                        final Tarea myTarea = adapter.getTareaAtPosition(position);


                        //para confirmar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        alerta.setMessage("Desea borrar esta tarea?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener(){

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //cuando le dices que si
                                                //toast
                                                Toast.makeText(MainActivity.this,
                                                        getString(R.string.delete_word_preamble) + " " +
                                                                myTarea.getTitulo(), Toast.LENGTH_LONG).show();
                                                //Eliminar tarea
                                                mTareaViewModel.deleteTarea(myTarea);
                                            }
                                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adaptador(mOrdenar);
                                    }
                                });
                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Confirmar");
                        titulo.show();
                        adaptador(mOrdenar);

                    }
                });
        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        //click en item
        adapter.setOnItemClickListener(new TareaListAdapter.ClickListener() {

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
            //para confirmar
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage("Desea borrar todas las tareas?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cuando le dices que si
                            //toast
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.clear_data_toast_text), Toast.LENGTH_LONG).show();
                            //Eliminar todos los datos
                            mTareaViewModel.deleteAll();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Confirmar");
            titulo.show();

            return true;
        }
        //ordenar alfabeticamente
        if (id == R.id.ordenar_alfabeticamente) {
            //mensaje toast
            Toast.makeText(this, R.string.ordenar_alfabeticamente, Toast.LENGTH_LONG).show();

            //Cambiar valor de variable
            mOrdenar = 0;
            //llama al adaptador
            adaptador(mOrdenar);

            return true;
        }
        //ordenar por fecha
        if (id == R.id.ordenar_fecha) {
            //mensaje toast
            Toast.makeText(this, R.string.ordenar_fecha, Toast.LENGTH_LONG).show();

            //Cambiar valor de variable
            mOrdenar = 1;
            //llama al adaptador
            adaptador(mOrdenar);

            return true;
        }
        //filtrar todas
        if (id == R.id.filtrar_todas){
            Toast.makeText(this, R.string.text_mostrartodas, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mOrdenar = 1;
            //llama al adaptador
            adaptador(mOrdenar);
        }
        //filtrar por finalizado
        if (id == R.id.filtrar_finalizado){
            Toast.makeText(this, R.string.text_filtrarfin, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mOrdenar = 2;
            //llama al adaptador
            adaptador(mOrdenar);
        }
        //filtrar por no finalizado
        if (id == R.id.filtrar_nofinalizado){
            Toast.makeText(this, R.string.text_filtrarnofin, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mOrdenar = 3;
            //llama al adaptador
            adaptador(mOrdenar);
        }
        return super.onOptionsItemSelected(item);
    }


    //devolucion de llamada de la segunda actividad, inserta la palabra en la BBDD
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TAREA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Tarea tarea = new Tarea(
                    data.getStringExtra(NewTareaActivity.EXTRA_TITULO),
                    data.getStringExtra(NewTareaActivity.EXTRA_DESCRIPCION),
                    data.getStringExtra(NewTareaActivity.EXTRA_FECHA),
                    data.getStringExtra(NewTareaActivity.EXTRA_FECHAFIN),
                    data.getStringExtra(NewTareaActivity.EXTRA_HORAFIN),
                    data.getBooleanExtra(NewTareaActivity.EXTRA_FINALIZADO, false));
            //Guarda los datos
            mTareaViewModel.insert(tarea);
        } else if (requestCode == UPDATE_TAREA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String titulo = data.getStringExtra(NewTareaActivity.EXTRA_TITULO);
            String descripcion = data.getStringExtra(NewTareaActivity.EXTRA_DESCRIPCION);
            String fecha = data.getStringExtra(NewTareaActivity.EXTRA_FECHA);
            String fechafin = data.getStringExtra(NewTareaActivity.EXTRA_FECHAFIN);
            String horafin = data.getStringExtra(NewTareaActivity.EXTRA_HORAFIN);
            Boolean finalizado = data.getBooleanExtra(NewTareaActivity.EXTRA_FINALIZADO, false);
            int identificador = data.getIntExtra(NewTareaActivity.EXTRA_ID, -1);
            if (identificador != -1) {
                mTareaViewModel.update(new Tarea(identificador, titulo, descripcion, fecha, fechafin, horafin, finalizado));
            } else  {
                Toast.makeText(this, "No actualizado", Toast.LENGTH_LONG).show();
            }


        } else if ( resultCode != RESULT_FIRST_USER) {
            //Toast y vuelve a abrir la segunda actividad
            Toast.makeText(
                    this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(MainActivity.this, NewTareaActivity.class);
            //startActivityForResult(intent, NEW_TAREA_ACTIVITY_REQUEST_CODE);
        }

        //boton borrar
        if ( resultCode == RESULT_FIRST_USER) {

            //para confirmar
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage("Desea borrar la tarea:"+tareaPulsada.getTitulo())
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cuando le dices que si
                            //toast
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.delete_word_preamble) + " " +
                                            tareaPulsada.getTitulo(), Toast.LENGTH_LONG).show();
                            //Eliminar tarea
                            mTareaViewModel.deleteTarea(tareaPulsada);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //cuando le dices que no
                            //vuelve a abrir la tarea que no quieres borrar
                            clickenitem(tareaPulsada);
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Confirmar");
            titulo.show();

        }
    }


    //click en item
    public void clickenitem(Tarea tarea) {
        Intent intent = new Intent(this, NewTareaActivity.class);
        intent.putExtra(EXTRA_DATA_ID, tarea.getIdentificador());
        intent.putExtra(EXTRA_DATA_UPDATE_TITULO, tarea.getTitulo());
        intent.putExtra(EXTRA_DATA_UPDATE_DESCRIPCION, tarea.getDescripcion());
        intent.putExtra(EXTRA_DATA_UPDATE_FECHA, tarea.getFecha());
        intent.putExtra(EXTRA_DATA_UPDATE_FECHAFIN, tarea.getFechafin());
        intent.putExtra(EXTRA_DATA_UPDATE_HORAFIN, tarea.getHorafin());
        intent.putExtra(EXTRA_DATA_UPDATE_FINALIZADO, tarea.getFinalizado());
        intent.putExtra(EXTRA_TAREA, tarea.getClass());
        tareaPulsada = tarea;
        startActivityForResult(intent, UPDATE_TAREA_ACTIVITY_REQUEST_CODE);
    }

    //metodo que llama al adaptador para mostrar la lista
    public void adaptador(int mOrdenar) {
        //RecyclerView que llama al adaptador
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TareaListAdapter adapter = new TareaListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //lo utilizamos para que cuando se destruya la actividad, no se borre
        mTareaViewModel = ViewModelProviders.of(this).get(TareaViewModel.class);

        //ordenar por fecha
        if (mOrdenar == 1) {
            mTareaViewModel.getAllfechasTareas().observe(this, new Observer<List<Tarea>>() {
                @Override
                public void onChanged(@Nullable final List<Tarea> tareas) {
                    adapter.setTareas(tareas);
                }
            });

        }
        //ordenar alfabeticamente
        if (mOrdenar == 0) {
            //observador, que actualiza los cambios cuando se producen
            //Obtiene todas las tareas de la BBDD y la asocia al adaptador
            mTareaViewModel.getAllTareas().observe(this, new Observer<List<Tarea>>() {
                @Override
                public void onChanged(@Nullable final List<Tarea> tareas) {
                    //Actualiza las tareas en cache
                    adapter.setTareas(tareas);
                }
            });
        }
        //filtrar por finalizado
        if (mOrdenar == 2) {
            mTareaViewModel.getAllfinalizadoTareas().observe(this, new Observer<List<Tarea>>() {
                @Override
                public void onChanged(@Nullable final List<Tarea> tareas) {
                    adapter.setTareas(tareas);
                }
            });
        }
        //filtrar por no finalizado
        if (mOrdenar == 3) {
            mTareaViewModel.getAllnofinalizadoTareas().observe(this, new Observer<List<Tarea>>() {
                @Override
                public void onChanged(@Nullable final List<Tarea> tareas) {
                    adapter.setTareas(tareas);
                }
            });
        }
    }

}
