package app.android.example.Tareas;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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

        private static String [] descripciones = {"leche, huevos y azucar", "la cocina y el comedor", "y despues barrer y fregar", "Filosofia y matematicas", "camisetas y pantalones",
                "de ropa blanca y de color", "tambien los basos"};
        private static String fecha = "1/1/1";
        private static String fechafin = "01/01/2022";
        private static String horafin = "00:00";
        private static Boolean finalizado = false;
        private static String[] alarmaid = {"-1","-2","-3","-4","-5","-6","-7"};
        private static Boolean alarmafinalizada = false;

        PopulateDbAsync(TareaRoomDatabase db) {
            mDao = db.TareaDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //aqui se comprueba que la BBDD esta vacia
            if (mDao.getAnyTarea().length < 1) {
                for (int i = 0; i <= titulos.length - 1; i++) {
                    Tarea tarea = new Tarea(titulos[i], descripciones[i], fecha, fechafin, horafin, finalizado, alarmaid[i],alarmafinalizada);
                    mDao.insert(tarea);
                }
            }
            return null;
        }
    }
}

