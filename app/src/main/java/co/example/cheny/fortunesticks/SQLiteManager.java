package co.example.cheny.fortunesticks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageView;

import java.sql.RowId;

public class SQLiteManager extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1;//資料庫版本
    private final static String DB_NAME = "SQLite.sqlite";//資料庫名稱，附檔名為db
    private final static String STICK_TABLE = "stick_table";

    public final static String ROW_ID = "_id";
    public final static String LUCKY = "lucky";
    public final static String CONTENT = "content";
    public final static String STICK = "stick";

    public SQLiteManager(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE IF NOT EXISTS " +  STICK_TABLE
                + "(" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + LUCKY + " TEXT, "
                + CONTENT + " TEXT, "
                + STICK + " INTEGER " + ")";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor select()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STICK_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public long insert(String number, String content, int stickNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LUCKY, number);
        contentValues.put(CONTENT, content);
        contentValues.put(STICK, stickNumber);
        long row = db.insert(STICK_TABLE, null, contentValues);
        return row;
    }

    public void delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ROW_ID + " = " + Integer.toString(id) ;
        db.delete(STICK_TABLE, where, null);
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + STICK_TABLE );
    }
}
