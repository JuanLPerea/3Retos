package tresretos.com.Minijuegos;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by aerocool on 24/07/17.
 */

public class FuerzaSprite {

    // direction = 0 up, 1 left, 2 down, 3 right,

    private static final int MAX_SPEED = 5;
    private FuerzaView fuerzaView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    private int valor;
    boolean tocado;


    public FuerzaSprite(FuerzaView fuerzaView, Bitmap bmporiginal, int valor) {
        this.fuerzaView = fuerzaView;
        this.valor = valor;


        Random rnd = new Random();

        switch (valor){
            case 0:
                this.bmp = Bitmap.createScaledBitmap(bmporiginal, fuerzaView.getWidth() , fuerzaView.getWidth() , true);
                x = 0;
                y=0;
                break;
            case 1:
                this.bmp = Bitmap.createScaledBitmap(bmporiginal, fuerzaView.getWidth() / 5, fuerzaView.getWidth() / 5, true);
                x = bmp.getWidth();
                y = bmp.getHeight();
                tocado = false;
                break;
            case 2:
                this.bmp = Bitmap.createScaledBitmap(bmporiginal, fuerzaView.getWidth() / 5, fuerzaView.getWidth() / 5, true);
                x = fuerzaView.getWidth() - (bmp.getWidth() * 2);
                y = bmp.getHeight();
                tocado = true;
                break;
            case 3:
                // Guerrero
                this.bmp = Bitmap.createScaledBitmap(bmporiginal, fuerzaView.getWidth() / 3, (int) (fuerzaView.getWidth() / 1.5), true);
                x = (fuerzaView.getWidth() /2) - (bmp.getWidth() / 2);
                y = fuerzaView.getHeight() - bmp.getHeight() - 100;
                break;
            case 4:
                // brazo
                this.bmp = Bitmap.createScaledBitmap(bmporiginal, fuerzaView.getWidth() / 3,(int) (fuerzaView.getWidth() / 1.5), true);
                x = (fuerzaView.getWidth() /2) - (bmp.getWidth() / 2);
                y = fuerzaView.getHeight() - bmp.getHeight() - 100;
                break;
            default:
                // brazo
                this.bmp = Bitmap.createScaledBitmap(bmporiginal, fuerzaView.getWidth() / 3, (int) (fuerzaView.getWidth() / 1.5), true);
                x = (fuerzaView.getWidth() /2) - (bmp.getWidth() / 2);
                y = fuerzaView.getHeight() - bmp.getHeight() - 100;

                break;



        }

        //  Log.d("POSICION", "" + x + " - " + y);

        xSpeed = 0;
        ySpeed = 0;
        width = bmp.getWidth();
        height = bmp.getHeight();
    }


    public void onDraw(Canvas canvas) {

        int posicionx = (getX());
        int posiciony = (getY());

        //  Log.d("SPRITE", "" + x + " - " + y);

        Rect destinationRect = new Rect();
        destinationRect.set(0, 0, bmp.getWidth(), bmp.getHeight());
        destinationRect.offsetTo(posicionx, posiciony);


        canvas.drawBitmap(bmp, null, destinationRect, null);



    }

    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + bmp.getWidth() - 5 && y2 > y && y2 < y + bmp.getHeight();
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
        this.bmp = Bitmap.createScaledBitmap(bmp, fuerzaView.getWidth() / 2, fuerzaView.getWidth() / 2, true);
        this.setX((fuerzaView.getWidth()/2) - (bmp.getWidth())/2);
        this.setY((fuerzaView.getHeight() - bmp.getHeight() )- (bmp.getHeight()/2));
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public boolean isTocado() {
        return tocado;
    }

    public void setTocado(boolean tocado) {
        this.tocado = tocado;
    }


}
