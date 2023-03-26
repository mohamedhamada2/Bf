package com.mz.bf.products;

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
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {
    List<Product> productList;

    public ProductsAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    Context context;

    @NonNull
    @NotNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.products_item3,parent,false);
       return new ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsHolder holder, int position) {
        holder.setData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_product_name)
        TextView txt_product_name;
        @BindView(R.id.txt_one_amount)
        TextView txt_one_amount;
        @BindView(R.id.txt_packet_amount)
        TextView txt_packet_amount;
        @BindView(R.id.txt_one_sell_price)
        TextView txt_one_sell_price;
        @BindView(R.id.txt_packet_sell_price)
        TextView txt_packet_sell_price;
        public ProductsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Product product) {
            txt_product_name.setText(product.getProductCode());
            txt_one_sell_price.setText(product.getOneSellPrice());
            txt_packet_sell_price.setText(product.getPacketSellPrice());
            txt_one_amount.setText(product.getOneRasied()+"");
            txt_packet_amount.setText(product.getPacketRasied()+"");
        }
    }
    public void add_product(List<Product> productList2) {
        for (Product product : productList2) {
            productList.add(product);
        }
        notifyDataSetChanged();
    }
}
