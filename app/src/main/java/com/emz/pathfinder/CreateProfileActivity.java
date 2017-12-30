package com.emz.pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateProfileActivity extends AppCompatActivity {

    private EditText nameEt, lastNameEt;
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        bindView();
        setupView();
    }

    private void bindView() {
        nameEt = findViewById(R.id.input_name);
        lastNameEt = findViewById(R.id.input_lastname);
        genderSpinner = findViewById(R.id.select_gender);
    }

    private void setupView() {
        String[] genderArray = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, genderArray);
        genderSpinner.setAdapter(adapterGender);
    }
}
