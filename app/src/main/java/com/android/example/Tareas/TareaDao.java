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

    //metodo para insertar Tareas
    //ignora que introduzcas otro titulo igual, ya que los titulos son clave
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

    //consultas SQL para filtrar y ordenar
    //Ordenar por orden alfabetico
        //Todas las tareas
    @Query("SELECT * from tarea_table ORDER BY titulo ASC")
    //metodo que debuelve una lista de palabras
    //utilizamos liveData para responder a los cambios de los datos
    LiveData<List<Tarea>> getAllTareas();
        //Finalizadas
        @Query("SELECT * from tarea_table WHERE finalizado ORDER BY titulo ASC")
        LiveData<List<Tarea>> getAllfinalizadoTareas();
        //No finalizadas
        @Query("SELECT * from tarea_table WHERE not finalizado ORDER BY titulo ASC")
        LiveData<List<Tarea>> getAllnofinalizadoTareas();
        //Alarma activada
        @Query("SELECT * from tarea_table WHERE  alarmaactivada ORDER BY titulo ASC")
        LiveData<List<Tarea>> getAllAlarmaTareas();

    //Ordenar por fecha
        //Todas las tareas
    @Query("SELECT * from tarea_table ORDER BY fechafin ASC")
    LiveData<List<Tarea>> getAllfechasTareas();
        //Finalizadas
        @Query("SELECT * from tarea_table WHERE finalizado ORDER BY fechafin ASC")
        LiveData<List<Tarea>> getAllfinalizadofechaTareas();
        //No finalizadas
        @Query("SELECT * from tarea_table WHERE not finalizado ORDER BY fechafin ASC")
        LiveData<List<Tarea>> getAllnofinalizadofechaTareas();
        //Alarma activada
        @Query("SELECT * from tarea_table WHERE  alarmaactivada ORDER BY fechafin ASC")
        LiveData<List<Tarea>> getAllAlarmafechaTareas();

    //metodo para actualizar tareas
    @Update
    void update(Tarea... tarea);

}
