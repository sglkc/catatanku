package id.my.sglkc.catatanku.database;

public class AccountsTable {
    public static final String TABLE = "accounts";
//    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String SCHOOL = "school";
    public static final String ADDRESS = "address";
    static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE;
    static final String SQL_CREATE =
            "CREATE TABLE " + TABLE + " (" +
                    USERNAME + " TEXT PRIMARY KEY," +
                    PASSWORD + " TEXT," +
                    EMAIL + " TEXT," +
                    NAME + " TEXT," +
                    SCHOOL + " TEXT," +
                    ADDRESS + " TEXT)";
}
