package com.example.projectthfinal.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectthfinal.R;
import com.example.projectthfinal.database.OnlineStoreDatabaseHelper;
import com.example.projectthfinal.utils.UserDAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Spinner spnCate;
    EditText edtCreateName, edtCreateDes, edtCreatePrice, edtCreateStock;
    ImageButton imageCreate;
    Button btnCreate;
    Uri imageUri;
    OnlineStoreDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        databaseHelper = new OnlineStoreDatabaseHelper(this);
        edtCreateName = findViewById(R.id.edtCreateName);
        edtCreateDes = findViewById(R.id.edtCreateDes);
        edtCreatePrice = findViewById(R.id.edtCreatePrice);
        edtCreateStock = findViewById(R.id.edtCreateStock);
        imageCreate = findViewById(R.id.imageCreate);
        btnCreate = findViewById(R.id.btnCreate);
        imageCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsset();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = edtCreateName.getText().toString().trim();
                String Des = edtCreateDes.getText().toString().trim();
                double price = Double.parseDouble(edtCreatePrice.getText().toString().trim());
                int stock = Integer.parseInt(edtCreateStock.getText().toString().trim());
                if (imageUri  == null) {
                    Toast.makeText(CreateProductActivity.this, "Vui lòng chọn hình ảnh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserDAO userDAO = new UserDAO(CreateProductActivity.this);
                boolean isAdded = userDAO.addProduct(Name, Des, price, stock, imageUri.toString());
                if (isAdded) {
                    Toast.makeText(CreateProductActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                   finish();

                } else {
                    Toast.makeText(CreateProductActivity.this, "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openAsset() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Glide.with(this).load(imageUri).into(imageCreate);


        }
    }


}