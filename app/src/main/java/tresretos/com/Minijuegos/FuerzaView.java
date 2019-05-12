package tresretos.com.Minijuegos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import tresretos.com.R;


/**
 * Created by aerocool on 24/07/17.
 */

public class FuerzaView extends SurfaceView {

    private FuerzaLoopThread fuerzaLoopThread;
    private List<FuerzaSprite> fuerzaSprites = new ArrayList<>();
    private FuerzaSprite[] brazo;
    private int animacionbrazo = 0;
    private long lastClick;
    private long lastTouch;
    private Bitmap foto;
    Context contexto;
    long crono = 20000;
    boolean resuelto = false;
    int puntos = 0;
    Activity miActividad;
    int botontocado = 0;
    int fuerza  = 0;


    public FuerzaView(Context context) {
        super(context);
        this.contexto = context;
        this.brazo = new FuerzaSprite[4];

        fuerzaLoopThread = new FuerzaLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                fuerzaLoopThread.setRunning(false);

                while (retry) {
                    try {
                        fuerzaLoopThread.join();
                        retry = false;

                        parar();
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                fuerzaLoopThread.start();
                fuerzaLoopThread.setRunning(true);
                crono = crono + System.currentTimeMillis();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }


        });
    }


    public void createSprites(){
        fuerzaSprites.add(createSprite(R.drawable.bosque,0));
        fuerzaSprites.add(createSprite(R.drawable.botonrojo, 1));
        fuerzaSprites.add(createSprite(R.drawable.botonverde, 2));
        fuerzaSprites.add(createSprite(R.drawable.warrior2, 3));

        brazo[0] = createSprite(R.drawable.arm, 4);
        brazo[1] = createSprite(R.drawable.arm2, 5);
        brazo[2] = createSprite(R.drawable.arm3, 6);
        brazo[3] = createSprite(R.drawable.arm4, 7);

    }


    private FuerzaSprite createSprite(int resource, int valor){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new FuerzaSprite(this, bmp, valor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fondo negro
        canvas.drawColor(Color.BLACK);
        // Fondo
        fuerzaSprites.get(0).onDraw(canvas);

        // girar brazo

        Paint pintar = new Paint();
        pintar.setColor(Color.WHITE);
        pintar.setTextSize(82);
        pintar.setAntiAlias(true);
        pintar.setTextAlign(Paint.Align.CENTER);

        long segundos = (crono  - System.currentTimeMillis()) / 1000;


        if (segundos <= 0) {
            finjuego();
        }


        long decimas = (crono - System.currentTimeMillis() ) % 1000;
        String decimastxt = decimas + "  ";


        Paint pintarFuerza = new Paint();
        pintarFuerza.setColor(Color.RED);

        if  (System.currentTimeMillis() - lastTouch >199){
            fuerza--;
            if (fuerza<1) fuerza = 1;
        }

        // tamaño proporcional a la pantalla
        float anchoscr = fuerzaSprites.get(0).getWidth();
        float altoscr = fuerzaSprites.get(0).getHeight();
        int margen = 10;

        pintarFuerza.setStyle(Paint.Style.STROKE);
        pintarFuerza.setStrokeWidth(5);
        canvas.drawRect(anchoscr / 12, altoscr / 12, anchoscr - (anchoscr / 12) + 5, (altoscr / 6), pintarFuerza);

        pintarFuerza.setStyle(Paint.Style.FILL_AND_STROKE);
        pintarFuerza.setColor(Color.BLUE);
        canvas.drawRect((anchoscr / 12) + margen, altoscr / 12 + margen, (anchoscr / 12) + (anchoscr / 120 * fuerza), (altoscr / 6) - margen, pintarFuerza);


        canvas.drawText(segundos + ":" + decimastxt.charAt(0) , canvas.getWidth() / 2, canvas.getHeight() -10 , pintar);


        // Boton 1
        fuerzaSprites.get(1).onDraw(canvas);
        // Boton 2
        fuerzaSprites.get(2).onDraw(canvas);
        // Guerrero
        fuerzaSprites.get(3).onDraw(canvas);

        if (fuerza % 5 == 0){
            animacionbrazo++;
            if (animacionbrazo==4) animacionbrazo=0;
        }

        brazo[animacionbrazo].onDraw(canvas);


        // Log.d("TIEMPO", System.currentTimeMillis() - crono +  "");

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 10) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = fuerzaSprites.size() - 1; i >= 0; i--) {
                    FuerzaSprite fuerzaSprite = fuerzaSprites.get(i);

                    if (fuerzaSprite.isCollition(x, y)) {

                        if (fuerzaSprite.isTocado()){


                            // Cambiar los botones de posicion
                            int tmpx = fuerzaSprites.get(1).getX();
                            fuerzaSprites.get(1).setX(fuerzaSprites.get(2).getX());
                            fuerzaSprites.get(2).setX(tmpx);

                            if (System.currentTimeMillis() - lastTouch < 100){
                                fuerza++;
                            }
                            if (System.currentTimeMillis() - lastTouch < 30){
                                fuerza++;
                            }
                            if (System.currentTimeMillis() - lastTouch < 15){
                                fuerza++;
                            }


                            if (fuerza > 100) fuerza = 100;

                            lastTouch = System.currentTimeMillis();

                        }

                        Log.d("BOTON", "tocado: " + fuerza);



                    }
                }
            }
        }
        return true;
    }


    public void parar(){

    }


    private void finjuego() {

        fuerzaLoopThread.setRunning(false);
        fuerzaLoopThread.interrupt();

        // aquí calcular los puntos del juego
        long puntos = ((fuerza/2) * 700) + 1;

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Resultado", puntos);
        returnIntent.putExtra("Juego", "Fuerza");
        miActividad.setResult(Activity.RESULT_OK,returnIntent);
        miActividad.finish();

    }


    public void setMiActividad(Activity miActividad) {
        this.miActividad = miActividad;
    }
}
