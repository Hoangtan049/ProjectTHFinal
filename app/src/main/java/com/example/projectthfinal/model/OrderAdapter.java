package com.example.projectthfinal.model;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectthfinal.R;
import com.example.projectthfinal.utils.UserDAO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    List<Orders> list;


    public OrderAdapter(Context context, List<Orders> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_order_user, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Orders orders= list.get(position);
        holder.TxtQuantityOrderUser.setText(String.valueOf(orders.getQuantity()));
        holder.txtDateOrderUser.setText(orders.getOrderDate());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        String formattedPrice = formatter.format(orders.getTotalPrice()) + " VND";
        holder.txtToTalUser.setText(formattedPrice);

        holder.txtStatusUser.setText(orders.getStatus());
        setStatusColor(holder.txtStatusUser,orders.getStatus());
        holder.txtProductNameOrderUser.setText(orders.getProductName());

        Uri imageUri = Uri.parse(orders.getImageProduct());
        Glide.with(context).load(imageUri).into(holder.imgProductOrderUser);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView TxtQuantityOrderUser,txtDateOrderUser,txtToTalUser,txtStatusUser,txtProductNameOrderUser;
        ImageView imgProductOrderUser;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductNameOrderUser=itemView.findViewById(R.id.txtProductNameOrderUser);
            TxtQuantityOrderUser=itemView.findViewById(R.id.TxtQuantityOrderUser);
            txtDateOrderUser=itemView.findViewById(R.id.txtDateOrderUser);
            txtToTalUser=itemView.findViewById(R.id.txtToTalUser);
            txtStatusUser=itemView.findViewById(R.id.txtStatusUser);
            imgProductOrderUser=itemView.findViewById(R.id.imgProductOrderUser);
        }
    }
    private void setStatusColor(TextView textView, String status) {
        switch (status) {
            case "pending":
                textView.setTextColor(Color.YELLOW);
                break;
            case "canceled":
                textView.setTextColor(Color.RED);
                break;
            case "completed":
                textView.setTextColor(Color.GREEN);
                break;
            default:
                textView.setTextColor(Color.BLACK);
                break;
        }
    }

}
