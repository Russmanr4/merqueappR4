package com.example.merqueapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.merqueapp.R;
import com.example.merqueapp.models.User;
import com.example.merqueapp.providers.AuthProviders;
import com.example.merqueapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {
    TextInputEditText mTextImputUsername;
    Button mButtonRegister;
    AuthProviders mAuthProviders;
    UsersProvider mUsersProviders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextImputUsername = findViewById(R.id.textInputEditTextUsernameC);
        mButtonRegister=findViewById(R.id.btnConfirmarC);

        mAuthProviders = new AuthProviders();
        mUsersProviders = new UsersProvider();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {
        String username=mTextImputUsername.getText().toString();
        if(!username.isEmpty()){
            updateUser(username);
        }else{
            Toast.makeText(this, "Para continuar inserta el nombre del usuario", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateUser( final String username) {
        String id=mAuthProviders.getUid();
        User user = new User();
        user.setUsername(username);
        user.setId(id);


        mUsersProviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{

                    Toast.makeText(CompleteProfileActivity.this, "No se almaceno el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}