package tresretos.com.Minijuegos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tresretos.com.R;

/**
 * Created by aerocool on 24/07/17.
 */

public class PuzzleView extends SurfaceView {
    private PuzzleLoopThread puzzleLoopThread;
    private List<PuzzleSprite> puzzleSprites = new ArrayList<PuzzleSprite>();
    private int[][] puzzle;
    private long lastClick;
    private Bitmap foto;
    int ancho, alto;
    Context contexto;
    long crono = 60000;
    boolean resuelto = false;
    int puntos = 0;
    Activity miActividad;
    Canvas micanvas;

    public PuzzleView(Context context, int ancho, int alto) {
        super(context);
        this.contexto = context;
        this.ancho = ancho;
        this.alto = alto;
        this.puzzle = new int[ancho][alto];

        puzzleLoopThread = new PuzzleLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                puzzleLoopThread.setRunning(false);
                while (retry) {
                    try {
                        puzzleLoopThread.join();
                        retry = false;
                        parar();
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                puzzleLoopThread.start();
                puzzleLoopThread.setRunning(true);
                crono = crono + System.currentTimeMillis();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    private void createSprites() {

        Random aleatorio = new Random();
        int imagen = 0;
        int numero_imagenes = 11;

        switch (aleatorio.nextInt(numero_imagenes)){
            case 0:
                imagen = R.drawable.puzzle2;
                break;
            case 1:
                imagen = R.drawable.arbol;
                break;
            case 2:
                imagen = R.drawable.caballero;
                break;
            case 3:
                imagen = R.drawable.castillo;
                break;
            case 4:
                imagen = R.drawable.cheops;
                break;
            case 5:
                imagen = R.drawable.dragon;
                break;
            case 6:
                imagen = R.drawable.leon;
                break;
            case 7:
                imagen = R.drawable.nazca;
                break;
            case 8:
                imagen = R.drawable.reloj;
                break;
            case 9:
                imagen = R.drawable.stonehenge;
                break;
            case 10:
                imagen = R.drawable.yellowstone;
                break;
            case 11:
                imagen = R.drawable.puzzle3;
                break;

        }

        int cnd = 0;

        for (int m = 0; m < alto; m++) {
            for (int n = 0; n < ancho; n++) {
                puzzleSprites.add(createSprite(imagen, n, m, ancho));
                puzzleSprites.get(cnd).setId(cnd);
                cnd++;
            }
        }
        puzzleSprites.get(puzzleSprites.size() - 1).setVisible(false);

        desordenarpuzzle();
    }


    public void desordenarpuzzle() {

        Log.d("SALIDA", "Inicio desordenar puzzle");


        Random aleat = new Random();

        // hacer minimo 8 movimientos y maximo 16 para desordenar
        int desorden = aleat.nextInt(18) + 8;

        int cnd = 0;
        int ultimomov = -1;

        while (cnd < desorden) {
            for (PuzzleSprite tmp : puzzleSprites) {
                // buscar la casilla que no es visible y cambiarla por otra al azar
                if (tmp.isVisible() == false) {

                    // Sacar un movimiento aleatorio solo con lo posible
                    String posibilidad = "";
                    // Izquierda
                    if (tmp.getX() > 0) {
                        if (ultimomov != 1) posibilidad += "3";
                    }
                    // Derecha
                    if (tmp.getX() < ancho - 1) {
                        if (ultimomov != 3) posibilidad += "1";
                    }
                    // Arriba
                    if (tmp.getY()  > 0) {
                        if (ultimomov != 2) posibilidad += "0";
                    }
                    // Abajo
                    if (tmp.getY() < alto - 1) {
                        if (ultimomov != 0) posibilidad += "2";
                    }

                    char movchar = posibilidad.charAt(aleat.nextInt(posibilidad.length()));

                    // Ejecutar el movimiento


                    switch (movchar) {
                        // mover arriba
                        case '0':

                            intercambiar(tmp, tmp.getX(), tmp.getY() - 1);
                            cnd++;
                            ultimomov = 0;

                            break;

                        // mover derecha
                        case '1':

                            intercambiar(tmp, tmp.getX() + 1, tmp.getY());
                            cnd++;
                            ultimomov = 1;

                            break;

                        // mover abajo
                        case '2':

                            intercambiar(tmp, tmp.getX(), tmp.getY() + 1);
                            cnd++;
                            ultimomov = 2;

                            break;
                        // mover izquierda
                        case '3':

                            intercambiar(tmp, tmp.getX() - 1, tmp.getY());
                            cnd++;
                            ultimomov = 3;

                            break;
                    }
                }
            }

        }


        Log.d("SALIDA", "Fin desordenar puzzle");

    }


    public void intercambiar(PuzzleSprite sprite, int x, int y) {
        for (PuzzleSprite tmp2 : puzzleSprites) {
            if (tmp2.getX() == x && tmp2.getY() == y) {

                int tempx = sprite.getX();
                int tempy = sprite.getY();

                sprite.setX(x);
                sprite.setY(y);

                tmp2.setX(tempx);
                tmp2.setY(tempy);

                Log.d("INTERCAMBIAR", "Blanco " + puzzleSprites.get(sprite.getId()).getId() + " Otro " + puzzleSprites.get(tmp2.getId()).getId());

            }
        }

    }


    private PuzzleSprite createSprite(int resource, int n, int m, int ancho) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new PuzzleSprite(this, bmp, n, m, ancho);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        micanvas = canvas;

        // Log.d("TIEMPO", System.currentTimeMillis() - crono +  "");

        Paint pintar = new Paint();
        pintar.setColor(Color.WHITE);
        pintar.setTextSize(82);
        pintar.setAntiAlias(true);
        pintar.setTextAlign(Paint.Align.CENTER);

        long segundos = (crono - System.currentTimeMillis()) / 1000;

        if (segundos<=0) salir();

        long decimas = (crono - System.currentTimeMillis()) % 1000;
        String decimastxt = decimas + "  ";

        canvas.drawText(segundos + ":" + decimastxt.charAt(0), canvas.getWidth() / 2, canvas.getHeight() - 10, pintar);

        for (PuzzleSprite puzzleSprite : puzzleSprites) {
            puzzleSprite.onDraw(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = puzzleSprites.size() - 1; i >= 0; i--) {
                    PuzzleSprite puzzleSprite = puzzleSprites.get(i);

                    if (puzzleSprite.isCollition(x, y)) {
                        // puzzleSprites.remove(puzzleSprite);
                        //   Toast.makeText(contexto, "PuzzleSprite num: " + i, Toast.LENGTH_SHORT).show();
                        comprobar(puzzleSprite);
                        resuelto();
                        break;
                    }
                }
            }
        }
        return true;
    }

    public void comprobar(PuzzleSprite spritetocado) {

        int posicionx = spritetocado.getX();
        int posiciony = spritetocado.getY();

        int posicionhuecox = -1;
        int posicionhuecoy = -1;

        PuzzleSprite vacio = null;

        for (PuzzleSprite tmp : puzzleSprites) {
            if (tmp.isVisible() == false) {
                posicionhuecox = tmp.getX();
                posicionhuecoy = tmp.getY();
                vacio = tmp;
            }
        }

        if (posicionhuecox == -1 || posicionhuecoy == -1) {
            Log.d("DIRECCION", "no encontrado hueco");
        }

        int direccionmovimiento = -1;

        // Derecha vacio
        if (posicionx - 1 == posicionhuecox && posiciony == posicionhuecoy) direccionmovimiento = 1;
        // Arriba vacío
        if (posicionx == posicionhuecox && posiciony - 1 == posicionhuecoy) direccionmovimiento = 2;
        // Derecha vacío
        if (posicionx + 1 == posicionhuecox && posiciony == posicionhuecoy) direccionmovimiento = 3;
        // Abajo vacío
        if (posicionx == posicionhuecox && posiciony + 1 == posicionhuecoy) direccionmovimiento = 4;

        Log.d("DIRECCION", direccionmovimiento + "");

        switch (direccionmovimiento) {
            case 1:
                spritetocado.setX(posicionx - 1);
                vacio.setX(posicionx);
                vacio.setY(posiciony);
                break;
            case 2:
                spritetocado.setY(posiciony - 1);
                vacio.setX(posicionx);
                vacio.setY(posiciony);
                break;
            case 3:
                spritetocado.setX(posicionx + 1);
                vacio.setX(posicionx);
                vacio.setY(posiciony);
                break;
            case 4:
                spritetocado.setY(posiciony + 1);
                vacio.setX(posicionx);
                vacio.setY(posiciony);
                break;
            default:
                Log.d("DIRECCION", "-1");
                break;
        }
    }

    public void resuelto() {

        int x = 0;
        int y = 0;
        int cnd = 0;

        for (PuzzleSprite tmp : puzzleSprites) {
            //  Log.d("RESUELTO", "" + tmp.getX() + " - " + tmp.getY() + " / " + x + " - " + y);
            if (tmp.getX() == x && tmp.getY() == y) cnd++;
            x++;
            if (x % ancho == 0) {
                y++;
                x = 0;
            }
        }

        //   Log.d("RESUELTO", "" + cnd + " - " + ancho * alto);

        if (cnd == ancho * alto) {

            for (PuzzleSprite tmp : puzzleSprites) {
                if (tmp.isVisible() == false) tmp.setVisible(true);
            }

            salir();

        }


    }


    public void salir() {


        puzzleLoopThread.setRunning(false);
        puzzleLoopThread.interrupt();

        long resultado = crono - System.currentTimeMillis();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Resultado", resultado);
        returnIntent.putExtra("Juego", "Inteligencia");
        miActividad.setResult(Activity.RESULT_OK,returnIntent);
        miActividad.finish();

    }


    public void parar() {
        puzzleLoopThread.setRunning(false);
        puzzleLoopThread.interrupt();
    }

    public void setActividad(Activity actividad) {
        this.miActividad = actividad;
    }

}