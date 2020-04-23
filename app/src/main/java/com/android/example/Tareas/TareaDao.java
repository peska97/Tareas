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

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) for a word.
 * Each method performs a database operation, such as inserting or deleting a word,
 * running a DB query, or deleting all words.
 */

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


}
