package com.mz.bf.addreturns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.addbill.Client;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder> {
    List<Client> clientList;
    Context context;
    AddReturnsFragment billsFragment;

    public ClientAdapter(List<Client> clientList, Context context, AddReturnsFragment billsFragment) {
        this.clientList = clientList;
        this.context = context;
        this.billsFragment = billsFragment;
    }

    @NonNull
    @NotNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_item,parent,false);
        return new ClientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClientHolder holder, int position) {
        holder.setData(clientList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFragment.setClientData(clientList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    class ClientHolder extends RecyclerView.ViewHolder{
        TextView txt_product_name;
        public ClientHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
        }

        public void setData(Client client) {
            txt_product_name.setText(client.getClientName());
        }
    }
    public void add_client(List<Client> clientList1){
        for (Client client: clientList1){
            clientList.add(client);
        }
        notifyDataSetChanged();
    }
}
