
package com.android.example.Tareas;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;


//clase cuyo papel es proporcionar datos a la interfaz de usuario y sobrevivir a los cambios de configuración, actua como un centro de comunicacion entre el repositorio y la interfaz de usuario
public class TareaViewModel extends AndroidViewModel {

    //var referencia del repositorio
    private TareaRepository mRepository;

    //var almacena el cache la lista de tareas
    private LiveData<List<Tarea>> mAllTareas;
    private LiveData<List<Tarea>> mAllfechasTareas;

    //constructor con las var anteriores
    public TareaViewModel(Application application) {
        super(application);
        mRepository = new TareaRepository(application);
        mAllTareas = mRepository.getAllTareas();
        mAllfechasTareas = mRepository.getAllfechasTareas();
    }

    //metodo getter para obtener las palabras
    LiveData<List<Tarea>> getAllTareas() {
        return mAllTareas;
    }
    LiveData<List<Tarea>> getAllfechasTareas() {
        return mAllfechasTareas;
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
