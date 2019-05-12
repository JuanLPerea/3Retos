package tresretos.com.Modelos;

import java.util.Comparator;

/**
 * Created by aerocool on 13/08/17.
 */

public class ComparadorActualizado implements Comparator<Jugador> {
    @Override
    public int compare(Jugador jugador1, Jugador jugador2) {

        int resultado = 0;

        resultado = (jugador2.getActualizado().compareTo(jugador1.getActualizado())) ;


        return resultado;
    }
}
