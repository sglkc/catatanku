package id.my.sglkc.catatanku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import id.my.sglkc.catatanku.database.AccountsTable;
import id.my.sglkc.catatanku.database.DatabaseHelper;
import id.my.sglkc.catatanku.database.LoggedInTable;

public class Account extends AppCompatActivity {
    public enum Responses { SUCCESS, ERROR, NOT_FOUND, WRONG_PASSWORD, ALREADY_EXISTS };
    public static String username, password, email, name, school, address = "";
    public static DatabaseHelper dbHelper;

    public static void setContext(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static void setDetails(String ...args) {
        username = args[0];
        password = args[1];
        email = args[2];
        name = args[3];
        school = args[4];
        address = args[5];
    }

    public static Responses register(String[] data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AccountsTable.USERNAME, data[0]);
        values.put(AccountsTable.PASSWORD, data[1]);
        values.put(AccountsTable.EMAIL, data[2]);
        values.put(AccountsTable.NAME, data[3]);
        values.put(AccountsTable.SCHOOL, data[4]);
        values.put(AccountsTable.ADDRESS, data[5]);

        long result = db.insert(AccountsTable.TABLE, null, values);

        if (result == -1) return Responses.ALREADY_EXISTS;

        login(data[0], data[1]);
        return Responses.SUCCESS;
    }

    public static Responses login(String username, String password) {
      return Account.login(username, password, true);
    }

    public static Responses login(String username, String password, boolean save) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                AccountsTable.TABLE,
                null,
                AccountsTable.USERNAME + " = ? AND " + AccountsTable.PASSWORD + " = ?",
                new String[] { username, password },
                null,
                null,
                null);

        int columnCount = cursor.getColumnCount();
//        int resultCount = cursor.getCount();

        if (!cursor.moveToFirst()) return Responses.NOT_FOUND;

        String[] data = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            data[i] = cursor.getString(i);
        }

        cursor.close();
        setDetails(data);

        if (save && !saveLogin()) {
            return Responses.ERROR;
        }

        return Responses.SUCCESS;
    }

    public static boolean saveLogin() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LoggedInTable.USERNAME, username);
        values.put(LoggedInTable.PASSWORD, password);

        long result = db.insert(LoggedInTable.TABLE, null, values);

        return result != 0;
    }

    public static boolean alreadyLoggedIn() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(LoggedInTable.TABLE, null, null, null, null, null, null, "1");

        if (!cursor.moveToFirst()) return false;

        String username = cursor.getString(0);
        String password = cursor.getString(1);

        login(username, password, false);
        cursor.close();

        return true;
    }

    public static void logout() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(LoggedInTable.TABLE, "1", null);
    }
}
