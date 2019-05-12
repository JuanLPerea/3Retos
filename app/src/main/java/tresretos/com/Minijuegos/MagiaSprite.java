package tresretos.com.Minijuegos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Created by aerocool on 24/07/17.
 */


public class MagiaSprite {

    // direction = 0 up, 1 left, 2 down, 3 right,

    private static final int MAX_SPEED = 5;
    private MagiaView magiaView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    private int valor;
    private boolean visible = true;
    private boolean movible = true;


    public MagiaSprite(MagiaView magiaView, Bitmap bmporiginal, int valor, int numero) {
        this.magiaView = magiaView;
        this.valor = valor;
        this.movible = true;

        if(numero != -1){
            this.bmp = Bitmap.createScaledBitmap(bmporiginal, magiaView.getWidth() / 5, magiaView.getWidth() / 5, true);
        } else {
            this.bmp = Bitmap.createScaledBitmap(bmporiginal, magiaView.getWidth() , magiaView.getHeight() , true);
        }


        // Random rnd = new Random();

        Log.d("SPRITE", "" + numero);

        int fila = 0;
        if (numero > 3){
            numero = numero - 4;
            fila = bmp.getHeight();
        }

        this.x = (bmp.getWidth() * numero) + (((magiaView.getWidth() - (bmp.getWidth() * 4)) /5)*(numero+1));
        this.y = 20 + fila;


        Log.d("POSICION", "" + x + " - " + y);

        xSpeed = 0;
        ySpeed = 0;
        width = bmp.getWidth();
        height = bmp.getHeight();
    }


    public void onDraw(Canvas canvas) {

        if (isVisible()) {
            int posicionx = (getX());
            int posiciony = (getY());

            //  Log.d("SPRITE", "" + x + " - " + y);

            Rect destinationRect = new Rect();
            destinationRect.set(0, 0, bmp.getWidth(), bmp.getHeight());
            destinationRect.offsetTo(posicionx, posiciony);

            canvas.drawBitmap(bmp, null, destinationRect, null);
        }
    }

    public boolean isCollition(float x2, float y2) {

        boolean colisiona = false;

        if (x2 > x && x2 < x + bmp.getWidth() - 5 && y2 > y && y2 < y + bmp.getHeight() && isMovible()){
            colisiona = true;
        }

        return colisiona;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void centrar(){
        this.bmp = Bitmap.createScaledBitmap(bmp, magiaView.getWidth() / 2, magiaView.getWidth() / 2, true);
        this.setX((magiaView.getWidth()/2) - (bmp.getWidth())/2);
        this.setY((magiaView.getHeight() - bmp.getHeight() ) - (magiaView.getHeight() / 8));
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isMovible() {
        return movible;
    }

    public void setMovible(boolean movible) {
        this.movible = movible;
    }
}
