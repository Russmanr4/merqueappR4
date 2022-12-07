package com.example.merqueapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merqueapp.R;
import com.example.merqueapp.providers.ImageProvider;
import com.example.merqueapp.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.annotation.Nullable;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    Button mButtonPost;
    ImageProvider mImageProvider;
    TextInputEditText mTextInputProducto;
    TextInputEditText mTextInputDescription;
    ImageView mImageViewLicores;
    ImageView mImageViewViveres;
    ImageView mImageViewPapeleria;
    ImageView mImageViewAseo;
    TextView mTextViewCategory;
    String mCategory="";

    private final int Gallery_REQUEST_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageProvider = new ImageProvider();

        mButtonPost = findViewById(R.id.btnPost);
        mTextInputProducto = findViewById(R.id.textInputProducto);
        mTextInputDescription = findViewById(R.id.textInputDescripcion);
        mImageViewLicores = findViewById(R.id.imageViewLicores);
        mImageViewViveres = findViewById(R.id.imageViewViveres);
        mImageViewPapeleria = findViewById(R.id.imageViewPapeleria);
        mImageViewAseo = findViewById(R.id.imageViewAseo);
        mTextViewCategory = findViewById(R.id.textViewCardCategory);
        
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveImage(); antes
                clickPost();
            }
        });
        
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mImageViewLicores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Licores";
            }
        });


        mImageViewViveres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Viveres";
            }
        });

        mImageViewPapeleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Papeleria";
            }
        });

        mImageViewAseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Aseo";
            }
        });

    }

    private void clickPost() {
        String Producto = mTextInputProducto.getText().toString();
        String Descripcion = mTextInputDescription.getText().toString();

    }

    private void saveImage() {
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PostActivity.this, "Error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_REQUEST_CODE); //numero entero que nos va ejecutar un */

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_REQUEST_CODE && resultCode == RESULT_OK){
            try{
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e){
                Log.d("Error", "Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}