package app.android.example.Tareas;

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
    private LiveData<List<Tarea>> mAllfinalizadoTareas;
    private LiveData<List<Tarea>> mAllnofinalizadoTareas;
    private LiveData<List<Tarea>> mAllAlarmaTareas;
    private LiveData<List<Tarea>> mAllfechasTareas;
    private LiveData<List<Tarea>> mAllfinalizadofechaTareas;
    private LiveData<List<Tarea>> mAllnofinalizadofechaTareas;
    private LiveData<List<Tarea>> mAllAlarmafechaTareas;

    //constructor con las var anteriores
    public TareaViewModel(Application application) {
        super(application);
        mRepository = new TareaRepository(application);
        mAllTareas = mRepository.getAllTareas();
        mAllfechasTareas = mRepository.getAllfechasTareas();
        mAllfinalizadoTareas = mRepository.getAllfinalizadoTareas();
        mAllnofinalizadoTareas = mRepository.getAllnofinalizadoTareas();
        mAllAlarmaTareas = mRepository.getAllAlarmaTareas();
        mAllfinalizadofechaTareas = mRepository.getAllfinalizadofechaTareas();
        mAllnofinalizadofechaTareas = mRepository.getAllnofinalizadofechaTareas();
        mAllAlarmafechaTareas = mRepository.getAllAlarmafechaTareas();
    }

    //metodo getter para obtener las palabras
    LiveData<List<Tarea>> getAllTareas() {
        return mAllTareas;
    }
    LiveData<List<Tarea>> getAllfechasTareas() {
        return mAllfechasTareas;
    }
    LiveData<List<Tarea>> getAllfinalizadoTareas() {
        return mAllfinalizadoTareas;
    }
    LiveData<List<Tarea>> getAllnofinalizadoTareas() {
        return mAllnofinalizadoTareas;
    }
    LiveData<List<Tarea>> getAllAlarmaTareas() {
        return mAllAlarmaTareas;
    }
    LiveData<List<Tarea>> getAllfinalizadofechaTareas() {
        return mAllfinalizadofechaTareas;
    }
    LiveData<List<Tarea>> getAllnofinalizadofechaTareas() {
        return mAllnofinalizadofechaTareas;
    }
    LiveData<List<Tarea>> getAllAlarmafechaTareas() {
        return mAllAlarmafechaTareas;
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
