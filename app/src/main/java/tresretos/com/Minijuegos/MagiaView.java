package tresretos.com.Minijuegos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
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


public class MagiaView extends SurfaceView {

    Context contexto;
    private MagiaLoopThread magiaLoopThread;
    private List<MagiaSprite> magiaSprites = new ArrayList<MagiaSprite>();
    private MagiaSprite fondo;
    private MagiaSprite caldero[];
    private long lastClick;
    long crono = 15000;
    boolean tocado = false;
    int spriteactivo;
    int poderhechizo = 0;
    Activity miActividad;
    int animacion = 0;
    int veloanim = 0;
    int num_objetos = 23;
    String pocion = "";


    public MagiaView(Context context) {
        super(context);
        this.contexto = context;

        magiaLoopThread = new MagiaLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                magiaLoopThread.setRunning(false);

                while (retry) {
                    try {
                        magiaLoopThread.join();
                        retry = false;
                        parar();
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                magiaLoopThread.start();
                magiaLoopThread.setRunning(true);
                crono = crono + System.currentTimeMillis();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    private void createSprites() {

        // preparar los calderos
        caldero = new MagiaSprite[4];
        caldero[0] = createSprite(R.drawable.caldero, 0, 0);
        caldero[1] = createSprite(R.drawable.caldero2, 0, 0);
        caldero[2] = createSprite(R.drawable.caldero3, 0, 0);
        caldero[3] = createSprite(R.drawable.caldero4, 0, 0);
        for (int cnd = 0; cnd < 4; cnd++) {
            caldero[cnd].centrar();
            caldero[cnd].setMovible(false);
        }
        // preparar el fondo
        fondo = createSprite(R.drawable.fondomagia, 0, -1);
        fondo.setMovible(false);
        fondo.setX(0);
        fondo.setY(0);

        Random rnd = new Random();

        List<Integer> objetos = new ArrayList<>();
        List<Integer> objetos_desordenados = new ArrayList<>();

        for (int numobj = 0 ; numobj < num_objetos ; numobj++){
            objetos.add(numobj);
        }
        for (int numobj = 0 ; numobj < num_objetos ; numobj++){
            int numero_azar = rnd.nextInt(objetos.size());
            objetos_desordenados.add(objetos.get(numero_azar));
            objetos.remove(numero_azar);
        }


        for (int nmr = 0; nmr < 8; nmr++) {

            int seleccion = rnd.nextInt(objetos_desordenados.size());


            switch (objetos_desordenados.get(seleccion)) {
                case 0:
                    magiaSprites.add(createSprite(R.drawable.hacha, 4, nmr));
                    break;
                case 1:
                    magiaSprites.add(createSprite(R.drawable.azucar, -1, nmr));
                    break;
                case 2:
                    magiaSprites.add(createSprite(R.drawable.hueso, 6, nmr));
                    break;
                case 3:
                    magiaSprites.add(createSprite(R.drawable.huevo, -2, nmr));
                    break;
                case 4:
                    magiaSprites.add(createSprite(R.drawable.lagartija, 7, nmr));
                    break;
                case 5:
                    magiaSprites.add(createSprite(R.drawable.ojos, 8, nmr));
                    break;
                case 6:
                    magiaSprites.add(createSprite(R.drawable.pelo, 8, nmr));
                    break;
                case 7:
                    magiaSprites.add(createSprite(R.drawable.rosa, -4, nmr));
                    break;
                case 8:
                    magiaSprites.add(createSprite(R.drawable.serpiente, 10, nmr));
                    break;
                case 9:
                    magiaSprites.add(createSprite(R.drawable.spider, 6, nmr));
                    break;
                case 10:
                    magiaSprites.add(createSprite(R.drawable.tomate, -3, nmr));
                    break;
                case 11:
                    magiaSprites.add(createSprite(R.drawable.veneno, 11, nmr));
                    break;
                case 12:
                    magiaSprites.add(createSprite(R.drawable.amuleto, -3, nmr));
                    break;
                case 13:
                    magiaSprites.add(createSprite(R.drawable.calavera, 15, nmr));
                    break;
                case 14:
                    magiaSprites.add(createSprite(R.drawable.diamante, -5, nmr));
                    break;
                case 15:
                    magiaSprites.add(createSprite(R.drawable.dinamita, 6, nmr));
                    break;
                case 16:
                    magiaSprites.add(createSprite(R.drawable.filete, -3, nmr));
                    break;
                case 17:
                    magiaSprites.add(createSprite(R.drawable.hiedra, 9, nmr));
                    break;
                case 18:
                    magiaSprites.add(createSprite(R.drawable.hoja, -4, nmr));
                    break;
                case 19:
                    magiaSprites.add(createSprite(R.drawable.manzana, -5, nmr));
                    break;
                case 20:
                    magiaSprites.add(createSprite(R.drawable.murcielago, 17, nmr));
                    break;
                case 21:
                    magiaSprites.add(createSprite(R.drawable.pluma, 8, nmr));
                    break;
                case 22:
                    magiaSprites.add(createSprite(R.drawable.raspa, 10, nmr));
                    break;
                case 23:
                    magiaSprites.add(createSprite(R.drawable.vudu, 20, nmr));
                    break;


            }
            objetos_desordenados.remove(seleccion);
        }
        magiaSprites.add(createSprite(R.drawable.puzzle, 0, 99));
    }


    private MagiaSprite createSprite(int resource, int valor, int numero) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new MagiaSprite(this, bmp, valor, numero);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        fondo.onDraw(canvas);

        // animación del caldero
        if (veloanim % 5 == 0) {
            animacion++;
            if (animacion == 4) animacion = 0;
        }
        caldero[animacion].onDraw(canvas);
        veloanim++;

        // dibujar los objetos en pantalla
        for (int cnd = 0; cnd < magiaSprites.size(); cnd++) {
            magiaSprites.get(cnd).onDraw(canvas);
        }

        // pintar el cronometro
        Paint pintar = new Paint();
        pintar.setColor(Color.WHITE);
        pintar.setTextSize(82);
        pintar.setAntiAlias(true);
        pintar.setTextAlign(Paint.Align.CENTER);


        long segundos = (crono - System.currentTimeMillis()) / 1000;


        if (segundos <= 0) {

            finjuego();
        }


        long decimas = (crono - System.currentTimeMillis()) % 1000;
        String decimastxt = decimas + "  ";

        canvas.drawText(segundos + ":" + decimastxt.charAt(0), canvas.getWidth() / 2, canvas.getHeight() - pintar.getTextSize() - 10, pintar);


    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {

                MagiaSprite magiaSprite = null;

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        for (int i = magiaSprites.size() - 1; i >= 0; i--) {
                            magiaSprite = magiaSprites.get(i);
                            if (magiaSprite.isCollition(x, y)) {
                                Log.d("EVENTO", "DOWN " + i);
                                spriteactivo = i;
                                tocado = true;
                                return true;
                            }
                        }
                        return true;

                    case (MotionEvent.ACTION_MOVE):
                        Log.d("EVENTO", "ARRASTRAR ");
                        if (magiaSprites.get(spriteactivo) != null && tocado) {
                            int tmpx = (int) event.getX() - (magiaSprites.get(spriteactivo).getWidth() / 2);
                            int tmpy = (int) event.getY() - (magiaSprites.get(spriteactivo).getHeight() / 2);
                            //   Log.d("ARRASTRAR", "" + tmpx + " - " + tmpy);

                            magiaSprites.get(spriteactivo).setX(tmpx);
                            magiaSprites.get(spriteactivo).setY(tmpy);


                            // si está dentro de las coordenadas del caldero, esque lo hemos echado dentro
                            if (magiaSprites.get(spriteactivo).getY() > this.getHeight() / 2) {
                                poderhechizo += magiaSprites.get(spriteactivo).getValor();
                                pocion = pocion + magiaSprites.get(spriteactivo).getValor();
                                magiaSprites.remove(spriteactivo);
                                tocado = false;
                            }

                        }
                        return true;

                    case (MotionEvent.ACTION_UP):
                        Log.d("EVENTO", "DENTRO ");
                        return false;

                    case (MotionEvent.ACTION_CANCEL):
                        Log.d("EVENTO", "CANCEL " + action);
                        return false;
                    case (MotionEvent.ACTION_OUTSIDE):
                        Log.d("EVENTO", "FUERA " + action);
                        return false;
                    default:
                        Log.d("EVENTO", "OTRO " + action);
                        return false;

                }
            }
        }
        return true;
    }


    public void parar() {
        magiaLoopThread.setRunning(false);
        magiaLoopThread.interrupt();
    }

    public void setMiActividad(Activity actividad) {
        this.miActividad = actividad;
    }

    private void finjuego() {


        magiaLoopThread.setRunning(false);
        magiaLoopThread.interrupt();

        // aquí calcular los puntos de la magia
        long puntos = (poderhechizo * 1000) + 1;

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Resultado", puntos);
        returnIntent.putExtra("Juego", "Magia");
        miActividad.setResult(Activity.RESULT_OK, returnIntent);
        miActividad.finish();

    }

}

