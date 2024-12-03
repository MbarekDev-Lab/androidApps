package com.plracticalcoding.photo_album_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {

    private ImageView addImageView;
    private EditText editTextAddTitle;
    private EditText editTextAddDesc;
    private Button buttonSave;

    ActivityResultLauncher<Intent> activityResultLauncherForSelectingImages;
    private Bitmap selectedImage;
    private Bitmap scledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Image");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addImageView = findViewById(R.id.imageviewaddImage);
        editTextAddTitle = findViewById(R.id.editTextTextAddTitle);
        editTextAddDesc = findViewById(R.id.editTextTextAddDesc);
        buttonSave = findViewById(R.id.buttonSave);

        // register activityResultLauncherForSelectingImages
        registerActivityForSelecterfdImage();
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String permition;

                if (Build.VERSION.SDK_INT >= 33) {
                    permition = Manifest.permission.READ_MEDIA_IMAGES;
                } else {
                    permition = Manifest.permission.READ_EXTERNAL_STORAGE;
                }
                if (ContextCompat.checkSelfPermission(AddImageActivity.this, permition) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddImageActivity.this, new String[]{permition}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncherForSelectingImages.launch(intent);
                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage == null) {
                    Toast.makeText(AddImageActivity.this, "Please select an Image!", Toast.LENGTH_SHORT).show();
                } else {
                    String title = editTextAddTitle.getText().toString();
                    String desc = editTextAddDesc.getText().toString();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scledImage = makeSmallImg(selectedImage, 300);

                    scledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                    byte[] image = outputStream.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra("title", title);
                    intent.putExtra("description", desc);
                    intent.putExtra("image", image);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                // selectedImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectingImages.launch(intent);
        }
    }

    private void registerActivityForSelecterfdImage() {
        activityResultLauncherForSelectingImages = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data != null) {
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                addImageView.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }

    public Bitmap makeSmallImg(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / height;

        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / height);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}