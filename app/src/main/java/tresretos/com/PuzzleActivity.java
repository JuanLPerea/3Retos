package tresretos.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import tresretos.com.Minijuegos.PuzzleView;

public class PuzzleActivity extends AppCompatActivity {
    PuzzleView fondo;
    Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        final LinearLayout layout1 = (LinearLayout) findViewById(R.id.pizarra);
        fondo = new PuzzleView(this, 3, 3);   // al crear el view indicamos las dimensiones del puzzle, en este caso 3 x 3
        layout1.addView(fondo);

        Button abandonarbtn = (Button)findViewById(R.id.abandonarbtn);

        fondo.setActividad(this);

        abandonarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fondo.parar();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        fondo.parar();
    }


    public void terminar(){

    }


}
