package com.example.projectthfinal.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectthfinal.R;
import com.example.projectthfinal.utils.UserDAO;

public class UpdateCategoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edtUpdateNameCate;
    ImageButton imageUpdateCate;
    Button btnUpdateCate;
    Uri imageUri;
    int categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        edtUpdateNameCate=findViewById(R.id.edtUpdateNameCate);
        imageUpdateCate=findViewById(R.id.imageUpdateCate);
        btnUpdateCate=findViewById(R.id.btnUpdateCate);
        imageUpdateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsset();
            }
        });
        Intent intent= getIntent();
        categoryId = intent.getIntExtra("category_id", -1);
        String productName = intent.getStringExtra("category_name");
        String imagepath = intent.getStringExtra("category_image");
        edtUpdateNameCate.setText(productName);
        if (imagepath != null) {
            imageUri = Uri.parse(imagepath);
            Glide.with(this).load(imageUri).into(imageUpdateCate);
        }
        btnUpdateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName= edtUpdateNameCate.getText().toString().trim();
                UserDAO userDAO= new UserDAO(UpdateCategoryActivity.this);
                userDAO.updateCate(categoryId,newName,imageUri != null ? imageUri.toString() : null);
                Toast.makeText(UpdateCategoryActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
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
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Glide.with(this).load(imageUri).into(imageUpdateCate);
        }
    }
}