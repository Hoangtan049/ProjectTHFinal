package com.example.projectthfinal.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectthfinal.R;
import com.example.projectthfinal.utils.UserDAO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductUserAdapter extends RecyclerView.Adapter<ProductUserAdapter.ProductUserViewHolder> {
    Context context;
    List<Products> list;

    public ProductUserAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ProductUserAdapter.ProductUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_user, parent, false);
        return new ProductUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductUserAdapter.ProductUserViewHolder holder, int position) {
        Products products= list.get(position);
        holder.txtNameProductUser.setText(products.getName());
        holder.txtDesProductUser.setText(products.getDescription());
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        String formattedPrice = formatter.format(products.getPrice()) + " VND";
        holder.txtPriceProductUser.setText(formattedPrice);
        holder.txtCateProductUser.setText(products.getCategoryName());
        Uri imageUri = Uri.parse(products.getImageName());
        Glide.with(context).load(imageUri).into(holder.imgProduct);
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF",Context.MODE_PRIVATE);
        int userId= sharedPreferences.getInt("user_id",-1);
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId==-1){
                    Toast.makeText(context, "Chưa Đăng Nhập", Toast.LENGTH_SHORT).show();
                }
                UserDAO userDAO = new UserDAO(context);
                boolean isOrderCreate= userDAO.creatOrder(userId,products.getId(),1,products.getPrice());
                if (isOrderCreate){
                    Toast.makeText(context, "Mua hàng thành công chờ duyệt đơn hàng", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(context, "Mua hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductUserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtNameProductUser, txtDesProductUser, txtPriceProductUser, txtCateProductUser;
        Button btnBuy;

        public ProductUserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtNameProductUser = itemView.findViewById(R.id.txtNameProductUser);
            txtDesProductUser = itemView.findViewById(R.id.txtDesProductUser);
            txtPriceProductUser = itemView.findViewById(R.id.txtPriceProductUser);
            txtCateProductUser = itemView.findViewById(R.id.txtCateProductUser);
            btnBuy = itemView.findViewById(R.id.btnBuy);

        }
    }
}
