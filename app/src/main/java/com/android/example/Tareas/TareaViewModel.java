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
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * The TareaViewModel provides the interface between the UI and the data layer of the app,
 * represented by the repository
 */

//clase cuyo papel es proporcionar datos a la interfaz de usuario y sobrevivir a los cambios de configuración, actua como un centro de comunicacion entre el repositorio y la interfaz de usuario
public class TareaViewModel extends AndroidViewModel {

    //var referencia del repositorio
    private TareaRepository mRepository;

    //var almacena el cache la lista de tareas
    private LiveData<List<Tarea>> mAllTareas;

    //constructor con las var anteriores
    public TareaViewModel(Application application) {
        super(application);
        mRepository = new TareaRepository(application);
        mAllTareas = mRepository.getAllTareas();
    }

    //metodo getter para obtener las palabras
    LiveData<List<Tarea>> getAllTareas() {
        return mAllTareas;
    }

    //metodo insert contenedor, para que este oculta al usuario
    public void insert(Tarea tarea) {
        mRepository.insert(tarea);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteTarea(Tarea tarea) {
        mRepository.deleteTarea(tarea);
    }

    public  void update(Tarea tarea) {mRepository.update(tarea);}
}
