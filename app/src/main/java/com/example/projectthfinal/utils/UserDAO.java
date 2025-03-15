package com.example.projectthfinal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectthfinal.database.OnlineStoreDatabaseHelper;
import com.example.projectthfinal.model.Categories;
import com.example.projectthfinal.model.Products;
import com.example.projectthfinal.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private OnlineStoreDatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new OnlineStoreDatabaseHelper(context);
    }

    public boolean creatOrder(int userId, int productId, int quantity, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put("user_id", userId);
        orderValues.put("total_price", price * quantity);
        orderValues.put("status", "pending");
        long orderId = db.insert("Orders", null, orderValues);
        if (orderId == -1) {
            return false;  // Nếu lỗi, dừng lại
        }

        ContentValues detailValues = new ContentValues();
        detailValues.put("order_id", orderId);
        detailValues.put("product_id", productId);
        detailValues.put("quantity", quantity);
        detailValues.put("price", price);

        long detailResult = db.insert("OrderDetails", null, detailValues);
        db.close();
        return detailResult != -1;
    }

    public User getInfomation(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT username, email, role FROM Users WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String email = cursor.getString(1);
            String role = cursor.getString(2);
            user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setRole(role);
        }
        db.close();
        cursor.close();
        return user;
    }

    public boolean registerUser(String name, String password, String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", name);
        values.put("password", password);
        values.put("email", email);
        values.put("role", "customer");
        long result = db.insert("Users", null, values);
        db.close();
        return result != -1;
    }

    public User loginUser(String name, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, username, password, email, role FROM Users WHERE username=? AND password=?",
                new String[]{name, password});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String username = cursor.getString(1);
            String Password = cursor.getString(2);
            String email = cursor.getString(3);
            String role = cursor.getString(4);
            cursor.close();
            db.close();
            return new User(userId, username, Password, email, role);
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean addCate(String name, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        long result = db.insert("Categories", null, values);
        return result != -1;
    }

    public void updateCate(int categoryId, String name, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        db.update("Categories", values, "id=?", new String[]{String.valueOf(categoryId)});
        db.close();
    }

    public void deleteCate(int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Categories", "id=?", new String[]{String.valueOf(categoryId)});
        db.close();
    }

    public List<Categories> getAllCate() {
        List<Categories> categoriesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        if ((cursor.moveToFirst())) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String image = cursor.getString(2);
                categoriesList.add(new Categories(id, name, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoriesList;
    }

    public boolean addProduct(String name, String description, double price, int stock, String imagePath, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("stock", stock);
        values.put("image", imagePath);
        values.put("category_id", categoryId);
        long result = db.insert("Products", null, values);
        return result != -1;
    }

    public void updateProduct(int productId, String name, String description, double price, int stock, String imagePath, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("stock", stock);
        values.put("image", imagePath);
        values.put("category_id", categoryId);
        db.update("Products", values, "id=?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Products", "id=?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public List<Products> getallProduct() {
        List<Products> productsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT p.id, p.name, p.description, p.price, p.stock, p.image, p.category_id, c.name as category_name " +
                        "FROM Products p " +
                        "JOIN Categories c ON p.category_id = c.id", null);
        if ((cursor.moveToFirst())) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                double price = cursor.getDouble(3);
                int stock = cursor.getInt(4);
                String imagePath = cursor.getString(5);
                int categoryId = cursor.getInt(6);
                String categoryName = cursor.getString(7);
                productsList.add(new Products(id, name, description, price, stock, imagePath, categoryId, categoryName));

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return productsList;
    }
}
