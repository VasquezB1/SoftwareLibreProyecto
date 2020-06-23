package com.example.softwarelibre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

public class VentanaRegistro extends AppCompatActivity {

    private EditText nombre;
    private EditText correo;
    private EditText contra;

   // private EditText direccion;
    //private EditText fecha;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;



    private Button botonregistro;

    //Variables a Ingresar
    private String nombreregi="";
    private String correoelec="";
    private String contraseña = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registro);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        nombre = findViewById(R.id.nombreRegistro);
        correo = findViewById(R.id.correoRegistro);
        contra = findViewById(R.id.contraRegistro);
        botonregistro = (Button) findViewById(R.id.button3);

        botonregistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                correoelec = correo.getText().toString();
                contraseña = contra.getText().toString();
                nombreregi = nombre.getText().toString();

                if(correoelec.isEmpty() && contraseña.isEmpty()){

                    if(contraseña.length()<6){
                        registerUser();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No se pudo", Toast.LENGTH_LONG).show();
                    }
                }else{
                    registerUser();
                }

            }
        });
    }



    public void registerUser(){
        mAuth.createUserWithEmailAndPassword(correoelec, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String,Object> map = new HashMap<>();
                            map.put("nombre",nombreregi);
                            map.put("usuario",correoelec);
                            map.put("contraseña",contraseña);
                            String id = mAuth.getCurrentUser().getUid();
                            databaseReference.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Se registro", Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Registro Fallido en Servidor", Toast.LENGTH_LONG).show();


                                    }
                                }

                            });

                        } else {

                        }


                    }
                });
    }




    //Metodo Boton Anterior

    public void Regresar(View View){
        Intent regresar = new Intent(this, MainActivity.class);
        startActivity(regresar);


    }
}

