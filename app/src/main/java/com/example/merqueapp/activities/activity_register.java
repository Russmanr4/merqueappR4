package com.example.merqueapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class activity_register extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    TextInputEditText mTextInputEditTextUsername;
    TextInputEditText mTextInputEditTextEmailR;
    TextInputEditText mTextInputEditTextPasswordR;
    TextInputEditText mTextInputEditTextConfirmPassword;
    Button mButtonRegister;
    AuthProviders mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mCircleImageViewBack=findViewById(R.id.circleimageback);
        mTextInputEditTextUsername= findViewById(R.id.textInputEditTextUsername);
        mTextInputEditTextEmailR= findViewById(R.id.textInputEditTextEmailR);
        mTextInputEditTextPasswordR= findViewById(R.id.textInputEditTextPasswordR);
        mTextInputEditTextConfirmPassword= findViewById(R.id.textInputEditTextConfirmPassword);
        mButtonRegister= findViewById(R.id.btnregister);

        mAuthProvider = new AuthProviders();
        mUsersProvider = new UsersProvider();

        mDialog = new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();



        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    private void register() {
        //metodo resgitro
        String username=mTextInputEditTextUsername.getText().toString();
        String email=mTextInputEditTextEmailR.getText().toString();
        String password=mTextInputEditTextPasswordR.getText().toString();
        String confirmpassword=mTextInputEditTextConfirmPassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
            if (isEmailValid(email)){
                if(password.equals(confirmpassword)){
                    if(password.length() >=6){
                        createUser(email,password,username);
                    }else {
                        Toast.makeText(this, "La Contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Has insertado todos los campos pero el correo no es valido", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(this, "para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void createUser(final String email, String password, final String username) {
        mDialog.show();
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id= mAuthProvider.getUid();
                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);

                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                          if (task.isSuccessful()){
                            Toast.makeText(activity_register.this, "El usuario se almaceno", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity_register.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }else{


                            Toast.makeText(activity_register.this, "El usuario No se almaceno", Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
                    Toast.makeText(activity_register.this, "El usuario se Registro Correctamente", Toast.LENGTH_SHORT).show();


                }else{
                    mDialog.dismiss();
                    Toast.makeText(activity_register.this, "No se puedo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
        }
        });

   }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

