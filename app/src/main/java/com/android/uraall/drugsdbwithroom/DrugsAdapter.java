package com.android.uraall.drugsdbwithroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Model.Drug;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Drug> drugs;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView priceTextView;


        public MyViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);

        }
    }


    public DrugsAdapter(Context context, ArrayList<Drug> drugs, MainActivity mainActivity) {
        this.context = context;
        this.drugs = drugs;
        this.mainActivity = mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final Drug drug = drugs.get(position);

        holder.nameTextView.setText(drug.getName());
        holder.priceTextView.setText(drug.getPrice() + " $");

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                mainActivity.addAndEditDrugs(true, drug, position);
            }
        });

    }

    @Override
    public int getItemCount() {

        return drugs.size();
    }


}
