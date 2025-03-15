package com.example.projectthfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectthfinal.model.User;
import com.example.projectthfinal.ui.HomeActivity;
import com.example.projectthfinal.ui.RegisterActivity;
import com.example.projectthfinal.ui.user.UserActivity;
import com.example.projectthfinal.utils.UserDAO;

public class MainActivity extends AppCompatActivity {
Button btnLogin,btnRegister;
EditText edtUserNameLogin,edtPasswordLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        edtUserNameLogin=findViewById(R.id.edtUserNameLogin);
        edtPasswordLogin=findViewById(R.id.edtPasswordLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUserNameLogin.getText().toString();
                String password= edtPasswordLogin.getText().toString();
                if (name.isEmpty()) {
                    edtUserNameLogin.setError("Tên đăng nhập không được trống");

                } else {
                    edtUserNameLogin.setError(null);

                }
                if (password.isEmpty()) {
                    edtPasswordLogin.setError("Mật khẩu không được trống");

                } else {
                    edtPasswordLogin.setError(null);

                }
                if (name.isEmpty() || password.isEmpty() ) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserDAO userDAO= new UserDAO(MainActivity.this);
                User user = userDAO.loginUser(name,password);
                if(user!=null){
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.apply();
                    if (user.equals("admin")){
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this,UserActivity.class));
                    }
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Sai tài khoản ,mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(MainActivity.this, RegisterActivity.class);
               startActivity(intent);
            }
        });
    }
}