package com.android.example.Tareas;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //var para todas las interacciones de la actividad
    private TareaViewModel mTareaViewModel;

    //click en item
    public static final int NEW_TAREA_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_TAREA_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_DATA_UPDATE_TITULO = "extra_data_update_titulo";
    public static final String EXTRA_DATA_UPDATE_DESCRIPCION = "extra_data_update_descripcion";
    public static final String EXTRA_DATA_UPDATE_FECHA = "extra_data_update_fecha";
    public static final String EXTRA_DATA_UPDATE_FECHAFIN = "extra_data_update_fechafin";
    public static final String EXTRA_DATA_UPDATE_HORAFIN = "extra_data_update_horafin";
    public static final String EXTRA_DATA_UPDATE_FINALIZADO = "extra_data_update_finalizado";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_TAREA = "extra_tarea";
    public static final String EXTRA_DATA_UPDATE_ALARMAID = "extra_alarmaid";
    public static final String EXTRA_DATA_UPDATE_ALARMAACTIVADA = "extra_alarmaactivada";
    //para ordenar la lista
    public int mOrdenar=1;
    //para filtrar la lista
    public int mFiltrar=1;
    //tarea que se pulsa
    public Tarea tareaPulsada;
    //deslizar hacia abajo
    SwipeRefreshLayout swipeRefreshLayout;

    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Poner el icono en action Bar
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_recortado);

        //actualizar lista al deslizar abajo
        swipeRefreshLayout =findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //llama al adaptador
                mFiltrar = 1;
                adaptador(mOrdenar,mFiltrar,"");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //RecyclerView donde vamos a poner la lista de tareas
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
                    //actualiza la lista de tareas
                    adaptador(mOrdenar,mFiltrar,"");
                }
            });

        //Opciones del boton flotante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //al dar clic en FAB llama a la clase para crear otra tarea
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
                        alerta.setMessage(R.string.alertaborrar)
                                .setCancelable(false)
                                .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener(){

                                    //si
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //toast
                                                Toast.makeText(MainActivity.this,
                                                        getString(R.string.delete_word_preamble) + " " +
                                                                myTarea.getTitulo(), Toast.LENGTH_LONG).show();

                                                //Eliminar alarma
                                                GestionAlarmas.deleteAlarm(Integer.parseInt(myTarea.getAlarmaid()), MainActivity.this);

                                                //Eliminar tarea
                                                mTareaViewModel.deleteTarea(myTarea);
                                            }
                                        })
                                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                    //no
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        //actualiza la lista de tareas
                                        adaptador(mOrdenar,mFiltrar,"");
                                    }
                                });
                        AlertDialog titulo = alerta.create();
                        titulo.setTitle(R.string.text_alerd_borrar);
                        titulo.show();

                    }
                });
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

        //cuando el modo oscuro este activado, cambia el texto por modo dia
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else {
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }

        //variables para buscar Tareas
        final MenuItem searchItem = menu.findItem(R.id.buscar_tarea);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //cuando introduzcas texto en el search
            @Override
            public boolean onQueryTextChange(String newText) {

                //cambiamos el valor para acceder a buscartarea
                mOrdenar = 0;
                //pasamos la parlabra para buscar al adaptador
                adaptador(mOrdenar,mFiltrar,newText);

                return true;
            }

        });
        return true;
    }

    //Item de menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Boton de informacion
        if (id == R.id.info) {
            //para confirmar
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage(R.string.text_informacion)
                    .setCancelable(false)
                    .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener(){
                        //si
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle(R.string.text_info);
            titulo.show();

            return true;
        }

        //Boton pra borrar todo
        if (id == R.id.clear_data) {
            //para confirmar
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage(R.string.alertaborrartodas)
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener(){
                        //si
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.clear_data_toast_text), Toast.LENGTH_LONG).show();
                            //pasamos el contador de alarmas, con el SharedPreferences
                            int contid = settings.getInt("contadoralarmaid",0);
                            //Eliminar todas las alarmas
                            for (int i = 0;i<contid;i++) {
                                GestionAlarmas.deleteAlarm(i, MainActivity.this);
                            }
                            //borrar alarmas de ejemplo
                            for (int i = -7;i<0;i++) {
                                GestionAlarmas.deleteAlarm(i, MainActivity.this);
                            }

                            //reiniciamos el contador del contadoralaraid
                            SharedPreferences.Editor edit = settings.edit();
                            contid=0;
                            edit.putInt("contadoralarmaid",contid);
                            edit.commit();

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
            titulo.setTitle(R.string.text_alerd_borrar);
            titulo.show();

            return true;
        }
        //ordenar alfabeticamente
        if (id == R.id.ordenar_alfabeticamente) {
            //mensaje toast
            Toast.makeText(this, R.string.ordenar_alfabeticamente, Toast.LENGTH_LONG).show();

            //Cambiar valor de variable
            mOrdenar = 1;
            //llama al adaptador
            adaptador(mOrdenar,mFiltrar,"");

            return true;
        }
        //ordenar por fecha
        if (id == R.id.ordenar_fecha) {
            //mensaje toast
            Toast.makeText(this, R.string.ordenar_fecha, Toast.LENGTH_LONG).show();

            //Cambiar valor de variable
            mOrdenar = 2;
            //llama al adaptador
            adaptador(mOrdenar,mFiltrar,"");

            return true;
        }
        //filtrar todas
        if (id == R.id.filtrar_todas){
            Toast.makeText(this, R.string.text_mostrartodas, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mFiltrar = 1;
            //llama al adaptador
            adaptador(mOrdenar,mFiltrar,"");
        }
        //filtrar por finalizado
        if (id == R.id.filtrar_finalizado){
            Toast.makeText(this, R.string.text_filtrarfin, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mFiltrar = 2;
            //llama al adaptador
            adaptador(mOrdenar,mFiltrar,"");
        }
        //filtrar por no finalizado
        if (id == R.id.filtrar_nofinalizado){
            Toast.makeText(this, R.string.text_filtrarnofin, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mFiltrar = 3;
            //llama al adaptador
            adaptador(mOrdenar,mFiltrar,"");
        }
        //filtrar por alarma activada
        if (id == R.id.filtrar_alarma){
            Toast.makeText(this, R.string.text_filtrar_alarma, Toast.LENGTH_LONG).show();
            //Cambiar valor de variable
            mFiltrar = 4;
            //llama al adaptador
            adaptador(mOrdenar,mFiltrar,"");
        }
        //modo oscuro
        if (id == R.id.night_mode){
            //Establece el modo del tema
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
            }
            //Recrear la actividad para aplicar el efecto
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }


    //devolucion de llamada de la segunda actividad, inserta la palabra en la BBDD
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //introduce una nueva tarea
        if (requestCode == NEW_TAREA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Tarea tarea = new Tarea(
                    data.getStringExtra(NewTareaActivity.EXTRA_TITULO),
                    data.getStringExtra(NewTareaActivity.EXTRA_DESCRIPCION),
                    data.getStringExtra(NewTareaActivity.EXTRA_FECHA),
                    data.getStringExtra(NewTareaActivity.EXTRA_FECHAFIN),
                    data.getStringExtra(NewTareaActivity.EXTRA_HORAFIN),
                    data.getBooleanExtra(NewTareaActivity.EXTRA_FINALIZADO, false),
                    data.getStringExtra(NewTareaActivity.EXTRA_ALARMAID),
                    data.getBooleanExtra(NewTareaActivity.EXTRA_ALARMAACTIVADA,false));
            //Guarda los datos
            mTareaViewModel.insert(tarea);
            //suma uno al contador de alarmas con SharedPreferences
            int cont = settings.getInt("contadoralarmaid", 0);
            SharedPreferences.Editor edit = settings.edit();
            cont++;
            edit.putInt("contadoralarmaid",cont);
            edit.commit();

            //actualiza la tarea
        } else if (requestCode == UPDATE_TAREA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String titulo = data.getStringExtra(NewTareaActivity.EXTRA_TITULO);
            String descripcion = data.getStringExtra(NewTareaActivity.EXTRA_DESCRIPCION);
            String fecha = data.getStringExtra(NewTareaActivity.EXTRA_FECHA);
            String fechafin = data.getStringExtra(NewTareaActivity.EXTRA_FECHAFIN);
            String horafin = data.getStringExtra(NewTareaActivity.EXTRA_HORAFIN);
            Boolean finalizado = data.getBooleanExtra(NewTareaActivity.EXTRA_FINALIZADO, false);
            String alarmaid= data.getStringExtra(NewTareaActivity.EXTRA_ALARMAID);
            Boolean alarmaactivada = data.getBooleanExtra(NewTareaActivity.EXTRA_ALARMAACTIVADA, false);
            int identificador = data.getIntExtra(NewTareaActivity.EXTRA_ID, -1);
            if (identificador != -1) {
                mTareaViewModel.update(new Tarea(identificador, titulo, descripcion, fecha, fechafin, horafin, finalizado, alarmaid, alarmaactivada));
            } else  {
                Toast.makeText(this, "No actualizado", Toast.LENGTH_LONG).show();
            }

            //cuando la tarea no se guarda
        } else if ( resultCode != RESULT_FIRST_USER) {
            Toast.makeText(
                    this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }

        //click en boton borrar
        if ( resultCode == RESULT_FIRST_USER) {

            //para confirmar
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage("Desea borrar la tarea:"+tareaPulsada.getTitulo())
                    .setCancelable(false)
                    //si
                    .setPositiveButton("Si", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.delete_word_preamble) + " " +
                                            tareaPulsada.getTitulo(), Toast.LENGTH_LONG).show();
                            //eliminar alarma
                            GestionAlarmas.deleteAlarm(Integer.parseInt(tareaPulsada.getAlarmaid()), MainActivity.this);
                            //Eliminar tarea
                            mTareaViewModel.deleteTarea(tareaPulsada);
                        }
                    })
                    //no
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //vuelve a abrir la tarea que no quieres borrar
                            clickenitem(tareaPulsada);
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle(R.string.text_alerd_borrar);
            titulo.show();

        }

    }


    //click en item, pasa los atributoas a la segunda actividad
    public void clickenitem(Tarea tarea) {
        Intent intent = new Intent(this, NewTareaActivity.class);
        intent.putExtra(EXTRA_DATA_ID, tarea.getIdentificador());
        intent.putExtra(EXTRA_DATA_UPDATE_TITULO, tarea.getTitulo());
        intent.putExtra(EXTRA_DATA_UPDATE_DESCRIPCION, tarea.getDescripcion());
        intent.putExtra(EXTRA_DATA_UPDATE_FECHA, tarea.getFecha());
        intent.putExtra(EXTRA_DATA_UPDATE_FECHAFIN, tarea.getFechafin());
        intent.putExtra(EXTRA_DATA_UPDATE_HORAFIN, tarea.getHorafin());
        intent.putExtra(EXTRA_DATA_UPDATE_FINALIZADO, tarea.getFinalizado());
        intent.putExtra(EXTRA_DATA_UPDATE_ALARMAID, tarea.getAlarmaid());
        intent.putExtra(EXTRA_DATA_UPDATE_ALARMAACTIVADA, tarea.getAlarmaactivada());
        intent.putExtra(EXTRA_TAREA, tarea.getClass());
        tareaPulsada = tarea;
        startActivityForResult(intent, UPDATE_TAREA_ACTIVITY_REQUEST_CODE);
    }

    //metodo que llama al adaptador para mostrar la lista
    public void adaptador(int mOrdenar,int mFiltrar, final String newText) {
        //RecyclerView que llama al adaptador
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TareaListAdapter adapter = new TareaListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //lo utilizamos para que cuando se destruya la actividad, no se borre
        mTareaViewModel = ViewModelProviders.of(this).get(TareaViewModel.class);

        //buscar tarea
        if (mOrdenar == 0) {
            //llama al dao para hacer la consulta en la BBDD
            mTareaViewModel.getAllTareas().observe(this, new Observer<List<Tarea>>() {
                @Override
                public void onChanged(@Nullable final List<Tarea> tareas) {
                    //filtamos con otro metodo
                    ArrayList<Tarea>listaFiltrada=filter((ArrayList<Tarea>) tareas,newText);
                    //y pasamos la lista filtrada
                    adapter.setTareas(listaFiltrada);
                }
            });
        }

        //ordenar alfabeticamente
        if (mOrdenar == 1) {
            //filtrar todas
            if (mFiltrar == 1) {
                mTareaViewModel.getAllTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }
            //filtra por finalizado
            if (mFiltrar == 2) {
                mTareaViewModel.getAllfinalizadoTareas().observe(this, new Observer<List<Tarea>>() {
                            @Override
                            public void onChanged(@Nullable final List<Tarea> tareas) {
                                adapter.setTareas(tareas);
                            }
                });
            }
            //filtrar por no finalizado
            if (mFiltrar == 3) {
                mTareaViewModel.getAllnofinalizadoTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }
            //filtrar por alarma activada
            if (mFiltrar == 4) {
                mTareaViewModel.getAllAlarmaTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }
        }

        //ordenar por fecha
        if (mOrdenar == 2) {
            //filtrar todas
            if (mFiltrar == 1) {
                mTareaViewModel.getAllfechasTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }
            //filtra por finalizado
            if (mFiltrar == 2) {
                mTareaViewModel.getAllfinalizadofechaTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }
            //filtrar por no finalizado
            if (mFiltrar == 3) {
                mTareaViewModel.getAllnofinalizadofechaTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }
            //filtrar por alarma activada
            if (mFiltrar == 4) {
                mTareaViewModel.getAllAlarmafechaTareas().observe(this, new Observer<List<Tarea>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tarea> tareas) {
                        adapter.setTareas(tareas);
                    }
                });
            }

        }

    }

    //filtrar para buscar tareas
    private ArrayList<Tarea> filter(ArrayList<Tarea> tarea,String texto){
        ArrayList<Tarea>listaFiltrada = new ArrayList<>();
        try {
            texto = texto.toLowerCase();
            for(Tarea tare: tarea){
                String tarea2=tare.getTitulo().toLowerCase();
                //si el titulo de la tarea contiene el texto, se añade a la nueva lista filtrada
                if (tarea2.contains(texto)){
                    listaFiltrada.add(tare);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }

}
