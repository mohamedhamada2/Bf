package com.mz.bf.addreturns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.addbill.Product;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    List<Product> productList;
    Context context;
    AddReturnsFragment billsFragment;

    public ProductAdapter(List<Product> productList, Context context, AddReturnsFragment billsFragment) {
        this.productList = productList;
        this.context = context;
        this.billsFragment = billsFragment;
    }

    @NonNull
    @NotNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductHolder holder, int position) {
        holder.setData(productList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFragment.setData(productList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        TextView txt_product_name,txt_code;
        public ProductHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_code = itemView.findViewById(R.id.txt_code);
        }

        public void setData(Product product) {
            txt_product_name.setText(product.getProductName());
            txt_code.setText(product.getProductCode());
        }
    }
    public void add_product(List<Product> productList1){
        for (Product product: productList1){
            productList.add(product);
        }
        notifyDataSetChanged();
    }
}
