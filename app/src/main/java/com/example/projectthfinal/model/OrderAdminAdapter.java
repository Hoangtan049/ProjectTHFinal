package com.example.projectthfinal.model;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderAdminViewHolder> {
    Context context;
    List<Orders> list;

    public OrderAdminAdapter(Context context, List<Orders> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdminAdapter.OrderAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_admin, parent, false);
        return new OrderAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdminAdapter.OrderAdminViewHolder holder, int position) {
        Orders orders= list.get(position);
        holder.TxtQuantityOrderAdmin.setText(String.valueOf(orders.getQuantity()));
        holder.txtDateOrderAdmin.setText(orders.getOrderDate());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        String formattedPrice = formatter.format(orders.getTotalPrice()) + " VND";
        holder.txtToTalAdmin.setText(formattedPrice);

        holder.txtStatusAdmin.setText(orders.getStatus());
        setStatusColor(holder.txtStatusAdmin,orders.getStatus());
        holder.txtProductNameOrderAdmin.setText(orders.getProductName());

        Uri imageUri = Uri.parse(orders.getImageProduct());
        Glide.with(context).load(imageUri).into(holder.imgProductOrderAdmin);
        holder.btnCanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus(orders,"canceled",holder.txtStatusAdmin);
            }
        });
        holder.btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus(orders,"completed",holder.txtStatusAdmin);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderAdminViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductNameOrderAdmin,TxtQuantityOrderAdmin,txtDateOrderAdmin,txtToTalAdmin,txtStatusAdmin;
        ImageView imgProductOrderAdmin;
        Button btnCanceled,btnCompleted;
        public OrderAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductNameOrderAdmin=itemView.findViewById(R.id.txtProductNameOrderAdmin);
            TxtQuantityOrderAdmin=itemView.findViewById(R.id.TxtQuantityOrderAdmin);
            txtDateOrderAdmin=itemView.findViewById(R.id.txtDateOrderAdmin);
            txtToTalAdmin=itemView.findViewById(R.id.txtToTalAdmin);
            txtStatusAdmin=itemView.findViewById(R.id.txtStatusAdmin);
            imgProductOrderAdmin=itemView.findViewById(R.id.imgProductOrderAdmin);
            btnCanceled=itemView.findViewById(R.id.btnCanceled);
            btnCompleted=itemView.findViewById(R.id.btnCompleted);
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
    private void updateOrderStatus(Orders order, String newStatus, TextView statusTextView) {
        UserDAO userDAO = new UserDAO(context);
        boolean isUpdated = userDAO.updateStatusOrder(order.getId(), newStatus);

        if (isUpdated) {
            order.setStatus(newStatus);
            statusTextView.setText(newStatus);
            setStatusColor(statusTextView, newStatus);
        }
    }
}
