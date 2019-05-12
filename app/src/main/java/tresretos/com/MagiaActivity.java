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

import tresretos.com.Minijuegos.MagiaView;

public class MagiaActivity extends AppCompatActivity {

    MagiaView magiaView;
    Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magia);


        final LinearLayout lmagia = (LinearLayout)findViewById(R.id.pizarramagia);
        magiaView = new MagiaView(contexto);
        lmagia.addView(magiaView);

        magiaView.setMiActividad(this);

        Button abandonarbtn = (Button)findViewById(R.id.abandonarmagiabtn);

        abandonarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                magiaView.parar();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        magiaView.parar();
    }
}
