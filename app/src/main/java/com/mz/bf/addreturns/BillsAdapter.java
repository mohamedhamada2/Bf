package com.mz.bf.addreturns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.addbill.FatoraDetail;
import com.mz.bf.data.DatabaseClass;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillsHolder> {
    List<FatoraDetail> fatoraDetailList;
    Context context;
    AddReturnsFragment billsFragment;
    List<String> typeslist;
    String product_name,product_price,product_amount,type_id,discount,bonous,product_id;
    Double total_price;
    DatabaseClass databaseClass;
    DecimalFormat df;
    public BillsAdapter(List<FatoraDetail> fatoraDetailList, Context context, AddReturnsFragment billsFragment) {
        this.fatoraDetailList = fatoraDetailList;
        this.context = context;
        this.billsFragment = billsFragment;
        databaseClass =  Room.databaseBuilder(this.context.getApplicationContext(),DatabaseClass.class,"bills").allowMainThreadQueries().build();
    }

    @NonNull
    @NotNull
    @Override
    public BillsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_item,parent,false);
        return new BillsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BillsHolder holder, int position) {
        holder.setData(fatoraDetailList.get(position));
        holder.btn_delete_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFragment.delete_product(fatoraDetailList.get(position));
            }
        });
        holder.btn_edit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFragment.edit_bill(fatoraDetailList.get(position));
            }
        });
       /* holder.et_product_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    discount ="0.0";
                    Toast.makeText(context, discount+"", Toast.LENGTH_SHORT).show();
                    //nesba_discount = 0.0;
                    try {
                        total_price = Double.parseDouble(product_price)*Double.parseDouble(product_amount);
                        holder.et_product_total_price.setText(total_price+"");
                        FatoraDetail fatoraDetail = new FatoraDetail();
                        fatoraDetail.setProduct_id_fk(product_id);
                        fatoraDetail.setProduct_name(product_name);
                        fatoraDetail.setType(type_id);
                        fatoraDetail.setAmount(product_amount);
                        fatoraDetail.setSell_price(product_price);
                        fatoraDetail.setProduct_discount(discount);
                        fatoraDetail.setProduct_pouns(bonous);
                        fatoraDetail.setNotes("");
                        fatoraDetail.setTotal(total_price+"");
                        //databaseClass.getDao().editproduct(fatoraDetail);
                        //Toast.makeText(context, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                        billsFragment.getAllBills();
                    }catch (Exception e){
                        total_price = 0.0;
                        holder.et_product_total_price.setText(total_price+"");
                    }
                }else {
                    discount = charSequence.toString();
                    //nesba_discount = Double.parseDouble(discount);
                    try {
                        total_price = Double.parseDouble(product_price)*Double.parseDouble(product_amount)-Double.parseDouble(product_price)*Double.parseDouble(product_amount)*Double.parseDouble(discount)/100;
                        holder.et_product_total_price.setText(total_price+"");
                        FatoraDetail fatoraDetail = new FatoraDetail();
                        fatoraDetail.setProduct_id_fk(product_id);
                        fatoraDetail.setProduct_name(product_name);
                        fatoraDetail.setType(type_id);
                        fatoraDetail.setAmount(product_amount);
                        fatoraDetail.setSell_price(product_price);
                        fatoraDetail.setProduct_discount(discount);
                        fatoraDetail.setProduct_pouns(bonous);
                        fatoraDetail.setNotes("");
                        fatoraDetail.setTotal(total_price+"");
                        // databaseClass.getDao().editproduct(fatoraDetail);
                        //Toast.makeText(context, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                        billsFragment.getAllBills();
                    }catch (Exception e){
                        total_price = 0.0;
                        holder.et_product_total_price.setText(total_price+"");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
        /*holder.btn_edit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FatoraDetail fatoraDetail = new FatoraDetail();
                fatoraDetail.setProduct_id_fk(fatoraDetailList.get(position).getProduct_id_fk());
                fatoraDetail.setProduct_name(product_name);
                fatoraDetail.setType(type_id);
                fatoraDetail.setAmount(product_amount);
                fatoraDetail.setSell_price(product_price);
                fatoraDetail.setProduct_discount(discount);
                fatoraDetail.setProduct_pouns(bonous);
                fatoraDetail.setNotes("");
                fatoraDetail.setTotal(total_price+"");
                billsFragment.edit_product(fatoraDetailList.get(position));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return fatoraDetailList.size();
    }

    class BillsHolder extends RecyclerView.ViewHolder{
        TextView et_product_name,et_product_price,et_product_amount,et_product_discount,et_product_total_price,et_bonous;

        Button btn_delete_bill,btn_edit_bill;
        public BillsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            et_product_name = itemView.findViewById(R.id.txt_product_name);
            et_product_amount = itemView.findViewById(R.id.txt_product_amount);
            et_product_price = itemView.findViewById(R.id.txt_product_price);
            et_product_total_price = itemView.findViewById(R.id.txt_total);
            btn_delete_bill= itemView.findViewById(R.id.btn_delete_bill);
            btn_edit_bill= itemView.findViewById(R.id.btn_update_bill);
        }

        public void setData(FatoraDetail fatoraDetail) {
            df = new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US));
            product_id = fatoraDetail.getProduct_id_fk();
            typeslist = new ArrayList<>();
            typeslist.add("قطاعي");
            typeslist.add("جملة");
            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,R.layout.spinner_item2,typeslist);
            //type_spinner.setAdapter(arrayAdapter);
            total_price = Double.parseDouble(fatoraDetail.getTotal());
            product_price = fatoraDetail.getSell_price();
            product_amount = fatoraDetail.getAmount();
            discount = fatoraDetail.getProduct_discount();
            product_name = fatoraDetail.getProduct_name();
            et_product_name.setText(product_name);
            et_product_price.setText(product_price);
            et_product_amount.setText(df.format(Double.parseDouble(product_amount)));
            et_product_total_price.setText(df.format(total_price));
        }
    }
}
