package com.example.projectthfinal.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectthfinal.R;
import com.example.projectthfinal.model.User;
import com.example.projectthfinal.utils.UserDAO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfomationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfomationFragment extends Fragment {
TextView txtUsername,txtEmail,txtRole;
UserDAO userDAO;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfomationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfomationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfomationFragment newInstance(String param1, String param2) {
        InfomationFragment fragment = new InfomationFragment();
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
        View v = inflater.inflate(R.layout.fragment_infomation, container, false);
        txtRole=v.findViewById(R.id.txtRole);
        txtEmail=v.findViewById(R.id.txtEmail);
        txtUsername=v.findViewById(R.id.txtUsername);
        userDAO=new UserDAO(getActivity());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", "");
        if (name!=null){
            User user= userDAO.getInfomation(name);
            if(user!=null){
                txtUsername.setText(user.getUsername());
                txtEmail.setText(user.getEmail());
                txtRole.setText(user.getRole());

            }
        }
        return v;
    }
}