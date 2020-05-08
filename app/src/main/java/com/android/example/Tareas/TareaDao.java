package com.android.example.Tareas;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TareaDao {

    //metodo para insertar palabra
    //ignora que introduzcas otra palabra igual, ya que las palabras son clave
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Tarea tarea);


    //metodo para eliminar todas las palabras
    @Query("DELETE FROM tarea_table")
    void deleteAll();

    //metodo para eliminar una palabras
    @Delete
    void deleteTarea(Tarea tarea);

    //consulta para comprobar que hay tareas
    @Query("SELECT * from tarea_table LIMIT 1")
    Tarea[] getAnyTarea();

    //consulta SQL que obtiene todas las palabras por orden alfabetico
    @Query("SELECT * from tarea_table ORDER BY titulo ASC")
    //metodo que debuelve una lista de palabras
    //utilizamos liveData para responder a los cambios de los datos
    LiveData<List<Tarea>> getAllTareas();

    //metodo para ordenar por fecha
    @Query("SELECT * from tarea_table ORDER BY fechafin ASC")
    LiveData<List<Tarea>> getAllfechasTareas();

    //metodo para filtrar por finalizadas
    @Query("SELECT * from tarea_table WHERE finalizado")
    LiveData<List<Tarea>> getAllfinalizadoTareas();

    //metodo para filtrar por no finalizadas
    @Query("SELECT * from tarea_table WHERE not finalizado")
    LiveData<List<Tarea>> getAllnofinalizadoTareas();

    //metodo para actualizar tareas
    @Update
    void update(Tarea... tarea);

}
