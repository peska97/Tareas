
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
    private List<Tarea> Tareas; // Cached copy of words
    //click en item
    private static ClickListener clickListener;
    private Boolean fin = false;
    TareaListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //para cambiar el disseño si la tarea esta finalizada
            if (fin == true) {
                View itemView = mInflater.inflate(R.layout.recyclerview_itemfin, parent, false);
                return new TareaViewHolder(itemView);
            } else {
                View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
                return new TareaViewHolder(itemView);
            }

    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        if (Tareas != null) {
            if (Tareas.get(position).getFinalizado()==true) {
                fin = true;
            }
            else {
                fin = false;
            }
            Tarea current = Tareas.get(position);
            holder.tareaItemView.setText(current.getTitulo());
        } else {
            // Covers the case of data not being ready yet.
            // holder.wordItemView.setText("No Tarea");
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