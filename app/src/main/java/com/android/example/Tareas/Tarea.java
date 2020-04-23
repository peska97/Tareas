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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "tarea_table")
public class Tarea {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "descripcion")
    private String descripcion;


    public Tarea(@NonNull String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }


    public String getTitulo(){
        return this.titulo;
    }
    public String getDescripcion(){return this.descripcion;}

    public void setTitulo() { this.titulo = titulo; }
    public void setDescripcion() { this.descripcion = descripcion; }
}

