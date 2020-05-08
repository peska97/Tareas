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

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TareaRepository {

    //var miembro para el DAO
    private TareaDao mTareaDao;
    //lista de palabras
    private LiveData<List<Tarea>> mAllTareas;
    private LiveData<List<Tarea>> mAllfechasTareas;
    private LiveData<List<Tarea>> mAllfinalizadoTareas;
    private LiveData<List<Tarea>> mAllnofinalizadoTareas;

    //constructor, obtiene identificacion de BBDD e inicializa las variables
    TareaRepository(Application application) {
        TareaRoomDatabase db = TareaRoomDatabase.getDatabase(application);
        mTareaDao = db.TareaDao();
        mAllTareas = mTareaDao.getAllTareas();
        mAllfechasTareas = mTareaDao.getAllfechasTareas();
        mAllfinalizadoTareas = mTareaDao.getAllfinalizadoTareas();
        mAllnofinalizadoTareas = mTareaDao.getAllnofinalizadoTareas();
    }

    //metodo contenedor, notifica cuando los datos cambian
    LiveData<List<Tarea>> getAllTareas() {
        return mAllTareas;
    }
    LiveData<List<Tarea>> getAllfechasTareas() {
        return mAllfechasTareas;
    }
    LiveData<List<Tarea>> getAllfinalizadoTareas() {
        return mAllfinalizadoTareas;
    }
    LiveData<List<Tarea>> getAllnofinalizadoTareas() { return mAllnofinalizadoTareas;}

    //contenedor para insert, llama a un subproceso para que la aplicacion no se bloque
    public void insert(Tarea tarea) {
        new insertAsyncTask(mTareaDao).execute(tarea);
    }

    public void deleteAll() {
        new deleteAllTareasAsyncTask(mTareaDao).execute();
    }

    public void update(Tarea tarea) {new updateTareasAsyncTask(mTareaDao).execute(tarea);}

    // Need to run off main thread
    public void deleteTarea(Tarea tarea) {
        new deleteTareaAsyncTask(mTareaDao).execute(tarea);
    }



    // Static inner classes below here to run database interactions
    // in the background.

    //Introduce tareas a la BBDD
    private static class insertAsyncTask extends AsyncTask<Tarea, Void, Void> {

        private TareaDao mAsyncTaskDao;

        insertAsyncTask(TareaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Tarea... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //Eliminar todas las tareas de la BBDD
    private static class deleteAllTareasAsyncTask extends AsyncTask<Void, Void, Void> {
        private TareaDao mAsyncTaskDao;

        deleteAllTareasAsyncTask(TareaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    //Elimina una tarea de la BBDD
    private static class deleteTareaAsyncTask extends AsyncTask<Tarea, Void, Void> {
        private TareaDao mAsyncTaskDao;

        deleteTareaAsyncTask(TareaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Tarea... params) {
            mAsyncTaskDao.deleteTarea(params[0]);
            return null;
        }
    }

    //Actualizar tareas en la BBDD
    private static class updateTareasAsyncTask extends AsyncTask<Tarea, Void, Void> {
        private TareaDao mAsyncTaskDao;

        updateTareasAsyncTask(TareaDao dao) { mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Tarea... params) {
            mAsyncTaskDao.update(params[0], params[0], params[0], params[0], params[0], params[0]);
            return null;
        }
    }
}
