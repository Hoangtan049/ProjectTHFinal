package com.example.projectthfinal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.example.projectthfinal.database.OnlineStoreDatabaseHelper;
import com.example.projectthfinal.model.Categories;
import com.example.projectthfinal.model.Orders;
import com.example.projectthfinal.model.Products;
import com.example.projectthfinal.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserDAO {
    private OnlineStoreDatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new OnlineStoreDatabaseHelper(context);
    }

    public List<Orders> getAllOrders() {
        List<Orders> ordersList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                String image_product = cursor.getString(cursor.getColumnIndexOrThrow("image_product"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double priceTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                ordersList.add(new Orders(id, userId, date, productId, productName, image_product, quantity, priceTotal, status));

                Log.d("Database", "ID: " + id
                        + ", User ID: " + userId
                        + ", Date: " + date
                        + ", Product ID: " + productId
                        + ", Product Name: " + productName
                        + ", Image: " + image_product
                        + ", Quantity: " + quantity
                        + ", Total Price: " + priceTotal
                        + ", Status: " + status);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return ordersList;
    }

    public boolean updateStatusOrder(int orderId, String newstatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newstatus);
        int updatestatus = db.update("Orders", values, "id=?", new String[]{String.valueOf(orderId)});
        db.close();
        return updatestatus > 0;
    }

    public List<Orders> getOrderUser(int userId) {
        List<Orders> ordersList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT id, order_date, product_id, product_name, quantity, total_price, status ,image_product " +
                "FROM Orders WHERE user_id=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    Orders order = new Orders();
                    order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow("order_date")));
                    order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                    order.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                    order.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                    order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")));
                    order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                    order.setImageProduct(cursor.getString(cursor.getColumnIndexOrThrow("image_product")));

                    ordersList.add(order);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return ordersList;
    }

    public boolean creatOrder(int userId, int productId, int quantity, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            Cursor cursor = db.rawQuery("SELECT name, price, stock ,image FROM Products WHERE id=?", new String[]{String.valueOf(productId)});
            if (cursor != null && cursor.moveToFirst()) {
                String productName = cursor.getString(0);
                double productPrice = cursor.getDouble(1);
                int stock = cursor.getInt(2);
                String imageProduct = cursor.getString(3);
                cursor.close();
                if (stock < quantity) {
                    return false;
                }


                ContentValues orderValues = new ContentValues();
                orderValues.put("user_id", userId);
                orderValues.put("total_price", productPrice * quantity);
                orderValues.put("status", "pending");
                orderValues.put("order_date", currentDate);
                orderValues.put("product_id", productId);
                orderValues.put("product_name", productName);
                orderValues.put("quantity", quantity);
                orderValues.put("image_product", imageProduct);
                long orderId = db.insert("Orders", null, orderValues);
                if (orderId == -1) {
                    return false;  // Nếu lỗi, dừng lại
                }
                ContentValues uupdaStock = new ContentValues();
                uupdaStock.put("stock", stock - quantity);
                db.update("Products", uupdaStock, "id=?", new String[]{String.valueOf(productId)});
                db.setTransactionSuccessful();
            }
            return true;
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    public User getInfomation(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
    public String getUserRole(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String role =null;
        Cursor cursor = db.rawQuery("SELECT role FROM Users WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }
    public boolean updateUserRole(int userId, String role) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("role", role);

        int rowsAffected = db.update("Users", values, "id=?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }


    // Xóa tài khoản
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Users", "id=?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }


    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                String email = cursor.getString(3);
                String role = cursor.getString(4);
                userList.add(new User(id, username,password, email, role));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
