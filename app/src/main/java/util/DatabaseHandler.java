package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.DosageModel;
import model.My_order_detail_model;

/**
 * Created by Rajesh on 2017-09-11.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DB_NAME = "getPills";
    private static int DB_VERSION = 3;
    private SQLiteDatabase db;

    public static final String CART_TABLE = "cart";

    public static final String COLUMN_ID = "product_id";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String COLUMN_CAT_ID = "category_id";
    public static final String COLUMN_IN_STOCK = "in_stock";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_UNIT_VALUE = "unit_value";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_MFG_ID = "mfg_id";
    public static final String COLUMN_IS_GENERIC = "isgeneric";
    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_DISCOUNT = "discount";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MFG_NAME = "mfg_name";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_TOTAL_DISC_AMOUNT = "total_discount_amount";
    public static final String COLUMN_TOTAL_ITEM_PRICE = "total_item_price";

    //Table Notification Count
    public static final String NOTIFICATION_TABLE = "notification_tbl";

    public static final String COLUMN_COUNT = "bell_count";
    public static final String CO_ID = "bell_id";

    //Table Dosage
    public static final String DOSAGE_TABLE = "dosage_tbl";

    public static final String MEDICINE = "medicine";
    public static final String DOSAGE_ID = "dosage_id";
    public static final String MEDICINE_TIME = "dosage_time";





    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + COLUMN_ID + " integer primary key, "
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_CAT_ID + " TEXT NOT NULL, "
                + COLUMN_IN_STOCK + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_UNIT_VALUE + " DOUBLE NOT NULL, "
                + COLUMN_UNIT + " TEXT NOT NULL, "
                + COLUMN_MFG_ID + " TEXT NOT NULL, "
                + COLUMN_IS_GENERIC + " TEXT NOT NULL, "
                + COLUMN_STOCK + " DOUBLE NOT NULL, "
                + COLUMN_GROUP_NAME + " TEXT NOT NULL, "
                + COLUMN_DISCOUNT + " DOUBLE NOT NULL, "
                + COLUMN_MFG_NAME + " TEXT NOT NULL, "
                + COLUMN_TOTAL_DISC_AMOUNT + " DOUBLE NOT NULL, "
                + COLUMN_TOTAL_ITEM_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL "
                + ")";

        String notiExe= "CREATE TABLE IF NOT EXISTS " + NOTIFICATION_TABLE + "(" + CO_ID + " integer primary key AUTOINCREMENT NOT NULL, "+ COLUMN_COUNT + " integer NOT NULL )";

        String dosageExe= "CREATE TABLE IF NOT EXISTS " + DOSAGE_TABLE + "(" + DOSAGE_ID + " integer primary key AUTOINCREMENT NOT NULL, "+ MEDICINE + " TEXT NOT NULL, "+MEDICINE_TIME+ " TEXT NOT NULL)";

        db.execSQL(exe);
        db.execSQL(notiExe);
        db.execSQL(dosageExe);
    }

    public boolean setCart(HashMap<String, String> map, Float Qty, Double Total_amount, Double Total_discount_amount) {
        db = getWritableDatabase();
        if (isInCart(map.get(COLUMN_ID))) {
            db.execSQL("update " + CART_TABLE + " set " +
                    COLUMN_TOTAL_DISC_AMOUNT + " = '" + Total_discount_amount + "'," +
                    COLUMN_TOTAL_ITEM_PRICE + " = '" + Total_amount + "'," +
                    COLUMN_QTY + " = '" + Qty + "' where " + COLUMN_ID + "=" + map.get(COLUMN_ID));
            return false;
        } else {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID, map.get(COLUMN_ID));
            values.put(COLUMN_QTY, Qty);
            values.put(COLUMN_CAT_ID, map.get(COLUMN_CAT_ID));
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_IS_GENERIC, map.get(COLUMN_IS_GENERIC));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_IN_STOCK, map.get(COLUMN_IN_STOCK));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
            values.put(COLUMN_TITLE, String.valueOf(map.get(COLUMN_TITLE)));
            values.put(COLUMN_UNIT, map.get(COLUMN_UNIT));
            values.put(COLUMN_MFG_ID, map.get(COLUMN_MFG_ID));
            values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            values.put(COLUMN_GROUP_NAME, map.get(COLUMN_GROUP_NAME));
            values.put(COLUMN_DISCOUNT, map.get(COLUMN_DISCOUNT));
            values.put(COLUMN_MFG_NAME, map.get(COLUMN_MFG_NAME));
            values.put(COLUMN_TOTAL_DISC_AMOUNT, Total_discount_amount);
            values.put(COLUMN_TOTAL_ITEM_PRICE, Total_amount);

            db.insert(CART_TABLE, null, values);
            return true;
        }
    }

    public boolean insertDosage(DosageModel model) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MEDICINE,model.MedicineName);
        values.put(MEDICINE_TIME,model.Time);
        db.insert(DOSAGE_TABLE, null, values);
        return true;
    }

    public boolean setNotificationBell(int Qty) {
        db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_COUNT, Qty);

            db.insert(NOTIFICATION_TABLE, null, values);
            return true;
    }
    public boolean isInCart(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;

        return false;
    }

    public String getCartItemQty(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        else
            return "0";

    }

    public String getInCartItemQty(String id) {
        if (isInCart(id)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_ID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0.0";
        }
    }

    public int getCartCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        return cursor.getCount();
    }

    public int getNotificationCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + NOTIFICATION_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        Log.d("NOTI----->",cursor.getCount()+"------");
        return cursor.getCount();
    }

    public String getTotalAmount() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }

    public String getTotalDiscountAmount() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_TOTAL_DISC_AMOUNT + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }

    public ArrayList<HashMap<String, String>> getCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_CAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_CAT_ID)));
            map.put(COLUMN_IN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_IN_STOCK)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_UNIT, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            map.put(COLUMN_MFG_ID, cursor.getString(cursor.getColumnIndex(COLUMN_MFG_ID)));
            map.put(COLUMN_IS_GENERIC, cursor.getString(cursor.getColumnIndex(COLUMN_IS_GENERIC)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            map.put(COLUMN_GROUP_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_NAME)));
            map.put(COLUMN_DISCOUNT, cursor.getString(cursor.getColumnIndex(COLUMN_DISCOUNT)));
            map.put(COLUMN_MFG_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_MFG_NAME)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public List<DosageModel> getAllDosage() {
        List<DosageModel> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + DOSAGE_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            DosageModel map = new DosageModel();
            map.MedicineName=cursor.getString(cursor.getColumnIndex(MEDICINE));
            map.Time=cursor.getString(cursor.getColumnIndex(MEDICINE_TIME));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }


    public String getFavConcatString() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String concate = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            if (concate.equalsIgnoreCase("")) {
                concate = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            } else {
                concate = concate + "_" + cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            }
            cursor.moveToNext();
        }
        return concate;
    }

    public void clearCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }
    public void clearNotification() {
        db = getReadableDatabase();
        db.execSQL("delete from " + NOTIFICATION_TABLE);
    }

    public void removeItemFromCart(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + " where " + COLUMN_ID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

}
