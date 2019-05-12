package tresretos.com.Modelos;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tresretos.com.R;

/**
 * Created by aerocool on 28/07/17.
 */

public class TareaNotificacion extends AsyncTask<Void, Integer, Void> {


    Context context;
    NotificationCompat.Builder mBuilder;
    String noti;
    String publicado;
    int id;

    Jugador jugadorActivo;

    // Referencias a Firebase
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference userRef;
    FirebaseAuth.AuthStateListener mAuthListener;


    public TareaNotificacion(Context context) {
        this.context = context;

        // aqui obtenemos la referencia a Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        userRef = database.getReference().child(user.getUid());

        Log.d("TAREANOTI", "Usuario " + user.getUid());


    }

    @Override
    protected Void doInBackground(Void... voids) {

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (user != null){
                    Log.i("SESION" , "sesion iniciada con mail " + user.getEmail() );

                }
                else {
                    Log.i("SESION" , "Sesion Cerrada");
                }
            }


        };

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot3) {
                jugadorActivo = dataSnapshot3.getValue(Jugador.class);

                Log.d("TAREANOTI", "Listener Servicio");

                // si tenemos alguna notificación la mostramos
                if (!jugadorActivo.getNotificacion().equals("")) {


                    Log.d("TAREANOTI", "Hay una Notificacion");

                    // Creamos la notificacion
                    mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notificacion)
                            .setContentTitle("S.I.M.")
                            .setContentText(jugadorActivo.getNotificacion());

                    //Lanzamos la notificacion
                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    int mId = 0;
                    mNotificationManager.notify(mId, mBuilder.build());


                    // esperar 3 segundos
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        Log.d("DATOS", "Error en el Hilo");
                    }

                    // eliminamos la notificacion
                    jugadorActivo.setNotificacion("");
                    userRef.setValue(jugadorActivo);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return null;
    }

}