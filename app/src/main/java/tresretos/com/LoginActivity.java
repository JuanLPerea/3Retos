package tresretos.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import tresretos.com.Modelos.Jugador;

public class LoginActivity extends AppCompatActivity {

    Context context = this;

    Button buttonRegister, buttonSignIn;
    EditText editTextEmail, editTextPass, editTextApodo;
    Bundle bundle;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    // Create a storage reference from our app
    StorageReference storageRef;
    FirebaseStorage storage;

    //Ads
  //  private AdView mAdView;

    // Video
    ImageView animacionFondo;

    // Shared preferences
    SharedPreferences prefs;
    String email;
    String password;
    String apodo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ad Mob
        //MobileAds.initialize(this, "ca-app-pub-4821530383095033/5883489875");

        buttonRegister = (Button) findViewById(R.id.registbtn);
        buttonSignIn = (Button) findViewById(R.id.loginbtn);
        editTextEmail = (EditText) findViewById(R.id.usuario);
        editTextPass = (EditText) findViewById(R.id.password);
        editTextApodo = (EditText) findViewById(R.id.apodo);

        prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("pass", "");
        apodo = prefs.getString("apodo", "");

        editTextEmail.setText(email);
        editTextPass.setText(password);
        editTextApodo.setText(apodo);

        animacionFondo = (ImageView) findViewById(R.id.animacion_fondo);
        animacionFondo.setBackgroundResource(R.drawable.animacion);
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) animacionFondo.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();



      //  mAdView = (AdView) findViewById(R.id.adViewInicial);
     //   AdRequest adRequest = new AdRequest.Builder().build();
     //   mAdView.loadAd(adRequest);


        database = FirebaseDatabase.getInstance();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tresretos-24a82.appspot.com");

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        bundle = new Bundle();

        // Pedir permisos para la App (Versiones Android 6 o superior)
        // checkPermission();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.i("SESION" , "sesion iniciada con mail " + user.getEmail() );
                    //  Toast.makeText(context, "Sesión iniciada con mail: " + user.getEmail(), Toast.LENGTH_LONG).show();
                    //  iniciar();
                }
                else {
                    Log.i("SESION" , "Sesion Cerrada");
                }
            }


        };

    }


    private void registrar(final String email, String pass){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    /*
                    // Cuando el usuario se registra, se crea una entrada nueva en Firebase con el id del usuario
                    Map<String, String> nuevoUsuario = new HashMap<String, String>();
                    nuevoUsuario.put(user.getUid(), "idUsuario");
                    database.getReference().child("utilcoche").setValue(nuevoUsuario);
                    */
                    bundle.putString(FirebaseAnalytics.Event.SIGN_UP, email );
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    // Añadimos el nuevo jugador a la base de datos
                    Jugador nuevojugador = new Jugador(editTextApodo.getText().toString(), user.getUid());
                    DatabaseReference nodoRaiz = database.getReference().getRoot().child(user.getUid());
                    nuevojugador.setId(user.getUid());
                    nodoRaiz.setValue(nuevojugador);

                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.user);
                    // bmp.createScaledBitmap(bmp, 128, 128, true);
                    subirImagen(bmp);

                    Toast.makeText(context, "Usuario Creado Correctamente", Toast.LENGTH_LONG).show();
                    iniciar();
                }
                else
                {
                    Log.e("SESION", task.getException().getMessage() + "");
                    Toast.makeText(context, "No se pudo crear el usuario, por favor revisar datos. " + task.getException().getMessage()  , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void iniciarSesion(final String email, String pass){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("SESION", "Sesion Iniciada");
                    bundle.putString(FirebaseAnalytics.Event.SIGN_UP, email );
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    iniciar();
                }
                else
                {
                    Log.e("SESION", task.getException().getMessage() + "");
                    Toast.makeText(context, "Usuario o contraseña incorrectos, por favor revisar datos. "  , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void loggear(View v){
        String emailInicio = editTextEmail.getText().toString();
        String passInicio = editTextPass.getText().toString();

        if (emailInicio.equals("")) emailInicio = "vacio@gmail.com";
        if (passInicio.equals("")) passInicio = "vacio";
        iniciarSesion(emailInicio, passInicio);

    }

    public void registrar(View v){
        String emailReg = editTextEmail.getText().toString();
        if(emailReg.equals("")) emailReg = " ";
        String passReg = editTextPass.getText().toString();
        if(passReg.equals("")) passReg = " ";
        registrar(emailReg, passReg);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);

        }
    }

    public void iniciar(){


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", editTextEmail.getText() + "");
        editor.putString("pass", editTextPass.getText() + "");
        editor.putString("apodo", editTextApodo.getText() + "");
        editor.commit();

        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
        // finish();
    }

    public void resetpassword(View v){

        String mail = editTextEmail.getText().toString().trim();

        if (!mail.equals("")) {
            auth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Hemos enviado un correo con instrucciones para recuperar tu contraseña!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error al enviar el correo!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }


    private boolean checkPermission() {

        boolean permiso = false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            // Toast.makeText(this, "Esta versión de Android no es 6 o superior" + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();

        } else {

            int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.CAMERA);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {android.Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);

                Toast.makeText(this, "Solicitando permisos", Toast.LENGTH_LONG).show();
                permiso = true;

            }else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Los Permisos ya estaban concedidos ", Toast.LENGTH_LONG).show();
                permiso = true;

            }

        }

        return permiso;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos concedidos !", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "No hay permiso para utilizar la cámara !", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void subirImagen(Bitmap bitmap){
        // Get the data from an ImageView as bytes
        // imageView.setDrawingCacheEnabled(true);
        //imageView.buildDrawingCache();
        //Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.child(user.getUid()).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
}
