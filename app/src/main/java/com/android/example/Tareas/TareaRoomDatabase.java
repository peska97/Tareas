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

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * TareaRoomDatabase. Includes code to create the database.
 * After the app creates the database, all further interactions
 * with it happen through the TareaViewModel.
 */

//entidad para la BBDD
    //version 2 para actualizar tareas
@Database(entities = {Tarea.class}, version = 2, exportSchema = false)
public abstract class TareaRoomDatabase extends RoomDatabase {

    //Definimos la DAO de la BBDD
    public abstract TareaDao TareaDao();

    private static TareaRoomDatabase INSTANCE;

    //Creamos TareaRoomDatabase como un singleton para no tener varias instancias abiertas al mismo tiempo
    public static TareaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TareaRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Creamos la BBDD con Room
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TareaRoomDatabase.class, "tarea_database")
                            //limpia y reconstruye la BBDD en lugar de migrar
                            .fallbackToDestructiveMigration()
                            //agrega la devolucion de la llamada, para agregar datos
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }


    //para añadir contenido a la BBDD, si esta vacia
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    //introduce unas tareas por defecto, para que no este vacia
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TareaDao mDao;

        private static String [] titulos = {"Ir a la compra", "Limpiar", "Ordenar habitacion", "Estudiar", "Planchar",
                "Hacer la colada", "Fregar los platos"};

        private static String [] descripciones = {"Ir a la compra", "Limpiar", "Ordenar habitacion", "Estudiar", "Planchar",
                "Hacer la colada", "Fregar los platos"};
        private static String fecha = "1/1/1";
        private static Boolean finalizado = false;

        PopulateDbAsync(TareaRoomDatabase db) {
            mDao = db.TareaDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //aqui se comprueba que la BBDD esta vacia
            if (mDao.getAnyTarea().length < 1) {
                for (int i = 0; i <= titulos.length - 1; i++) {
                    Tarea tarea = new Tarea(titulos[i], descripciones[i], fecha, finalizado));
                    mDao.insert(tarea);
                }
            }
            return null;
        }
    }
}

