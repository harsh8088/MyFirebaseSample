package com.hrawat.mydb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hrawat.mydb.R;
import com.hrawat.mydb.model.Medicine;

import java.io.ByteArrayOutputStream;

public class SaveMedicineActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private TextInputEditText inputManufacturer;
    private TextInputEditText inputName;
    private TextInputEditText inputPrice;
    private TextInputEditText inputQuantity;
    private TextInputEditText inputUnit;
    private TextInputEditText inputUrl;
    private String imageEncoded;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_medicine);
        init();
    }

    private void init() {
        inputManufacturer = (TextInputEditText) findViewById(R.id.input_manufacturer);
        inputName = (TextInputEditText) findViewById(R.id.input_name);
        inputPrice = (TextInputEditText) findViewById(R.id.input_price);
        inputQuantity = (TextInputEditText) findViewById(R.id.input_quantity);
        inputUnit = (TextInputEditText) findViewById(R.id.input_unit);
        inputUrl = (TextInputEditText) findViewById(R.id.input_url);
        image = (ImageView) findViewById(R.id.image_medicine);
        inputUrl.setOnClickListener(this);
        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveMedicineDetails();
                break;
            case R.id.input_url:
                launchCamera();
                break;
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void saveMedicineDetails() {
        String name = inputName.getText().toString();
        String manufacturer = inputManufacturer.getText().toString();
        String price = inputPrice.getText().toString();
        String quantity = inputQuantity.getText().toString();
        String unit = inputUnit.getText().toString();
        if (!name.isEmpty()) {
            Medicine medicine = new Medicine();
            medicine.setName(name);
            medicine.setManufacturer(manufacturer);
            medicine.setPrice(price);
            medicine.setQuantity(quantity);
            medicine.setUnit(unit);
            medicine.setUrl(imageEncoded);
            getDatabaseReference().child("Medicine").child(name).setValue(medicine)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("Data saved...");
                                finish();
                            } else
                                showToast("Error");
                        }
                    });
        } else
            showToast("Name can't be empty");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }

    private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        inputUrl.setText(imageEncoded);
//        getDatabaseReference().child("Medicine").child(inputName.getText().toString())
//                .child("url").setValue(imageEncoded);
    }
}
