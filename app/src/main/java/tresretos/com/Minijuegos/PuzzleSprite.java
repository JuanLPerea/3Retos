package tresretos.com.Minijuegos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by aerocool on 24/07/17.
 */

public class PuzzleSprite {
    // direction = 0 up, 1 left, 2 down, 3 right,

    private int id;
    private static final int MAX_SPEED = 5;
    private PuzzleView puzzleView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    boolean visible;


    public PuzzleSprite(PuzzleView puzzleView, Bitmap bmporiginal, int dx, int dy, int ancho) {
        this.puzzleView = puzzleView;
        this.visible = true;

        /*
         createBitmap(Bitmap source, int x, int y, int width, int height)
         Returns an immutable bitmap from the specified subset of the source bitmap.
         */


        Bitmap bmpredim;
        bmpredim = Bitmap.createScaledBitmap(bmporiginal, puzzleView.getWidth(), puzzleView.getWidth(), true);
        bmporiginal = bmpredim;

        //  bmpredim = Bitmap.createBitmap(bmporiginal,  bmporiginal.getHeight()- gameView.getHeight() ,  bmporiginal.getHeight() - gameView.getWidth()  , gameView.getHeight(), gameView.getWidth());


        int anchorecorte = (bmporiginal.getWidth() / ancho) - 5;
        int altorecorte = (bmporiginal.getHeight() / ancho) - 5;

        this.width = anchorecorte;
        this.height = altorecorte;

        int puntox = (dx * anchorecorte);
        int puntoy = (dy * altorecorte);

        Log.d("SALIDA: ", "" + puntox + " " + puntoy + " " + anchorecorte + " " + altorecorte);

        this.bmp = Bitmap.createBitmap(bmporiginal, puntox, puntoy, anchorecorte, altorecorte);

        x = dx;
        y = dy;
        xSpeed = 0;
        ySpeed = 0;
    }

    private void update() {
        if (x >= puzzleView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        xSpeed = 0;
        if (y >= puzzleView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        ySpeed = 0;

    }


    public void onDraw(Canvas canvas) {
        update();

        if (isVisible()) {
            int posicionx = (x * bmp.getWidth()) + 5;
            int posiciony = (y * bmp.getHeight()) + 5;

            Rect destinationRect = new Rect();
            destinationRect.set(5, 5, bmp.getWidth() - 5, bmp.getHeight() - 5);
            destinationRect.offsetTo(posicionx, posiciony);

            canvas.drawBitmap(bmp, null, destinationRect, null);
        }
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > (x * bmp.getWidth()) + 5 && x2 < (x * bmp.getWidth()) + 5 + bmp.getWidth() - 5 && y2 > (y * bmp.getHeight()) + 5 && y2 < (y * bmp.getHeight()) + 5 + bmp.getHeight() - 5;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}