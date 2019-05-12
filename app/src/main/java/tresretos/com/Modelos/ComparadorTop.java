package tresretos.com.Modelos;

import android.util.Log;

import java.util.Comparator;

public class ComparadorTop implements Comparator<Jugador> {


    @Override
    public int compare(Jugador jugador1, Jugador jugador2) {



        int resultado = (jugador2.getVictorias() > jugador1.getVictorias()) ? 1 :-1;

        Log.d("ORDENAR", jugador1.getVictorias() + " " + jugador2.getVictorias() + " " + resultado + "");

        return resultado;
    }
}
