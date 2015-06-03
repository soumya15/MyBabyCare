package babycare.android.scu.edu.mybabycare.shopping.DBModels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Soumya on 5/11/2015.
 */
public class Item implements Serializable{
    private Integer productId;
    private String productName;
    private String category;
    private String brandName;
    private Integer itemCount;
    private String purchaseDate;
    private String expiryDate;
    private String storeAddress;
    private String storeLatitude;
    private String storeLongitude;
    private boolean isReminderSet;
    private boolean isFavorite;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public boolean isReminderSet() {
        return isReminderSet;
    }

    public void setReminderSet(boolean isReminderSet) {
        this.isReminderSet = isReminderSet;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(String storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public String getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(String storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public void setStoreAddress(String storeLocation) {
        this.storeAddress = storeLocation;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Item() {
        //default constructor
    }

    public Item(Integer productId, String productName, String category, String brandName, Integer itemCount, String purchaseDate, String expiryDate, String storeAddress, String storeLatitude, String storeLongitude, boolean isReminderSet, boolean isFavorite) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.brandName = brandName;
        this.itemCount = itemCount;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.storeAddress = storeAddress;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.isReminderSet = isReminderSet;
        this.isFavorite = isFavorite;
    }

    public Date getExpiryDateFormat(){
        DateFormat format = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(getExpiryDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date getPurchaseDateFormat(){
        DateFormat format = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(getPurchaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(this);
        return b.toByteArray();
    }
}
