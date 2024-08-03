package id.my.sglkc.catatanku.database;

public class LoggedInTable {
    public static final String TABLE = "login";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE;
    static final String SQL_CREATE =
            "CREATE TABLE " + TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    USERNAME + " TEXT," +
                    PASSWORD + " TEXT)";
}
