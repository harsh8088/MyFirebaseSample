package com.hrawat.mydb.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrawat.mydb.R;
import com.hrawat.mydb.model.Medicine;
import com.hrawat.mydb.utils.RuntimePermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements RuntimePermissionUtils.OnPermissionResult,
        View.OnClickListener {

    private DatabaseReference mDatabase;
    private ArrayList<Medicine> medicineList;
    private RuntimePermissionUtils permissionUtilsFragment;
    private Button btnCreate;
    private Button btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkRuntimePermission();
//        readData();
//        mDatabase.child("Medicine").child("cxpi").child("price").setValue("400");
    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        medicineList = new ArrayList<>();
        permissionUtilsFragment = (RuntimePermissionUtils) getSupportFragmentManager()
                .findFragmentByTag(RuntimePermissionUtils.TAG);
        btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(this);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                break;
            case R.id.btn_show:
                startActivity(ProductListingActivity2.class);
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
        mDatabase.child("Medicine").addValueEventListener(new ValueEventListener() {
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
        mDatabase.onDisconnect();
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
