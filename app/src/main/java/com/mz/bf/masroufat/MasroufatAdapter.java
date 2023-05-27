package com.mz.bf.masroufat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.clients.Client;
import com.mz.bf.clients.ClientAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MasroufatAdapter extends RecyclerView.Adapter<MasroufatAdapter.MasroufatHolder> {
    List<Record> recordList;
    Context context;

    public MasroufatAdapter(List<Record> recordList, Context context) {
        this.recordList = recordList;
        this.context = context;
    }

    @NonNull
    @Override
    public MasroufatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.masrouf_item,parent,false);
        return new MasroufatAdapter.MasroufatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasroufatHolder holder, int position) {
        holder.setData(recordList.get(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class MasroufatHolder extends RecyclerView.ViewHolder{
        TextView txt_masrouf_cat,txt_masrouf_date,txt_value;
        public MasroufatHolder(@NonNull View itemView) {
            super(itemView);
            txt_value = itemView.findViewById(R.id.txt_value);
            txt_masrouf_cat = itemView.findViewById(R.id.txt_masrouf_cat);
            txt_masrouf_date = itemView.findViewById(R.id.txt_masrouf_date);
        }

        public void setData(Record record) {
            txt_value.setText(record.getValue());
            txt_masrouf_cat.setText(record.getTitleSetting());
            txt_masrouf_date.setText(record.getDateEsal());
        }
    }
    public void add_client(List<Record> clientList1){
        for (Record client: clientList1){
            recordList.add(client);
        }
        notifyDataSetChanged();
    }
}
