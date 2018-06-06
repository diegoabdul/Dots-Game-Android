package com.mygdx.dots;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class FichaInt extends Ficha{
    public static final int colores=5;
   /* public static final int COLOR_AZUL = 0;
    public static final int COLOR_ROJO = 1;
    public static final int COLOR_AMARILLO = 2;
    public static final int COLOR_VERDE= 3;
    public static final int COLOR_MORADO = 4;*/

    public FichaInt(){
        Random generator = new Random();
        this.value = generator.nextInt(colores);
    }

    public int getColor(){
        return this.value;
    }

    public void setColor(int color){

        this.value=color;
    }
    public void setColorRandom(){
        Random generator = new Random();
        this.value= generator.nextInt(colores);
    }
}