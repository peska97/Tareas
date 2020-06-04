package com.android.example.Tareas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

//adaptador
public class TareaListAdapter extends RecyclerView.Adapter<TareaListAdapter.TareaViewHolder> {

    private final LayoutInflater mInflater;
    private List<Tarea> Tareas;
    //click en item
    private static ClickListener clickListener;
    private Boolean fin = false;
    private Boolean alarmaactivada = false;
    private int indicador = 0;

    TareaListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //para cambiar el disseño si la tarea esta finalizada
        //para que no de error cuando introduces una nueva o cambias datos y empeza desde 0
        if (indicador==Tareas.size()){
            fin = false;
            alarmaactivada = false;
        }
        else {
            //Cambiamos una a una el diseño
            fin = Tareas.get(indicador).getFinalizado();
            alarmaactivada = Tareas.get(indicador).getAlarmaactivada();
            indicador ++;
        }

            if (fin == true) {
                if(alarmaactivada == true) {
                    View itemView = mInflater.inflate(R.layout.recyclerview_itemfin_alarma, parent, false);
                    return new TareaViewHolder(itemView);
                } else {
                    View itemView = mInflater.inflate(R.layout.recyclerview_itemfin, parent, false);
                    return new TareaViewHolder(itemView);
                }
            } else {
                if(alarmaactivada == true) {
                    View itemView = mInflater.inflate(R.layout.recyclerview_item_alarma, parent, false);
                    return new TareaViewHolder(itemView);
                } else {
                    View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
                    return new TareaViewHolder(itemView);
                }
            }

    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        if (Tareas != null) {
            Tarea current = Tareas.get(position);
            holder.tareaItemView.setText(current.getTitulo());
        } else {
            // Por si los datos no estan listos
            holder.tareaItemView.setText(R.string.no_tarea);
        }
    }


    void setTareas(List<Tarea> tareas) {
        Tareas = tareas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (Tareas != null)
            return Tareas.size();
        else return 0;
    }

    public Tarea getTareaAtPosition(int position) {
        return Tareas.get(position);
    }

    class TareaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tareaItemView;

        private TareaViewHolder(View itemView) {
            super(itemView);
            tareaItemView = itemView.findViewById(R.id.textView);
            //click en item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    //click en item
    public void setOnItemClickListener(ClickListener clickListener) {
        TareaListAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}