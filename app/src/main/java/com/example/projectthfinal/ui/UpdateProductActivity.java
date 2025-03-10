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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectthfinal.R;
import com.example.projectthfinal.utils.UserDAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edtUpdateName, edtUpdateDes, edtUpdatePrice, edtUpdateStock;
    ImageButton btnImage;
    int productId;
    Button btnUpdate;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        edtUpdateName = findViewById(R.id.edtUpdateName);
        edtUpdateDes = findViewById(R.id.edtUpdateDes);
        edtUpdatePrice = findViewById(R.id.edtUpdatePrice);
        edtUpdateStock = findViewById(R.id.edtUpdateStock);
        btnImage = findViewById(R.id.imageUpdate);
        btnUpdate = findViewById(R.id.btnUpdateProduct);

        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        String productName = intent.getStringExtra("product_name");
        String productDes = intent.getStringExtra("product_desription");
        double productPrice = intent.getDoubleExtra("product_price", 0);
        int productStock = intent.getIntExtra("product_stock", 0);
        String imagepath = intent.getStringExtra("product_image");
        edtUpdateName.setText(productName);
        edtUpdateDes.setText(productDes);
        edtUpdatePrice.setText(productPrice + "");
        edtUpdateStock.setText(productStock + "");
        if (imagepath != null) {
            imageUri = Uri.parse(imagepath);
            Glide.with(this).load(imageUri).into(btnImage);
        }
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsset();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edtUpdateName.getText().toString().trim();
                String newDes = edtUpdateDes.getText().toString().trim();
                double newPrice = Double.parseDouble(edtUpdatePrice.getText().toString());
                int newStock = Integer.parseInt(edtUpdateStock.getText().toString());
                UserDAO userDAO = new UserDAO(UpdateProductActivity.this);
                userDAO.updateProduct(productId, newName, newDes, newPrice, newStock, imageUri != null ? imageUri.toString() : null);
                Toast.makeText(UpdateProductActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
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

            // Giữ quyền truy cập Uri lâu dài
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Glide.with(this).load(imageUri).into(btnImage);


        }
    }


}