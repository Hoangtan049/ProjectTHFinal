package com.example.projectthfinal.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectthfinal.R;
import com.example.projectthfinal.ui.UpdateProductActivity;
import com.example.projectthfinal.utils.UserDAO;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    Context context;
    List<Products> list;

    public ProductAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_products, parent, false);
        return new ProductHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
        Products products= list.get(position);
        holder.txtName.setText(products.getName());
        holder.txtDes.setText(products.getDescription());
        holder.txtPrice.setText(products.getPrice()+"VND");
        holder.txtStock.setText(products.getStock()+"");
        holder.txtCate.setText(products.getCategoryName());
        Uri imageUri = Uri.parse(products.getImageName());
        Glide.with(context).load(imageUri).into(holder.imageProduct);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDAO userDAO= new UserDAO(context);
                userDAO.deleteProduct(list.get(holder.getAdapterPosition()).getId());
                list.remove(list.get(holder.getAdapterPosition()));
                notifyItemRemoved(holder.getAdapterPosition());

            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition=holder.getAdapterPosition();
                Products currentProduct= list.get(currentPosition);
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("product_id",currentProduct.getId());
                intent.putExtra("product_name",currentProduct.getName());
                intent.putExtra("product_description",currentProduct.getDescription());
                intent.putExtra("product_price",currentProduct.getPrice());
                intent.putExtra("product_stock",currentProduct.getStock());
                intent.putExtra("product_image",currentProduct.getImageName());
                intent.putExtra("product_category_id",currentProduct.getCategoryId());
                intent.putExtra("product_category_name",currentProduct.getCategoryName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView txtName,txtDes,txtPrice,txtStock,txtCate;
        ImageButton btnDelete,btnUpdate;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct=itemView.findViewById(R.id.imageProduct);
            txtName=itemView.findViewById(R.id.txtName);
            txtDes=itemView.findViewById(R.id.txtDes);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            txtStock=itemView.findViewById(R.id.txtStock);
            txtCate=itemView.findViewById(R.id.txtCate);
            btnUpdate=itemView.findViewById(R.id.btnUpdate);
            btnDelete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
