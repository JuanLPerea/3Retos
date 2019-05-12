package tresretos.com.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.ByteBuffer;
import java.util.List;

import tresretos.com.Modelos.Jugador;
import tresretos.com.R;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.JugadoresViewHolder> {

    private Context mContext;
    List<Jugador> jugadores;

    // Referencias a Firebase
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    FirebaseUser user;
    private FirebaseAuth auth;


    // Create a storage reference from our app
    StorageReference storageRef;
    FirebaseStorage storage;


    public PlayersAdapter() {
    }

    @Override
    public JugadoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_principal, parent, false);
        JugadoresViewHolder holder = new JugadoresViewHolder(v);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tresretos-24a82.appspot.com");


        return  holder;
    }

    @Override
    public void onBindViewHolder(JugadoresViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(JugadoresViewHolder holder, int position) {

        final Jugador jugador = jugadores.get(position);

        holder.nombrejugador.setText(jugador.getNombre());
        holder.fuerza.setText(jugador.getFuerza() + "");
        holder.inteligencia.setText(jugador.getInteligencia() + "");
        holder.magia.setText(jugador.getMagia() + "");
        holder.mensaje.setText(jugador.getMensajes().get(jugador.getMensajes().size()-1));
        holder.victoriasTV.setText(jugador.getVictorias() + "");
        holder.derrotasTV.setText(jugador.getDerrotas() + "");


        Glide.with(mContext)
                .load(storageRef.child(jugador.getId()))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imagen);


        if (jugador.isConectado()){
            holder.online.setImageResource(R.drawable.online);
        }else{
            holder.online.setImageResource(R.drawable.offline);
        }

        if (jugador.isJugando()){
            holder.jugando.setImageResource(R.drawable.espada);
        } else {
            holder.jugando.setImageResource(R.drawable.mano);
        }

    }

    public PlayersAdapter(Context mContext, List<Jugador> players) {
        this.mContext = mContext;
        this.jugadores = players;
    }



    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    // Clase
    public static class JugadoresViewHolder extends RecyclerView.ViewHolder{

        ImageView imagen, online, jugando;
        TextView nombrejugador, fuerza, inteligencia, magia, mensaje, victoriasTV, derrotasTV;



        public JugadoresViewHolder(View itemView) {
            super(itemView);
            imagen =  (ImageView)itemView.findViewById(R.id.imagenjugador);
            online = (ImageView)itemView.findViewById(R.id.isonlineIV);
            nombrejugador = (TextView)itemView.findViewById(R.id.jugadortxt);
            fuerza = (TextView)itemView.findViewById(R.id.fuerzatxt);
            inteligencia = (TextView)itemView.findViewById(R.id.inteligenciatxt);
            magia = (TextView)itemView.findViewById(R.id.magiatxt);
            mensaje = (TextView)itemView.findViewById(R.id.mensajetxt);
            jugando = (ImageView)itemView.findViewById(R.id.jugandoIV);
            victoriasTV = (TextView)itemView.findViewById(R.id.listavictoriasTV);
            derrotasTV = (TextView)itemView.findViewById(R.id.listaderrotasTV);

        }
    }

}
