package com.example.projectthfinal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OnlineStoreDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "OnlineStore.db";
    private static final int DATABASE_VERSION = 6;

    // Bảng Users
    private static final String CREATE_USERS_TABLE = "CREATE TABLE Users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT UNIQUE NOT NULL, " +
            "password TEXT NOT NULL, " +
            "email TEXT UNIQUE NOT NULL, " +
            "role TEXT CHECK(role IN ('admin', 'customer')) NOT NULL DEFAULT 'customer');";

    // Bảng Customers
    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE Customers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER UNIQUE NOT NULL, " +
            "full_name TEXT NOT NULL, " +
            "address TEXT NOT NULL, " +
            "phone TEXT NOT NULL, " +
            "FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE);";

    // Bảng Products
    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE Products (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "description TEXT, " +
            "price REAL NOT NULL, " +
            "stock INTEGER NOT NULL DEFAULT 0,"+
            "image TEXT NOT NULL, " +
            "category_id INTEGER, " +
            "FOREIGN KEY (category_id) REFERENCES Categories(id) ON DELETE CASCADE);";
    // Bảng Categories
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE Categories (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL UNIQUE, " +
            "image TEXT NOT NULL);";
    // Bảng Orders
    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE Orders (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "order_date TEXT DEFAULT CURRENT_TIMESTAMP, " +
            "product_id INTEGER NOT NULL, " +
            "product_name TEXT NOT NULL, " +
            "image_product TEXT NOT NULL, " +
            "quantity INTEGER NOT NULL DEFAULT 1, " +
            "total_price REAL NOT NULL, " +
            "status TEXT CHECK(status IN ('pending', 'completed', 'canceled')) NOT NULL DEFAULT 'pending', " +
            "FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE);";


    public OnlineStoreDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CUSTOMERS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);

        db.execSQL("INSERT INTO Users (username, password, email, role) " +
                "VALUES ('admin', 'admin123', 'admin@example.com', 'admin');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 3) { // Kiểm tra nếu database cũ không có bảng Categories
            db.execSQL(CREATE_CATEGORIES_TABLE);
            db.execSQL("ALTER TABLE Products ADD COLUMN category_id INTEGER REFERENCES Categories(id) ON DELETE SET NULL;");
        }
        if (oldVersion < 4) { // Nếu nâng cấp từ phiên bản cũ lên 4
            db.execSQL("INSERT OR IGNORE INTO Users (username, password, email, role) " +
                    "VALUES ('admin', 'admin123', 'admin@example.com', 'admin');");
        }
        if (oldVersion < 5) {
            db.execSQL("DROP TABLE IF EXISTS Customers");
            db.execSQL("ALTER TABLE Orders RENAME COLUMN customer_id TO user_id;");
        }
        if (oldVersion < 6) {  // Giả sử phiên bản tiếp theo là 6
            db.execSQL("ALTER TABLE Orders ADD COLUMN product_id INTEGER;");
            db.execSQL("ALTER TABLE Orders ADD COLUMN product_name TEXT;");
            db.execSQL("ALTER TABLE Orders ADD COLUMN quantity INTEGER DEFAULT 1;");
            db.execSQL("ALTER TABLE Orders ADD COLUMN image_product TEXT NOT NULL DEFAULT '';");
        }
    }
}
