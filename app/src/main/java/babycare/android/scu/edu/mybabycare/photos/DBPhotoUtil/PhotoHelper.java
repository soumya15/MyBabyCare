package babycare.android.scu.edu.mybabycare.photos.DBPhotoUtil;

/**
 * Created by akshu on 5/23/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotoHelper extends SQLiteOpenHelper {
    public static final String PHOTO_ID = "photo_id";
    public static final String PHOTO_BLOB = "photo_blob";
    public static final String PHOTO_CAPTION = "photo_caption";

    private static final String DATABASE_NAME="baby_care";
    private static final int SCHEMA_VERSION=1;
    public static final String DATABASE_TABLE = "baby_photos";
    private static final String DATABASE_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s (" + "  %s integer primary key autoincrement, " + "  %s blob," + "  %s text)", DATABASE_TABLE, PHOTO_ID, PHOTO_BLOB, PHOTO_CAPTION);
    public static SQLiteDatabase db = null;

    public PhotoHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
    public Cursor getAll() {

        db = this.getWritableDatabase();

        String[] resultColumns = {PHOTO_ID, PHOTO_BLOB, PHOTO_CAPTION};
        Cursor cursor = db.query(PhotoHelper.DATABASE_TABLE, resultColumns, null, null, null, null, null);
        return cursor;
    }
    public long insert(byte[] photoBytes, String photoCaption)
    {
        db = this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(PHOTO_BLOB,photoBytes);
        cv.put(PHOTO_CAPTION,photoCaption);

        long id = db.insert(DATABASE_TABLE,PHOTO_CAPTION,cv);
        return id;

    }
}