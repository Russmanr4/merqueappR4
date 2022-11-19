package com.example.merqueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView mTextViewRegister;
    TextInputEditText mTextInputEditTextEmail;
    TextInputEditText mTextInputEditTextPassword;
    Button mButtonLogin;
    SignInButton mbtngoogle;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int Request_code_Google=1;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewRegister=findViewById(R.id.TextViewRegister);

        mTextInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        mTextInputEditTextPassword= findViewById(R.id.textInputEditTextPassword);
        mButtonLogin = findViewById(R.id.btnlogin);
        mbtngoogle = findViewById(R.id.btnloginSignInGoogle);

        mAuth= FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        mbtngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {login();}

        });

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,activity_register.class);
                startActivity(intent);
            }
        });


    }

    private void signInGoogle (){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Request_code_Google);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Request_code_Google){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //google autenticacion fue exitosa
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e){
                // google autenticacion fallo
                Log.w("ERROR", "Google sign in failed", e);

            }

        }


    }


    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String id= mAuth.getCurrentUser().getUid();
                            checkUserExist(id);

                        }else{
                            Log.w("error", "signInWithCredential:failure", task.getException());

                        }

                    }
                });


    }

    private void checkUserExist(final String id) {
        mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);

                }
                else{
                   String email = mAuth.getCurrentUser().getEmail();
                    Map<String, Object> map = new HashMap<>();
                    map.put("email", email);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                                startActivity(intent);


                            }else{
                                Toast.makeText(MainActivity.this, "No se puedo Almacenar el Usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


    private void login() {

        String email = mTextInputEditTextEmail.getText().toString();
        String password = mTextInputEditTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, "El email y contraseña no son Correctos", Toast.LENGTH_SHORT).show();

                }

            }
        });

        Log.d("campo", "email "  +  email);
        Log.d("campo" , "password "   +  password);

    }
}