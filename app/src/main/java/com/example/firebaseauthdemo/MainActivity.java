package com.example.firebaseauthdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText email;
    EditText password;
    Button register;
    EditText artistName;
    Spinner spinnerGenre;
    Button addButton;

    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
         email = (EditText) findViewById(R.id.username);
         password = (EditText) findViewById(R.id.password);
         register = (Button) findViewById(R.id.register);

         artistName = (EditText) findViewById(R.id.artistName);
         spinnerGenre = (Spinner) findViewById(R.id.spinnerGenre);
         addButton = (Button) findViewById(R.id.addButton);

         addButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               addArtist();
             }
         });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = email.getText().toString();
                String pwd = password.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(username,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

    private void addArtist() {

        String name = artistName.getText().toString();
        String genre = spinnerGenre.getSelectedItem().toString();
        
        if (name != "") {

            String id = databaseArtists.push().getKey();
            Artist artist = new Artist(id, name, genre);
            databaseArtists.child(id).setValue(artist);
            artistName.setText("");
         Toast.makeText(getApplicationContext(), "Artist Added", Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(getApplicationContext(),"Enter Artist Name", Toast.LENGTH_LONG).show();
        }
    }
}
