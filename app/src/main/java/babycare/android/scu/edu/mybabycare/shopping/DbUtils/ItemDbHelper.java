package babycare.android.scu.edu.mybabycare.shopping.DbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;

/**
 * Created by Soumya on 5/11/2015.
 */
public class ItemDbHelper extends SQLiteOpenHelper {
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String CATEGORY = "category";
    public static final String BRAND_NAME = "brand_name";
    public static final String ITEM_COUNT = "item_count";
    public static final String PURCHASE_DATE = "purchase_date";
    public static final String EXPIRY_DATE = "expiry_date";
    public static final String STORE_ADDRESS = "store_address";
    public static final String STORE_LATITUDE = "store_latitude";
    public static final String STORE_LONGITUDE = "store_longitude";
    public static final String REMINDER_SET = "reminder_set";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String DATABASE_TABLE = "item_details";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "baby_care";
    public static SQLiteDatabase db = null;


    private static final String DATABASE_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s (" + "  %s integer primary key autoincrement, " + "  %s text," + "  %s text," + "  %s text," + "  %s integer," + "  %s text," + "  %s text," + "  %s text," + " %s text," + "  %s text," + "  %s integer," + "  %s integer) ", DATABASE_TABLE, PRODUCT_ID, PRODUCT_NAME, CATEGORY, BRAND_NAME, ITEM_COUNT, PURCHASE_DATE, EXPIRY_DATE, STORE_ADDRESS, STORE_LATITUDE, STORE_LONGITUDE, REMINDER_SET, IS_FAVORITE);

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    //add a new item
    public Item addItem(Item item) {
        db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(ItemDbHelper.PRODUCT_NAME, item.getProductName());
        newValues.put(ItemDbHelper.ITEM_COUNT, item.getItemCount());
        newValues.put(ItemDbHelper.BRAND_NAME, item.getBrandName());
        newValues.put(ItemDbHelper.STORE_ADDRESS, item.getStoreAddress());
        newValues.put(ItemDbHelper.STORE_LATITUDE, item.getStoreLatitude());
        newValues.put(ItemDbHelper.STORE_LONGITUDE, item.getStoreLongitude());
        newValues.put(ItemDbHelper.EXPIRY_DATE, item.getExpiryDate());
        newValues.put(ItemDbHelper.PURCHASE_DATE, item.getPurchaseDate());
        newValues.put(ItemDbHelper.REMINDER_SET, item.isReminderSet());
        newValues.put(ItemDbHelper.IS_FAVORITE, item.isFavorite());
        newValues.put(ItemDbHelper.CATEGORY, item.getCategory());

        long row = db.insert(ItemDbHelper.DATABASE_TABLE, null, newValues);
        System.out.println("rows inserted"+row);
        db.close();
        return item;
    }
    //update Item details
    public void updateItem(Item item) {
        db = this.getWritableDatabase();
        String whereClause = ItemDbHelper.PRODUCT_ID + "="+item.getProductId();
        ContentValues newValues = new ContentValues();
        newValues.put(ItemDbHelper.PRODUCT_NAME, item.getProductName());
        newValues.put(ItemDbHelper.ITEM_COUNT, item.getItemCount());
        newValues.put(ItemDbHelper.BRAND_NAME, item.getBrandName());
        newValues.put(ItemDbHelper.STORE_ADDRESS, item.getStoreAddress());
        newValues.put(ItemDbHelper.STORE_LATITUDE, String.valueOf(item.getStoreLatitude()));
        newValues.put(ItemDbHelper.STORE_LONGITUDE, String.valueOf(item.getStoreLongitude()));
        newValues.put(ItemDbHelper.EXPIRY_DATE, item.getExpiryDate());
        newValues.put(ItemDbHelper.PURCHASE_DATE, item.getPurchaseDate());
        newValues.put(ItemDbHelper.REMINDER_SET, item.isReminderSet());
        newValues.put(ItemDbHelper.IS_FAVORITE, item.isFavorite());
        newValues.put(ItemDbHelper.CATEGORY, item.getCategory());
        int i = db.update(ItemDbHelper.DATABASE_TABLE, newValues, whereClause, null);
        db.close();
    }

    // get all distinct brand names
    public List<String> getAllBrands() {
        List<String> brandNames = new ArrayList<String>();
        db = this.getWritableDatabase();
        String[] resultColumns = {ItemDbHelper.BRAND_NAME};
        Cursor cursor = db.query(true, ItemDbHelper.DATABASE_TABLE, resultColumns, null, null, ItemDbHelper.BRAND_NAME, null, null, null);

        if(cursor == null){
            return brandNames;
        }
        while (cursor.moveToNext()) {
            brandNames.add(cursor.getString(0));
        }
        return brandNames;
    }

