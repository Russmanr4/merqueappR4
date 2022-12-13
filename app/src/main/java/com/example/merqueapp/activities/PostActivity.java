package com.example.merqueapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merqueapp.R;
import com.example.merqueapp.models.Post;
import com.example.merqueapp.providers.AuthProviders;
import com.example.merqueapp.providers.ImageProvider;
import com.example.merqueapp.providers.PostProvider;
import com.example.merqueapp.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File getmImageFile2;
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
    PostProvider mPostProvider;
    String mProducto = "";
    String mDescription = "";
    AuthProviders mAuthProviders;
    AlertDialog mDialog;
    CircleImageView mCircleImageView2;
    AlertDialog.Builder mBuilderSelector;
    CharSequence options [];

    String mAbsolutePhotePatch;
    String mPhotoPatch;
    File mPhotoFile;

    private final int Gallery_REQUEST_CODE =1;
    private final int Gallery_REQUEST_CODE_2 =2;
    private final int Photo_REQUEST_CODE =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2 = findViewById(R.id.imageViewPost2);

        mCircleImageView2 = findViewById(R.id.circleimageback2);
        mButtonPost = findViewById(R.id.btnPost);
        mTextInputProducto = findViewById(R.id.textInputProducto);
        mTextInputDescription = findViewById(R.id.textInputDescripcion);
        mImageViewLicores = findViewById(R.id.imageViewLicores);
        mImageViewViveres = findViewById(R.id.imageViewViveres);
        mImageViewPapeleria = findViewById(R.id.imageViewPapeleria);
        mImageViewAseo = findViewById(R.id.imageViewAseo);
        mTextViewCategory = findViewById(R.id.textViewCardCategory);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProviders = new AuthProviders();



        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento....")
                .setCancelable(false).build();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Seleccione una Opción");
        options = new CharSequence[]{"Imagen Galeria" , "Tomar Fotografía"};

        mCircleImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


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
                selectOptionImage(Gallery_REQUEST_CODE);
                //openGallery(Gallery_REQUEST_CODE);
            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(Gallery_REQUEST_CODE_2);
                //openGallery(Gallery_REQUEST_CODE_2);
            }
        });

        mImageViewLicores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Licores";
                mTextViewCategory.setText(mCategory);
            }
        });


        mImageViewViveres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Viveres";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewPapeleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Papeleria";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewAseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Aseo";
                mTextViewCategory.setText(mCategory);
            }
        });

    }

    private void selectOptionImage(int requestcode) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    openGallery(requestcode);

                }else if(which == 1){
                    takePhoto();
                }
            }

        });
        mBuilderSelector.show();

    }

    private void takePhoto(){
        //Toast.makeText(this, "Selecciono Tomar Foto", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) !=null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile();

            }catch (Exception e){
                Toast.makeText(this, "Hubo un error con el Archivo" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.example.merqueapp", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, Photo_REQUEST_CODE);
            }

        }

    }

    private File createPhotoFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_Photo",
                ".jpg",
                storageDir
        );
        mPhotoPatch = "File: " + photoFile.getAbsolutePath();
        mAbsolutePhotePatch = photoFile.getAbsolutePath();
        return photoFile;
    }

    private void clickPost() {
        mProducto = mTextInputProducto.getText().toString();
        mDescription = mTextInputDescription.getText().toString();
        if (!mProducto.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            if (mImageFile !=null){
                saveImage(mImageFile , getmImageFile2);
            }else{
                Toast.makeText(this, "Selecciona una Imagen", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Completa los Campos para publicar", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveImage(File mImageFile, File getmImageFile2) {
        mDialog.show();
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            mImageProvider.save(PostActivity.this, getmImageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                         if (taskImage2.isSuccessful()){
                                             mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                 @Override
                                                 public void onSuccess(Uri uri2) {
                                                    String url2 = uri2.toString();
                                                     Post post = new Post();
                                                     post.setImage1(url);
                                                     post.setImage2(url2);
                                                     post.setNombreP(mProducto);
                                                     post.setDescription(mDescription);
                                                     post.setCategory(mCategory);
                                                     post.setIdUser(mAuthProviders.getUid());

                                                     mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> tasksave) {
                                                             mDialog.dismiss();
                                                             if (tasksave.isSuccessful()){
                                                                 clearForm();
                                                                 Toast.makeText(PostActivity.this, "La información se almaceno Correctamente", Toast.LENGTH_SHORT).show();
                                                             }else{
                                                                 Toast.makeText(PostActivity.this, "No se almaceno la informsción", Toast.LENGTH_SHORT).show();
                                                             }
                                                         }
                                                     });

                                                 }
                                             });
                                         }else{
                                             mDialog.dismiss();
                                             Toast.makeText(PostActivity.this, "Error al almacenar imagen 2", Toast.LENGTH_SHORT).show();

                                         }
                                }
                            });


                        }
                    });
                    Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void clearForm() {
        mTextInputDescription.setText("");
        mTextInputProducto.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.subirfoto);
        mImageViewPost2.setImageResource(R.drawable.subirfoto);
        mProducto= "";
        mDescription = "";
        mCategory = "";
        mImageViewPost1 = null;
        mImageViewPost2 = null;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode); //numero entero que nos va ejecutar un */

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_REQUEST_CODE && resultCode == RESULT_OK){
            try{
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e){
                Log.d("ERROR", "Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == Gallery_REQUEST_CODE_2 && resultCode == RESULT_OK){
            try{
                getmImageFile2 = FileUtil.from(this, data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(getmImageFile2.getAbsolutePath()));
            } catch (Exception e){
                Log.d("ERROR", "Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == Photo_REQUEST_CODE && resultCode == RESULT_OK){
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotePatch);
            Picasso.with(PostActivity.this).load(mPhotoPatch).into(mImageViewPost1);
        }


    }
}