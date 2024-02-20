package com.ganpat.smartagriculture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.ganpat.smartagriculture.database.DatabaseManagerUser;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maycol Meza on 15/04/2017.
 */

public class UserRegistro extends AppCompatActivity {

    private TextView loginLink;
    private EditText password;
    private EditText name;
    private EditText email;
    private Button registrar;
    //private DatabaseManagerUser managerUsuario;
    private String sPassword, sName, sEmail;
    private int request_code = 1;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);
        auth = FirebaseAuth.getInstance();

        loginLink = (TextView)findViewById(R.id.link_login);
        email = (EditText)findViewById(R.id.edtemail);
        password = (EditText)findViewById(R.id.edtpassword);
        name = (EditText)findViewById(R.id.edtname);
        registrar = (Button)findViewById(R.id.btnregister);
       // bitmap_foto = BitmapFactory.decodeResource(getResources(),R.drawable.imagen);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void registrar(){

        if (!validar()) return;

        sEmail = email.getText().toString();
        sPassword = password.getText().toString();
        sName = name.getText().toString();


         //       progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(UserRegistro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Toast.makeText(UserRegistro.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(UserRegistro.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            String user_id = auth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            Map newPost = new HashMap();
                            newPost.put("name",sName);
                            newPost.put("email",sEmail);

                            current_user_db.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                   //progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserRegistro.this, "Successfully Registered.!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(UserRegistro.this, "Error..!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            startActivity(new Intent(UserRegistro.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private boolean validar() {
        boolean valid = true;

        String sName = name.getText().toString();
        String sPassword = password.getText().toString();
        String sEmail = email.getText().toString();

        if (sName.isEmpty() || sName.length() < 3) {
            name.setError("Enter at least 3 characters.");
            valid = false;
        } else {
            name.setError(null);
        }

        if (sEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("Invalid email address.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (sPassword.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("Enter between 4 to 10 alphanumeric characters.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        progressBar.setVisibility(View.GONE);
//    }
}


