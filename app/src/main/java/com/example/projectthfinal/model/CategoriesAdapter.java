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
import com.example.projectthfinal.ui.UpdateCategoryActivity;
import com.example.projectthfinal.utils.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
    Context context;
    List<Categories> CategoryList;

    public CategoriesAdapter(Context context, List<Categories> categoryList) {
        this.context = context;
        CategoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categories categories=CategoryList.get(position);
        holder.txtcateName.setText(categories.getName());
        Uri imageUri = Uri.parse(categories.getImage());
        Glide.with(context).load(imageUri).into(holder.cateImg);
        holder.btnDeleteCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition= holder.getAdapterPosition();
                UserDAO userDAO= new UserDAO(context);
                userDAO.deleteCate(CategoryList.get(currentPosition).getId());
                CategoryList.remove(CategoryList.get(currentPosition));
                notifyItemRemoved(currentPosition);
            }
        });
        holder.btnUpdateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition= holder.getAdapterPosition();
                Categories currentCate= CategoryList.get(currentPosition);
                Intent intent= new Intent(context, UpdateCategoryActivity.class);
                intent.putExtra("category_id",currentCate.getId());
                intent.putExtra("category_name",currentCate.getName());
                intent.putExtra("category_image",currentCate.getImage());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView cateImg;
        TextView txtcateName;
        ImageButton btnUpdateCate,btnDeleteCate;
        public CategoryViewHolder(@NonNull View itemView) {

            super(itemView);
            cateImg=itemView.findViewById(R.id.cateImg);
            txtcateName=itemView.findViewById(R.id.txtcateName);
            btnDeleteCate=itemView.findViewById(R.id.btnDeleteCate);
            btnUpdateCate=itemView.findViewById(R.id.btnUpdateCate);
        }
    }
}
