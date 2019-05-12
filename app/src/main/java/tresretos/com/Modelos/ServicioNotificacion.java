package tresretos.com.Modelos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 *
 */

public class ServicioNotificacion extends Service {


    Context context;

    public ServicioNotificacion() {
        this.context = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.d("TAREANOTI", "Empieza el servicio");
        // Pasamos el contexto de nuestro servicio como parámetro (En el AsyncTask lo necesitamos para acceder a la BB.DD.
        TareaNotificacion notificacionTask = new TareaNotificacion(context);
        notificacionTask.execute();

        return START_STICKY;
    }




}
