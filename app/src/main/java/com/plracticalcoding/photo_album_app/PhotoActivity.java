package com.plracticalcoding.photo_album_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.plracticalcoding.myapplication.R;

import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    RecyclerView rv;
    FloatingActionButton fab;
    private MyImagesViewModel myImagesViewModel;
    private ActivityResultLauncher<Intent> activityResultLauncherForAddImage;
    private ActivityResultLauncher<Intent> activityResultLauncherForUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo_album);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // register Method for adding images;
        registerActivityForAddImaghes();

        // register Method for updating images;
        registerActivityForUpdatImaghes();

        rv = findViewById(R.id.rvimg);
        fab = findViewById(R.id.fab);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MyImagesAdapter adapter = new MyImagesAdapter();
        rv.setAdapter(adapter);


        myImagesViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyImagesViewModel.class);

        myImagesViewModel.getAllImages().observe(PhotoActivity.this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {
                adapter.setImagesList(myImages);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoActivity.this, AddImageActivity.class);
                // call the registerrActivityForAddingimagesd
                activityResultLauncherForAddImage.launch(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myImagesViewModel.delete(adapter.getItemPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(rv);

        adapter.setLisntener(new MyImagesAdapter.OnImageClickeListener() {
            @Override
            public void onImageClick(MyImages myImages) {
                Intent intent = new Intent(PhotoActivity.this, UpdateImageActivity.class);
                intent.putExtra("id", myImages.getImage_id());
                intent.putExtra("title", myImages.getImage_title());
                intent.putExtra("description", myImages.getImage_description());
                intent.putExtra("image", myImages.getImage());

                // activityResultLancher.
                activityResultLauncherForUpdateImage.launch(intent);
            }
        });
    }

    private void registerActivityForUpdatImaghes() {
        activityResultLauncherForUpdateImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data != null) {
                            String title = data.getStringExtra("updateTitle");
                            String updateDescription = data.getStringExtra("updateDesc");
                            byte[] image = data.getByteArrayExtra("image");
                            int id = data.getIntExtra("id", -1);

                            MyImages myImages = new MyImages(title, updateDescription, image);
                            myImages.setImage_id(id);
                            myImagesViewModel.update(myImages);

                        }

                    }
                });
    }

    private void registerActivityForAddImaghes() {
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if (resultCode == RESULT_OK && data != null) {
                            String title = data.getStringExtra("title");
                            String desc = data.getStringExtra("description");
                            byte[] image = data.getByteArrayExtra("image");

                            // saving the date in tzhe data base :
                            MyImages myImages = new MyImages(title, desc, image);
                            myImagesViewModel.insert(myImages);
                        }
                    }
                });
    }
}