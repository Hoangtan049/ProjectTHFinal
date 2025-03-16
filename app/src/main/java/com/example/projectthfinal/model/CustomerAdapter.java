package com.example.projectthfinal.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectthfinal.R;
import com.example.projectthfinal.utils.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    Context context;
    List<User> list;
    UserDAO userDAO;


    public CustomerAdapter(Context context, List<User> list, UserDAO userDAO) {
        this.context = context;
        this.list = list;
        this.userDAO = userDAO;

    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_admin, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder holder, int position) {
        User user= list.get(position);
        holder.txtuserId.setText(String.valueOf(user.getId()));
        holder.txtnamecustomer.setText(user.getUsername());
        holder.txtPasscustomer.setText(user.getPassword());
        holder.txtemailcustomer.setText(user.getEmail());

        String userRole = userDAO.getUserRole(user.getId());


        List<String> roleList = new ArrayList<>();
        roleList.add("admin");
        roleList.add("customer");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, roleList);
        holder.spnrole.setAdapter(adapter);


        if (userRole != null) {
            int spinnerPosition = adapter.getPosition(userRole);
            holder.spnrole.setSelection(spinnerPosition);
        }
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newrole = holder.spnrole.getSelectedItem().toString();
                boolean isUpdateRole= userDAO.updateUserRole(user.getId(),newrole);
                if (isUpdateRole) {
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView txtuserId,txtnamecustomer,txtPasscustomer,txtemailcustomer;
        Spinner spnrole;
        Button btnSave;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtuserId=itemView.findViewById(R.id.txtuserId);
            txtnamecustomer=itemView.findViewById(R.id.txtnamecustomer);
            txtPasscustomer=itemView.findViewById(R.id.txtPasscustomer);
            txtemailcustomer=itemView.findViewById(R.id.txtemailcustomer);
            spnrole=itemView.findViewById(R.id.spnrole);
            btnSave=itemView.findViewById(R.id.btnSave);
        }
    }
}
