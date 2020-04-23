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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * This is the adapter for the RecyclerView that displays
 * a list of words.
 */

//adaptador
public class TareaListAdapter extends RecyclerView.Adapter<TareaListAdapter.TareaViewHolder> {

    private final LayoutInflater mInflater;
    private List<Tarea> Tareas; // Cached copy of words
    //click en item
    private static ClickListener clickListener;

    TareaListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TareaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        if (Tareas != null) {
            Tarea current = Tareas.get(position);
            holder.tareaItemView.setText(current.getTitulo());
        } else {
            // Covers the case of data not being ready yet.
            // holder.wordItemView.setText("No Tarea");
            holder.tareaItemView.setText(R.string.no_tarea);
        }
    }

    /**
     *     Associate a list of tareas with this adapter
    */

    void setWords(List<Tarea> tareas) {
        Tareas = tareas;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTareas has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (Tareas != null)
            return Tareas.size();
        else return 0;
    }

    /**
     * Get the word at a given position.
     * This method is useful for identifying which word
     * was clicked or swiped in methods that handle user events.
     *
     * @param position
     * @return The word at the given position
     */
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
