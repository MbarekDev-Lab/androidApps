package com.plracticalcoding.photo_album_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateImageActivity extends AppCompatActivity {

    private ImageView imageViewUpdate;
    private EditText editTextUpdateTitle;
    private EditText editTextUpdateDesc;
    private Button buttonUpdate;

    private String title, description;
    private int id;
    private byte[] image;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectingImages;
    private Bitmap selectedImage;
    private Bitmap scledImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Image");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageViewUpdate = findViewById(R.id.imageviewUpdateImage);
        editTextUpdateTitle = findViewById(R.id.editTextTextUpdateTitle);
        editTextUpdateDesc = findViewById(R.id.editTextTextUpdateDesc);
        buttonUpdate = findViewById(R.id.buttonUpdate);


        registerActivityForSelecterfdImage();

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        image = getIntent().getByteArrayExtra("image");

        editTextUpdateTitle.setText(title);
        editTextUpdateTitle.setText(description);
        imageViewUpdate.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        /*0:00 Red Dead Redemption 2
        1:30 The Witcher 3: Wild Hunt
        2:45 Elden Ring
        4:00 God of War (2018)
        5:10 Sekiro: Shadows Die Twice
        6:30 Cyberpunk 2077
        7:45 Baldur’s Gate 3
        9:00 The Last of Us
        10:15 Uncharted 4: A Thief's End
        11:30 Bloodborne
        12:40 A Plague Tale: Requiem
        14:00 Batman: Arkham City
        15:00 Middle-Earth: Shadow of Mordor
        16:15 Nier: Automata
        17:40 Black Myth: Wukong*/

        imageViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectingImages.launch(intent);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

            }
        });

    }

    public void updateData() {
        if (id == -1) {
            Toast.makeText(this, "There is A problem ", Toast.LENGTH_SHORT).show();
        } else {
            String updateTitle = editTextUpdateTitle.getText().toString();
            String updateDesc = editTextUpdateDesc.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("updateTitle", updateTitle);
            intent.putExtra("updateDesc", updateDesc);
            if (selectedImage == null) {
                intent.putExtra("image", image);
                Toast.makeText(UpdateImageActivity.this, "Please select an Image!", Toast.LENGTH_SHORT).show();
            } else {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scledImage = makeSmallImg(selectedImage, 300);
                scledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] image = outputStream.toByteArray();
                intent.putExtra("image", image);
            }
            setResult(RESULT_OK, intent);
            finish();

        }
        //selectedImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
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
                                imageViewUpdate.setImageBitmap(selectedImage);
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