    // get item by id
    public Item getItemById(int id) {
        String whereClause = ItemDbHelper.PRODUCT_ID + "="+id;
        Item item = null;
        db = this.getWritableDatabase();
        String[] resultColumns = {PRODUCT_ID, PRODUCT_NAME, CATEGORY, BRAND_NAME, ITEM_COUNT, PURCHASE_DATE, EXPIRY_DATE, STORE_ADDRESS, REMINDER_SET, IS_FAVORITE};
        Cursor cursor = db.query(ItemDbHelper.DATABASE_TABLE, resultColumns, whereClause, null, null, null, null);

        if(cursor == null){
            return item;
        }
        while (cursor.moveToNext()) {
            item = new Item();
            item.setProductId(cursor.getInt(0));
            item.setProductName(cursor.getString(1));
            item.setCategory(cursor.getString(2));
            item.setBrandName(cursor.getString(3));
            item.setItemCount(cursor.getInt(4));
            item.setExpiryDate(cursor.getString(6));
            item.setPurchaseDate(cursor.getString(5));
            item.setStoreAddress(cursor.getString(7));
            item.setReminderSet(cursor.getInt(8) != 0);
            item.setFavorite(cursor.getInt(9) != 0);
        }
        return item;
    }

    // get all items matching search text
    public List<Item> getAllItems(String searchText) {
        List<Item> items = new ArrayList<Item>();
        // to be modified
        String whereClause = null;
        if(!searchText.equalsIgnoreCase("")){
            whereClause = ItemDbHelper.PRODUCT_NAME + " LIKE '%"+searchText+"%' OR "+ItemDbHelper.BRAND_NAME+" LIKE '%"+searchText+"%' OR "+ItemDbHelper.CATEGORY+" LIKE '%"+searchText+"%'";
        }
        Item item = null;
        db = this.getWritableDatabase();
        String[] resultColumns = {PRODUCT_ID, PRODUCT_NAME, CATEGORY, BRAND_NAME, ITEM_COUNT, PURCHASE_DATE, EXPIRY_DATE, STORE_ADDRESS, REMINDER_SET, IS_FAVORITE};
        Cursor cursor = db.query(ItemDbHelper.DATABASE_TABLE, resultColumns, whereClause, null, null, null, null);

        if(cursor == null){
            return items;
        }
        while (cursor.moveToNext()) {
            item = new Item();
            item.setProductId(cursor.getInt(0));
            item.setProductName(cursor.getString(1));
            item.setCategory(cursor.getString(2));
            item.setBrandName(cursor.getString(3));
            item.setItemCount(cursor.getInt(4));
            item.setExpiryDate(cursor.getString(6));
            item.setPurchaseDate(cursor.getString(5));
            item.setStoreAddress(cursor.getString(7));
            item.setReminderSet(cursor.getInt(8) != 0);
            item.setFavorite(cursor.getInt(9) != 0);
            items.add(item);
        }
        return items;
    }

    /***
     *
     * @return list of items for which proximity alert has been set
     */

    //TO-DO : Need to return just needed columns instead of all
    //TO-DO : Add where clause based on expiry date

    public List<Item> getItemsForProximityAlert(){

        List<Item> items = new ArrayList<Item>();
        db = this.getReadableDatabase();
        String[] resultColumns = {PRODUCT_ID, PRODUCT_NAME, CATEGORY, BRAND_NAME, ITEM_COUNT, PURCHASE_DATE, EXPIRY_DATE, STORE_ADDRESS,STORE_LATITUDE,STORE_LONGITUDE, REMINDER_SET, IS_FAVORITE};
        Cursor cursor = db.query(ItemDbHelper.DATABASE_TABLE,resultColumns,null,null,null,null,null);
        Item item = null;
        if(cursor == null){
            return items;
        }

        while (cursor.moveToNext()){
            item = new Item();
            item.setProductId(cursor.getInt(0));
            item.setProductName(cursor.getString(1));
            item.setCategory(cursor.getString(2));
            item.setBrandName(cursor.getString(3));
            item.setItemCount(cursor.getInt(4));
            item.setExpiryDate(cursor.getString(6));
            item.setPurchaseDate(cursor.getString(5));
            item.setStoreAddress(cursor.getString(7));
            item.setStoreLatitude(cursor.getString(8));
            item.setStoreLongitude(cursor.getString(9));
            item.setReminderSet(cursor.getInt(10) != 0);
            item.setFavorite(cursor.getInt(11) != 0);
            items.add(item);
        }
        return items;
    }
}
