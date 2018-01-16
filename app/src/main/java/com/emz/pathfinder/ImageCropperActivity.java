package com.emz.pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class ImageCropperActivity extends AppCompatActivity {

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        if(getIntent().getExtras() != null){
            imageFile = (File) getIntent().getExtras().get("picture");
        }else{
            finish();
        }
    }
}
