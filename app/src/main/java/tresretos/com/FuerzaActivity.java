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

import tresretos.com.Minijuegos.FuerzaView;
import tresretos.com.Minijuegos.MagiaView;

public class FuerzaActivity extends AppCompatActivity {

    FuerzaView fuerzaView;
    Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuerza);



        final LinearLayout lfuerza = (LinearLayout)findViewById(R.id.pizarrafuerza);
        fuerzaView = new FuerzaView(contexto);
        lfuerza.addView(fuerzaView);

        fuerzaView.setMiActividad(this);

        Button abandonarbtn = (Button)findViewById(R.id.abandonarfuerzabtn);

        abandonarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuerzaView.parar();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        fuerzaView.parar();
    }
}
