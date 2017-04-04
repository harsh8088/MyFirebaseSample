package com.hrawat.mydb.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrawat.mydb.R;
import com.hrawat.mydb.model.Medicine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 27-03-2017.
 */
public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Medicine> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName, price, manufacturerName, unit, quantity;
        ImageView medicineImage;

        public ViewHolder(View view) {
            super(view);
            medicineName = (TextView) view.findViewById(R.id.tv_medName);
            price = (TextView) view.findViewById(R.id.tv_price);
            manufacturerName = (TextView) view.findViewById(R.id.tv_manufacturerName);
            medicineImage = (ImageView) view.findViewById(R.id.iv_image);
        }
    }

    public ProductListAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_products_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Medicine item = list.get(position);
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.medicineName.setText(item.getName());
            viewHolder.price.setText(item.getPrice());
            viewHolder.manufacturerName.setText(item.getManufacturer());
            if (!item.getUrl().isEmpty()) {
                try {
                    viewHolder.medicineImage.setImageBitmap(decodeFromFirebaseBase64(item.getUrl()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void replaceAll(List<Medicine> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }
}