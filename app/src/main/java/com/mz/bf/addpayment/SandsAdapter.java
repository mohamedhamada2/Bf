package com.mz.bf.addpayment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.uis.activity_print_bill.PrintSandActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SandsAdapter extends RecyclerView.Adapter<SandsAdapter.SandsHolder> {
    List<Client> clientList;
    Context context;
    PaymentActivity paymentActivity;

    public SandsAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
        paymentActivity = (PaymentActivity) context;
    }

    @NonNull
    @NotNull
    @Override
    public SandsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sand_item,parent,false);
        return new SandsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SandsHolder holder, int position) {
        holder.setData(clientList.get(position));
        holder.txt_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createpopup(clientList.get(position).getNotes());
            }
        });
        holder.print_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PrintSandActivity.class);
                intent.putExtra("sand_id",clientList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    private void createpopup(String notes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.login_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        TextView txt_details = view.findViewById(R.id.txt_details);
        txt_details.setText(notes);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    class SandsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_sand_num)
        TextView txt_sand_num;
        @BindView(R.id.txt_sand_date)
        TextView txt_sand_date;
        @BindView(R.id.txt_client_name)
        TextView txt_client_name;
        @BindView(R.id.txt_value)
        TextView txt_value;
        @BindView(R.id.txt_notes)
        TextView txt_notes;
        @BindView(R.id.print_img)
        ImageView print_img;
        public SandsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Client client) {
            txt_sand_num.setText(client.getRkmEsal());
            txt_sand_date.setText(client.getDateAr());
            txt_client_name.setText(client.getClientName());
            txt_value.setText(client.getValue());
        }
    }
    public void add_sand(List<Client> clientList2) {
        for (Client fatora : clientList2) {
            clientList.add(fatora);
        }
        notifyDataSetChanged();
    }
}
