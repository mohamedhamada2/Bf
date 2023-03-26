package com.mz.bf.allbills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mz.bf.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BillDetailsAdapter extends RecyclerView.Adapter<BillDetailsAdapter.BillDetailsHolder> {
    List<Detail> detailList;
    Context context;
    DecimalFormat df;

    public BillDetailsAdapter(List<Detail> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public BillDetailsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_details_item,parent,false);
        return new BillDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BillDetailsHolder holder, int position) {
        holder.setData(detailList.get(position));
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    class BillDetailsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_product_name)
        TextView product_name;
        @BindView(R.id.txt_product_price)
        TextView product_price;
        @BindView(R.id.txt_product_amount)
        TextView product_amount;
        @BindView(R.id.txt_product_code)
        TextView txt_product_code;
        @BindView(R.id.txt_total)
        TextView txt_total;
        public BillDetailsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Detail detail) {
            df = new java.text.DecimalFormat("0.0",new DecimalFormatSymbols(Locale.US));
            product_name.setText(detail.getProductName());
            product_price.setText(df.format(Double.parseDouble(detail.getOne_price_sell()))+"");
            product_amount.setText(detail.getAllPieces());
            txt_total.setText(df.format(Double.parseDouble(detail.getTotal()))+"");
            txt_product_code.setText(detail.getProduct_code());
        }
    }
}
