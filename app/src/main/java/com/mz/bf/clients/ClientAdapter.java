package com.mz.bf.clients;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mz.bf.R;
import com.mz.bf.addclient.MapsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder> {
    List<Client> clientList;
    Context context;


    public ClientAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_item2,parent,false);
        return new ClientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClientHolder holder, int position) {
        holder.setData(clientList.get(position));
        holder.btn_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String labelLocation = clientList.get(position).getClientName();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + clientList.get(position).getLat()  + ">,<" + clientList.get(position).getLong() + ">?q=<" + clientList.get(position).getLat()  + ">,<" + clientList.get(position).getLong() + ">(" + labelLocation + ")"));
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
                //billsFragment.setClientData(clientList.get(position));
            }
        });
        holder.whatsapp_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whatsAppMessage = "http://maps.google.com/maps?saddr=" + clientList.get(position).getLat() + "," + clientList.get(position).getLong();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                context.startActivity(sendIntent);
            }
        });
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("flag",3);
                intent.putExtra("lat",clientList.get(position).getLat());
                intent.putExtra("long",clientList.get(position).getLong());
                intent.putExtra("client_id",clientList.get(position).getClientIdFk());
                context.startActivity(intent);
                //billsFragment.setClientData(clientList.get(position));
            }
        });
        holder.txt_client_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse("tel:" + clientList.get(position).getMob());

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try
                {
                    // Launch the Phone app's dialer with a phone
                    // number to dial a call.
                    context.startActivity(i);
                }
                catch (SecurityException s)
                {
                    // show() method display the toast with
                    // exception message.
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    class ClientHolder extends RecyclerView.ViewHolder{
        TextView txt_client_name,txt_client_phone;
        Button btn_see,btn_update,whatsapp_img;
        public ClientHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt_client_name = itemView.findViewById(R.id.txt_client_name);
            txt_client_phone = itemView.findViewById(R.id.txt_client_phone);
            btn_see = itemView.findViewById(R.id.btn_see);
            btn_update = itemView.findViewById(R.id.btn_update);
            whatsapp_img = itemView.findViewById(R.id.whatsapp_img);
        }

        public void setData(Client client) {
            txt_client_name.setText(client.getClientName());
            txt_client_phone.setText(client.getMob());
        }
    }
    public void add_client(List<Client> clientList1){
        for (Client client: clientList1){
            clientList.add(client);
        }
        notifyDataSetChanged();
    }
}
