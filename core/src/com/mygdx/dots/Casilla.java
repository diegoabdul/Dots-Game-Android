package com.mygdx.dots;

import com.badlogic.gdx.graphics.Color;

public class Casilla {

    private int color=1;
    private boolean seleccionada=false;
    private FichaInt fichaint;
    public int fila;
    public int columna;
    //Constructor
    public Casilla(){
        this.fichaint= new FichaInt();
        this.color=fichaint.getColor();
        this.seleccionada=false;

    }

    public Casilla(int fila, int columna,int color){
        this.fila = fila;
        this.columna = columna;
        this.color = color;
    }


    //Getters y Setters

    public int getColor(){
        return fichaint.value;
    }

    public int getColor2(){
        return this.color;
    }

    public void setColor(int color){
        fichaint.value=color;
        this.color=fichaint.value;
    }
    public void setColorRandom(){
        fichaint.setColorRandom();
        this.color = fichaint.getColor();
    }

    public void setSeleccion(boolean seleccion){
        this.seleccionada=seleccion;
    }

    public boolean getSeleccion(){
        return this.seleccionada;
    }
}