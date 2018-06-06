package com.mygdx.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;

import java.util.ArrayList;

public class Tablero {

    private Casilla matriz[][];
    public int nfilas = 6;
    public int ncolumnas = 6;
    public int i=0;
    public int color;
    public int puntaje=0;
    public static final float DOT_RADIUS = 100;
    public static final float DOTS_SPACE = 150;
    static final int SEGMENTS = 64;
    public static final float DOT_CENTER_X_LEFT_BOTTOM = (0.5f * DOTS_SPACE) + DOT_RADIUS;
    public static final float DOT_CENTER_Y_LEFT_BOTTOM = (0.5f * DOTS_SPACE) + DOT_RADIUS;

    int aux,contador=0,contseleccionadas=0,contseguidas=0;

    Color[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.PURPLE};

    public Tablero() {
        this.matriz = new Casilla[nfilas][ncolumnas];
    }

    public void LlenarMatriz() {
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                matriz[x][y] = new Casilla();
            }
        }

    }

    public void draw(ShapeRenderer renderer) {


        float dotcenter_y;
        float dotcenter_x;
        for (int i = 0; i < 6; i++) {
            dotcenter_y = DOT_CENTER_Y_LEFT_BOTTOM + (i * (2 * DOT_RADIUS + DOTS_SPACE));
            for (int j = 0; j < 6; j++) {
                dotcenter_x = DOT_CENTER_X_LEFT_BOTTOM + (j * (2 * DOT_RADIUS + DOTS_SPACE));
                // int color = matriz[i][j].getColor();
                renderer.setColor(colors[matriz[i][j].getColor()]);
                renderer.circle(dotcenter_x, dotcenter_y, DOT_RADIUS, SEGMENTS);
            }

        }
    }


    public Casilla screen2dot(int screenX, int screenY, OrthographicCamera camera) {
        Vector3 punto = camera.unproject(new Vector3(screenX, screenY, 0));
        int lado = (2 * (int) DOT_RADIUS) + (int) DOTS_SPACE;
        int columna = (int) punto.x / lado;
        int fila = (int) punto.y / lado;

        Gdx.app.log("traslate to tile ---- > ", "xt=" + screenX + ",yt=" + screenY + " - x=" + punto.x + ",y=" + punto.y + " => casilla=[" + fila + "," + columna + "]");

        Casilla dot = null;
        if ((fila >= 0) && (fila < 6) && (columna >= 0) && (columna < 6)) {
            dot = new Casilla(fila, columna, matriz[fila][columna].getColor());
        }

        return dot;
    }

    public boolean isValid(int x1, int y1, int x2, int y2) {
        boolean valido = (((x2 == x1) && (y2 == (y1 - 1))) ||
                ((x2 == x1 + 1) && (y2 == y1)) ||
                ((x2 == x1) && (y2 == (y1 + 1))) ||
                ((x2 == (x1 - 1)) && (y2 == y1)))
                && (matriz[x1][y1].getColor() == matriz[x2][y2].getColor());
        return valido;
    }

    public boolean existInPath(Casilla dot, ArrayList<Casilla> camino) {
        boolean exist = false;
        //NUEVO
        if(camino.size()>3) {
            for (int i = 0; (i < camino.size() && (!exist)); i++) {
                exist = (dot.fila == camino.get(i).fila) && (dot.columna == camino.get(i).columna);
            }
            return !exist;
        }
        //VIEJO
        for (int i = 0; (i < camino.size() && (!exist)); i++) {
            exist = (dot.fila == camino.get(i).fila) && (dot.columna == camino.get(i).columna);
        }
        return exist;
    }

    public void Seleccion(int fila, int columna){
        matriz[fila][columna].setSeleccion(true);
    }

    public boolean Poligono(IntArray coordenadas){
        if(coordenadas.size<5)
            return false;
        i=0;
        int j=i+2;
        int contador=2;
        while(i<coordenadas.size){
            while(j<coordenadas.size){

                if((coordenadas.get(i)==coordenadas.get(j))&&(coordenadas.get(i+1)==coordenadas.get(j+1))&&(contador<5))
                    return false;
                else if((coordenadas.get(i)==coordenadas.get(j))&&(coordenadas.get(i+1)==coordenadas.get(j+1))&&(contador==5)){
                    return true;
                }
                else
                    j+=2;
                contador++;
            }
            i+=2;
        }
        return false;
    }
    //Nuevo
    public void ActualizarTablero(boolean poligono) {

        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {

                if (matriz[x][y].getSeleccion() == true) {
                    this.color = matriz[x][y].getColor();
                    for (int j =0; j < matriz.length; j++) {  //modificado

                        if (matriz[j][y].getSeleccion() == true) {
                            int h = j;
                            while (h < matriz.length) {
                                if (matriz[h][y].getSeleccion() == true) {
                                    contseguidas++;
                                    matriz[h][y].setSeleccion(false);
                                    contseleccionadas++;
                                }
                                h++;
                            }
                            h = j;
                            while (contseguidas > 0) {
                                h = j;
                                while (h < matriz.length-1) {
                                    matriz[h][y].setColor(matriz[h+1][y].getColor());
                                    h++;
                                }
                                contseguidas--;
                            }
                            while (contseleccionadas > 0) {

                                int k = contseleccionadas - 1;

                                matriz[matriz.length-k-1][y].setColorRandom();
                                contseleccionadas--;
                                this.puntaje++;
                            }
                        }
                    }
                }
            }
        }
        if(poligono){
            for(int x=0;x<matriz.length;x++){
                for (int y=0; y < matriz[x].length; y++) {
                    if(matriz[x][y].getColor()==   this.color)
                        this.puntaje++;
                    while (matriz[x][y].getColor()==   this.color){
                        matriz[x][y].setColorRandom();

                    }
                }
            }
        }

    }

    public int getPuntaje(){
        return puntaje;
    }


}