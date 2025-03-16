package com.example.projectthfinal.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectthfinal.MainActivity;
import com.example.projectthfinal.R;
import com.example.projectthfinal.model.CustomerAdapter;
import com.example.projectthfinal.model.ProductAdapter;
import com.example.projectthfinal.model.Products;
import com.example.projectthfinal.model.User;
import com.example.projectthfinal.utils.UserDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomersFragment extends Fragment {
    RecyclerView recyclerView;
    UserDAO userDAO;
    CustomerAdapter customerAdapter;
    Button btnLogoutAdmin;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomersFragment newInstance(String param1, String param2) {
        CustomersFragment fragment = new CustomersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_customers, container, false);
        recyclerView=v.findViewById(R.id.rcl_customer);
        btnLogoutAdmin=v.findViewById(R.id.btnLogOutAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userDAO= new UserDAO(getContext());
        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Đăng Xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa tất cả activity trước đó
                            startActivity(intent);
                            getActivity().finish();
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
        loadCustomer();
        return v;
    }
    private void loadCustomer() {
        List<User> customerlist= userDAO.getAllUsers();
        customerAdapter= new CustomerAdapter(getContext(),customerlist,userDAO);
        recyclerView.setAdapter(customerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCustomer();
    }
}