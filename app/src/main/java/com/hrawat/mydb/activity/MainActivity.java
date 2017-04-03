package com.hrawat.mydb.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hrawat.mydb.R;
import com.hrawat.mydb.model.Medicine;
import com.hrawat.mydb.utils.RuntimePermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements RuntimePermissionUtils.OnPermissionResult,
        View.OnClickListener {

    private ArrayList<Medicine> medicineList;
    private RuntimePermissionUtils permissionUtilsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkRuntimePermission();
    }

    private void init() {
        medicineList = new ArrayList<>();
        permissionUtilsFragment = (RuntimePermissionUtils) getSupportFragmentManager()
                .findFragmentByTag(RuntimePermissionUtils.TAG);
        Button btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(this);
        Button btnCreate = (Button) findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                startActivity(SaveMedicineActivity.class);
                break;
            case R.id.btn_show:
                startActivity(ProductListActivity.class);
                break;
        }
    }

    private void checkRuntimePermission() {
        if (permissionUtilsFragment == null) {
            permissionUtilsFragment = new RuntimePermissionUtils();
            permissionUtilsFragment.setCallback(this);
            getSupportFragmentManager().beginTransaction()
                    .add(permissionUtilsFragment, RuntimePermissionUtils.TAG)
                    .commit();
        }
    }

    private void readData() {
        getDatabaseReference().child("Medicine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Medicine> td = (HashMap<String, Medicine>) dataSnapshot.getValue();
                for (Map.Entry<String, Medicine> entry : td.entrySet()) {
                    String name = null;
                    String manufacturer = null;
                    String price = null;
                    String quantity = null;
                    String unit = null;
                    String url = "";
                    Map<String, String> medicineMap = (Map<String, String>) entry.getValue();
                    for (Map.Entry<String, String> medEntry : medicineMap.entrySet()) {
                        String value = String.valueOf(medEntry.getValue());
                        switch (medEntry.getKey()) {
                            case "name":
                                name = value;
                                break;
                            case "manufacturer":
                                manufacturer = value;
                                break;
                            case "price":
                                price = value;
                                break;
                            case "quantity":
                                quantity = value;
                                break;
                            case "unit":
                                unit = value;
                                break;
                            case "url":
                                url = value;
                                break;
                        }
                    }
                    medicineList.add(new Medicine(name, url, manufacturer, price, quantity, unit));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(MainActivity.this, "failed to read", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUtilReady() {
    }

    @Override
    public void onPermissionGranted() {
        showToast("Permission Granted");
    }

    @Override
    public void onPermissionDenied() {
        showToast("Permission Denied");
    }
}
