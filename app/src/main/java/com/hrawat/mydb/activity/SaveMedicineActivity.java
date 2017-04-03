package com.hrawat.mydb.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hrawat.mydb.R;
import com.hrawat.mydb.model.Medicine;

public class SaveMedicineActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText inputManufacturer;
    private TextInputEditText inputName;
    private TextInputEditText inputPrice;
    private TextInputEditText inputQuantity;
    private TextInputEditText inputUnit;

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
        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveMedicineDetails();
                break;
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
}
