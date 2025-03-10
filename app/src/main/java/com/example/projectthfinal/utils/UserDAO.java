package com.example.projectthfinal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectthfinal.database.OnlineStoreDatabaseHelper;
import com.example.projectthfinal.model.Categories;
import com.example.projectthfinal.model.Products;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private OnlineStoreDatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new OnlineStoreDatabaseHelper(context);
    }

    public boolean addCate(String name, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        long result = db.insert("Categories", null, values);
        return result != -1;
    }
    public  void  updateCate(int categoryId,String name , String image){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        db.update("Categories",values,"id=?",new String[]{String.valueOf(categoryId)});
        db.close();
    }
    public void  deleteCate(int categoryId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Categories","id=?",new String[]{String.valueOf(categoryId)});
        db.close();
    }
    public  List<Categories> getAllCate(){
        List<Categories> categoriesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        if((cursor.moveToNext())){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String image = cursor.getString(2);
                categoriesList.add(new Categories(id,name,image));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoriesList;
    }

    public boolean addProduct(String name, String description, double price, int stock, String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("stock", stock);
        values.put("image", imagePath);
        long result = db.insert("Products", null, values);
        return result != -1;
    }

    public void updateProduct(int productId, String name, String description, double price, int stock, String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("stock", stock);
        values.put("image", imagePath);
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
        Cursor cursor = db.rawQuery("SELECT * FROM Products", null);
        if ((cursor.moveToFirst())) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                double price = cursor.getDouble(3);
                int stock = cursor.getInt(4);
                String imagePath = cursor.getString(5);
                productsList.add(new Products(id, name, description, price, stock, imagePath));

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return productsList;
    }
}
