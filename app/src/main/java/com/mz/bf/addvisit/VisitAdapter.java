package com.mz.bf.addvisit;

import android.app.Dialog;
import android.content.Context;
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

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.VisitHolder> {
    List<Visit> visitList;
    Context context;

    public VisitAdapter(List<Visit> visitList, Context context) {
        this.visitList = visitList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public VisitHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.visit_item,parent,false);
        return  new VisitHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VisitHolder holder, int position) {
        holder.setData(visitList.get(position));
        holder.txt_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createpopup(visitList.get(position).getNotes());{
                }
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
        return visitList.size();
    }

    class VisitHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_visit_num)
        TextView txt_visit_num;
        @BindView(R.id.txt_visit_date)
        TextView txt_visit_date;
        @BindView(R.id.txt_client_name)
        TextView txt_client_name;
        @BindView(R.id.txt_notes)
        TextView txt_notes;

        public VisitHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Visit visit) {
            txt_visit_num.setText(visit.getId());
            txt_client_name.setText(visit.getClientName());
            txt_visit_date.setText(visit.getTime()+"  "+visit.getDateAr());
        }
    }
    public void add_visit(List<Visit> visitList2) {
        for (Visit visit : visitList2) {
            visitList.add(visit);
        }
        notifyDataSetChanged();
    }
}
