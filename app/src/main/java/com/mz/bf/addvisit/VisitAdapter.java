package com.mz.bf.addvisit;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.Utilities.Constants;
import com.mz.bf.addpayment.Client;
import com.mz.bf.customerservice.CustomerServiceActivity;
import com.squareup.picasso.Picasso;

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
                createpopup(visitList.get(position));
            }
        });
    }

    private void createpopup(Visit visit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.login_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        ImageView img = view.findViewById(R.id.img);
        TextView txt_details = view.findViewById(R.id.txt_details);
        txt_details.setText(visit.getNotes());
        if (visit.getImage() != null){
            Picasso.get().load(Constants.BASE_URL+"uploads/images/"+visit.getImage()).into(img);
        }else {
            img.setVisibility(View.GONE);
        }
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


    class VisitHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_visit_num)
        TextView txt_visit_num;
        @BindView(R.id.txt_visit_date)
        TextView txt_visit_date;
        @BindView(R.id.txt_client_name)
        TextView txt_client_name;
        @BindView(R.id.txt_notes)
        TextView txt_notes;
        @BindView(R.id.relative_end_visit)
        RelativeLayout relative_end_visit;
        @BindView(R.id.btn_close_visit)
        Button btn_close_visit;
        @BindView(R.id.txt_end_visit_date)
        TextView txt_end_visit_date;

        public VisitHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Visit visit) {
            if (visit.getCheckType() == null ||visit.getCheckType().equals("start")) {
                relative_end_visit.setVisibility(View.GONE);
                btn_close_visit.setVisibility(View.VISIBLE);
                btn_close_visit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, AddVisitActivity.class);
                        intent.putExtra("id", visit.getId());
                        intent.putExtra("flag", 2);
                        context.startActivity(intent);
                    }
                });
            } else {
                txt_end_visit_date.setText(visit.getEndTime());
                relative_end_visit.setVisibility(View.VISIBLE);
                btn_close_visit.setVisibility(View.GONE);

            }
            txt_visit_num.setText(visit.getId());
            txt_client_name.setText(visit.getClientName());
            txt_visit_date.setText(visit.getStartTime());
        }

        public void add_visit(List<Visit> visitList2) {
            for (Visit visit : visitList2) {
                visitList.add(visit);
            }
            notifyDataSetChanged();
        }
    }
    public void add_visit(List<Visit> visitList2) {
        for (Visit visit : visitList2) {
            visitList.add(visit);
        }
        notifyDataSetChanged();
    }
}
