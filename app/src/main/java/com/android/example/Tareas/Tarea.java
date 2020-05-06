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
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "tarea_table")
public class Tarea {

    @PrimaryKey(autoGenerate = true)
    private int identificador;

    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "fechafin")
    private String fechafin;

    @ColumnInfo(name = "finalizado")
    private Boolean finalizado;


    public Tarea(@NonNull String titulo, String descripcion, String fecha, String fechafin, Boolean finalizado){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.fechafin = fechafin;
        this.finalizado = finalizado;
    }

    @Ignore
    public Tarea(int identificador, @NonNull String titulo, String descripcion, String fecha, String fechafin, Boolean finalizado) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.finalizado = finalizado;
    }



    public  int getIdentificador() {return this.identificador;}
    public String getTitulo(){
        return this.titulo;
    }
    public String getDescripcion(){return this.descripcion;}
    public String getFecha(){
        return this.fecha;
    }
    public String getFechafin(){
        return this.fechafin;
    }
    public Boolean getFinalizado(){
        return this.finalizado;
    }

    public void setIdentificador(int identificador) {this.identificador = identificador;}
    public void setTitulo() { this.titulo = titulo; }
    public void setDescripcion() { this.descripcion = descripcion; }
    public void setFecha() { this.fecha = fecha; }
    public void setFechafin() { this.fechafin = fechafin; }
    public void setFinalizado() { this.finalizado = finalizado; }
}

