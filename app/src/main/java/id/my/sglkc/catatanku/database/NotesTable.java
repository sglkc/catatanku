package id.my.sglkc.catatanku.database;

public class NotesTable {
    public static final String TABLE = "notes";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String MODIFIED = "modified";
    static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE;
    static final String SQL_CREATE =
            "CREATE TABLE " + TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    USERNAME + " TEXT," +
                    TITLE + " TEXT," +
                    CONTENT + " TEXT," +
                    MODIFIED + " TEXT)";
}
