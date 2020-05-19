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

    @ColumnInfo(name = "horafin")
    private String horafin;

    @ColumnInfo(name = "finalizado")
    private Boolean finalizado;

    @ColumnInfo(name = "alarmaid")
    private Integer alarmaid;


    public Tarea(@NonNull String titulo, String descripcion, String fecha, String fechafin, String horafin, Boolean finalizado, Integer alarmaid){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.fechafin = fechafin;
        this.horafin = horafin;
        this.finalizado = finalizado;
        this.alarmaid = alarmaid;
    }

    @Ignore
    public Tarea(int identificador, @NonNull String titulo, String descripcion, String fecha, String fechafin, String horafin, Boolean finalizado, Integer alarmaid) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.fechafin = fechafin;
        this.horafin = horafin;
        this.finalizado = finalizado;
        this.alarmaid = alarmaid;
    }



    public  int getIdentificador() {return this.identificador;}
    public String getTitulo(){ return this.titulo; }
    public String getDescripcion(){return this.descripcion;}
    public String getFecha(){ return this.fecha; }
    public String getFechafin(){ return this.fechafin; }
    public String getHorafin() {return this.horafin; }
    public Boolean getFinalizado(){ return this.finalizado; }
    public Integer getAlarmaid(){ return this.alarmaid; }

    public void setIdentificador(int identificador) {this.identificador = identificador;}
    public void setTitulo() { this.titulo = titulo; }
    public void setDescripcion() { this.descripcion = descripcion; }
    public void setFecha() { this.fecha = fecha; }
    public void setFechafin() { this.fechafin = fechafin; }
    public void setHorafin() {this.horafin = horafin; }
    public void setFinalizado() { this.finalizado = finalizado; }
    public void setAlarmaid() { this.alarmaid = alarmaid;}
}