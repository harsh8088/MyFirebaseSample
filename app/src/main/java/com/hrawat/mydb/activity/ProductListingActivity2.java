package com.hrawat.mydb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrawat.mydb.R;
import com.hrawat.mydb.adapter.ProductListAdapter;
import com.hrawat.mydb.model.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 26-03-2017.
 */
public class ProductListingActivity2 extends BaseActivity {

    public DatabaseReference mDatabase;
    ArrayList<Medicine> list;
    ProductListAdapter productAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hero);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductListAdapter();
        recyclerView.setAdapter(productAdapter);
        showToast("Fetching medicine list");
        showProgress();
        mDatabase.child("Medicine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                manipulateData((HashMap<String, Medicine>) dataSnapshot.getValue());
                hideProgress();
                showToast("List updated...");
                productAdapter.replaceAll(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void manipulateData(Map<String, Medicine> map) {
        list.clear();
        for (Map.Entry<String, Medicine> entry : map.entrySet()) {
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
            list.add(new Medicine(name, url, manufacturer, price, quantity, unit));
        }
    }
}