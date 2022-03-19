package com.example.myapplication.payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{

    private ArrayList<ArrayList<String>> arr;

    public PaymentAdapter(ArrayList<ArrayList<String>> data) {
        this.arr = data;
    }

    public PaymentAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_details_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.t1.setText(arr.get(position).get(0));
        holder.t2.setText(arr.get(position).get(1));


    }

    @Override
    public int getItemCount() {
        return this.arr.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1, t2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.PaymentCol1);
            t2 = itemView.findViewById(R.id.PaymentCol2);
        }
    }
}
