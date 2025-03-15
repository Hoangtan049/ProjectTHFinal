package com.example.projectthfinal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.projectthfinal.MainActivity;
import com.example.projectthfinal.R;
import com.example.projectthfinal.utils.UserDAO;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtConfirmPassword, edtEmail;
    Button btnRegisterUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassWord);
        edtConfirmPassword = findViewById(R.id.edtConfirmPass);
        edtEmail = findViewById(R.id.edtEmail);
        btnRegisterUser=findViewById(R.id.btnRegisterUser);
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });
    }
    public void SaveData() {
        String name = edtUsername.getText().toString();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty()) {
            edtUsername.setError("Tên đăng nhập không được trống");

        } else {
            edtUsername.setError(null);

        }
        if (password.isEmpty()) {
            edtPassword.setError("Mật khẩu không được trống");

        } else {
            edtPassword.setError(null);

        }
        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.setError("Mật khẩu không được trống");

        } else {
            edtConfirmPassword.setError(null);

        }
        if (email.isEmpty()) {
            edtEmail.setError("Email không được trống");

        } else {
            edtEmail.setError(null);

        }

        if (name.isEmpty() || password.isEmpty() || email.isEmpty() ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }
        UserDAO userDAO= new UserDAO(this);
        boolean isRegister = userDAO.registerUser(name,password,email);
        if (isRegister) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        finish();
        }else {
            Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}