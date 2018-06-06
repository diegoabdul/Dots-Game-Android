package com.mygdx.dots;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.IntArray;

import java.util.ArrayList;

import sun.security.provider.SHA;

public class MyDotsGame extends ApplicationAdapter implements InputProcessor {
    ShapeRenderer shapeRenderer;
    public static final int WIDTH =480;
    public static final int HEIGHT = 800;
    public static final float DOT_RADIUS = 100;
    public static final float DOTS_SPACE = 150;
    static final int SEGMENTS = 64;
    public static final int RESUME_BUTTON = 500;
    public static final float DOT_CENTER_X_LEFT_BOTTOM = (0.5f * DOTS_SPACE) + DOT_RADIUS;
    public static final float DOT_CENTER_Y_LEFT_BOTTOM = (0.5f * DOTS_SPACE) + DOT_RADIUS;
    public int contdots;
    public int movimientos=30;
    private Texture resumeButton;
    public boolean poligono=false;
    private Sound slidingSound;
    private Tablero tablero;
    BitmapFont font;
    OrthographicCamera camera;
    SpriteBatch batch;
    static final float CAMARA_X = 1050;
    static final float CAMARA_Y = 1050;
    Casilla dot;
    Vector3 lastTouch;
    ArrayList<Casilla> camino = new ArrayList<Casilla>();
    public IntArray Coordenadas = new IntArray(2*contdots);
    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        tablero = new Tablero();
        tablero.LlenarMatriz();
        slidingSound = Gdx.audio.newSound(Gdx.files.internal("Mario.mp3"));
        font = new BitmapFont();
        font.setColor(Color.DARK_GRAY);
        resumeButton = new Texture("reboot.png");

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        slidingSound.dispose();
        batch.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        float aspectratio = 1.0f * width / height;
        camera.setToOrtho(false, width, height);
        camera.viewportHeight= 2*2100;
        camera.viewportWidth = aspectratio * camera.viewportHeight;
        Gdx.app.log("resize", "viewportHeight="+camera.viewportHeight+",camera.viewportWidth="+camera.viewportWidth);
        Gdx.app.log("resize", "center viewportWidth="+ (0.5f * camera.viewportWidth));
        camera.position.set(CAMARA_X, CAMARA_Y, 0);

    }


    @Override
    public void render () {


        Gdx.gl.glClearColor(1, 1, 1, 1);   //1111 es blanco
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        tablero.draw(shapeRenderer);

        if (camino != null && camino.size() > 1) {
            int numLados = camino.size() - 1;
            for (int i = 0; i < numLados; i++) {
                shapeRenderer.setColor(tablero.colors[dot.getColor2()]);
                shapeRenderer.rectLine(DOT_CENTER_X_LEFT_BOTTOM + (camino.get(i).columna * (2 * DOT_RADIUS + DOTS_SPACE)),
                        DOT_CENTER_Y_LEFT_BOTTOM + (camino.get(i).fila * (2 * DOT_RADIUS + DOTS_SPACE)),
                        DOT_CENTER_X_LEFT_BOTTOM + (camino.get(i + 1).columna * (2 * DOT_RADIUS + DOTS_SPACE)),
                        DOT_CENTER_Y_LEFT_BOTTOM + (camino.get(i + 1).fila * (2 * DOT_RADIUS + DOTS_SPACE)),
                        50);
            }
        }


        if ((dot != null) && (lastTouch != null)) {
            shapeRenderer.setColor(tablero.colors[dot.getColor2()]);
            shapeRenderer.rectLine(DOT_CENTER_X_LEFT_BOTTOM + (dot.columna * (2 * DOT_RADIUS + DOTS_SPACE)),
                    DOT_CENTER_Y_LEFT_BOTTOM + (dot.fila * (2 * DOT_RADIUS + DOTS_SPACE)),
                    lastTouch.x,
                    lastTouch.y,
                    50);
        }

        shapeRenderer.end();
        batch.begin();
        font.getData().setScale(3);
        font.draw(batch, "Score =" + tablero.getPuntaje(), 100, Gdx.graphics.getHeight()-50, 300, 500, true);
        font.draw(batch, "Moves=" + movimientos, 700, Gdx.graphics.getHeight()-50, 300, 500, true);

        batch.end();

        if(movimientos==0) {
            batch.begin();
            batch.draw(
                    resumeButton,
                    WIDTH-180,
                    HEIGHT-180,
                    RESUME_BUTTON,
                    RESUME_BUTTON);
            batch.end();
                resetGame();
        }
    }


    @Override
    public void pause() {
        if (Gdx.input.justTouched()) {
                resetGame();
            }
        }

    @Override
    public void resume() {
    }
    private void resetGame() {
        if (Gdx.input.justTouched()) {
            tablero = new Tablero();
            tablero.LlenarMatriz();
            movimientos = 30;
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("touchDown", "x=" + screenX + ",y=" + screenY);

        this.dot = tablero.screen2dot(screenX, screenY, camera);
        if (dot != null) {
            this.camino.add(dot);
            contdots++;
            Gdx.app.log("dot","agregado");
            lastTouch = camera.unproject(new Vector3(screenX, screenY, 0));
            Coordenadas.add(dot.fila);
            Coordenadas.add(dot.columna);

            tablero.Seleccion(Coordenadas.get(0),Coordenadas.get(1));



        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("touchUp", "x="+screenX+",y="+screenY);


        if(camino.size()>1) {
            poligono = tablero.Poligono(Coordenadas);
            slidingSound.play();
            tablero.ActualizarTablero(poligono);
            movimientos--;
        }
        this.dot = null;
        this.camino = new ArrayList<Casilla>();
        contdots =0;
        Coordenadas = new IntArray(2*contdots);


        lastTouch = null;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log("touchDragged", "x="+screenX+",y="+screenY);
        Casilla dot2 = tablero.screen2dot(screenX, screenY,camera);
        if((dot != null) && (dot2 != null)){
            if(tablero.isValid(dot.fila, dot.columna, dot2.fila, dot2.columna)){
                if(!tablero.existInPath(dot2,camino)){
                    camino.add(dot2);
                    contdots++;
                    Gdx.app.log("dot2","agregado");
                    //NUEVO
                    Coordenadas.add(dot2.fila);
                    Coordenadas.add(dot2.columna);
                    tablero.Seleccion(Coordenadas.get(Coordenadas.size-2),Coordenadas.get(Coordenadas.size-1));
                    dot = dot2;
                }
            }
        }

        lastTouch = camera.unproject(new Vector3(screenX, screenY, 0));

        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }




}