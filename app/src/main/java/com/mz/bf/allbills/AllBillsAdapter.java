package com.mz.bf.allbills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mz.bf.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllBillsAdapter extends RecyclerView.Adapter <AllBillsAdapter.AllBillsHolder>{
    List<Fatora> fatoraList;
    Context context;
    AllBillsFragment allBillsFragment;

    public AllBillsAdapter(List<Fatora> fatoraList, Context context,AllBillsFragment allBillsFragment) {
        this.fatoraList = fatoraList;
        this.context = context;
        this.allBillsFragment = allBillsFragment;
    }

    @NonNull
    @NotNull
    @Override
    public AllBillsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fatora_item,parent,false);
        return new AllBillsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllBillsHolder holder, int position) {
        holder.setData(fatoraList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context, fatoraList.get(position).getDafterRkmFatora(), Toast.LENGTH_SHORT).show();
                allBillsFragment.setBills_product(fatoraList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return fatoraList.size();
    }

    class AllBillsHolder extends RecyclerView.ViewHolder{
        TextView txt_total,txt_client,txt_bill_num,txt_bill_date,txt_details;
        public AllBillsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt_total = itemView.findViewById(R.id.txt_total);
            txt_client = itemView.findViewById(R.id.txt_client_name);
            txt_bill_num = itemView.findViewById(R.id.txt_bill_num);
            txt_bill_date = itemView.findViewById(R.id.txt_bill_date);
            txt_details = itemView.findViewById(R.id.txt_details);
        }

        public void setData(Fatora fatora) {
            txt_total.setText(fatora.getFatoraCostAfterDiscount());
            txt_client.setText(fatora.getClientName());
            txt_bill_date.setText(fatora.getDate());
            txt_bill_num.setText(fatora.getRkmFatora());
        }
    }
    public void add_bill(List<Fatora> fatoraList2) {
        for (Fatora fatora : fatoraList2) {
            fatoraList.add(fatora);
        }
        notifyDataSetChanged();
    }
}
