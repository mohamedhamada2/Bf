package com.mz.bf.adapters;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.mz.bf.R;
import com.mz.bf.databinding.BluthoosRowBinding;

import java.io.IOException;
import java.util.List;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothHolder> {

    private List<BluetoothDevice> list;
    private Context context;
    private AppCompatActivity appCompatActivity;
    public BluetoothAdapter(Context context, List<BluetoothDevice> list) {
        this.list = list;
        this.context = context;
        appCompatActivity = (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public BluetoothHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BluthoosRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bluthoos_row, parent, false);
        return new BluetoothHolder(binding);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull BluetoothHolder holder, int position) {
        holder.binding.setTitle(list.get(position).getName());


        holder.itemView.setOnClickListener(v -> {
           /* if (appCompatActivity instanceof PrintSalesInvoiceActivity){

            }*/







        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class BluetoothHolder extends RecyclerView.ViewHolder {
        BluthoosRowBinding binding;

        public BluetoothHolder(@NonNull BluthoosRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
